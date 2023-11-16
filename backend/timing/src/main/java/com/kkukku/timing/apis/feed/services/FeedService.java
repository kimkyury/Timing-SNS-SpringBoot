package com.kkukku.timing.apis.feed.services;

import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.apis.challenge.services.SnapshotService;
import com.kkukku.timing.apis.comment.responses.CommentResponse;
import com.kkukku.timing.apis.comment.services.CommentService;
import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.apis.feed.repositories.FeedRepository;
import com.kkukku.timing.apis.feed.responses.FeedDetailResponse;
import com.kkukku.timing.apis.feed.responses.FeedNodeResponse;
import com.kkukku.timing.apis.feed.responses.FeedSearchResponse;
import com.kkukku.timing.apis.feed.responses.FeedSummaryResponse;
import com.kkukku.timing.apis.feed.responses.FeedSummaryWithCountResponse;
import com.kkukku.timing.apis.hashtag.repositories.FeedHashTagRepository;
import com.kkukku.timing.apis.hashtag.services.ChallengeHashTagService;
import com.kkukku.timing.apis.hashtag.services.FeedHashTagService;
import com.kkukku.timing.apis.like.services.LikeService;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.external.services.VisionAIService;
import com.kkukku.timing.jwt.services.JwtService;
import com.kkukku.timing.redis.services.RedisService;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import com.kkukku.timing.security.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient.ResponseSpec;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedHashTagService feedHashTagService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final S3Service s3Service;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final ChallengeService challengeService;
    private final SnapshotService snapshotService;
    private final ChallengeHashTagService challengeHashTagService;
    private final VisionAIService visionAIService;
    private final FeedHashTagRepository feedHashTagRepository;

    public List<FeedDetailResponse> getRecommendFeeds(Integer page) {

        Pageable pageable = PageRequest.of(page - 1, 9);

        return feedRepository.findFeedsOrderById(SecurityUtil.getLoggedInMemberPrimaryKey(),
                                 pageable)
                             .stream()
                             .map(feed -> getFeedDetail(feed.getId()))
                             .toList();
    }

    public FeedDetailResponse getFeedDetail(Long id) {
        FeedEntity feed = getFeedById(id);

        accessCheck(feed);

        return new FeedDetailResponse(feed, likeService.isLiked(id),
            feedHashTagService.getHashTagsByFeedId(id),
            commentService.getCommentCountByFeedId(id),
            likeService.getLikeCountByFeedId(id), countInfluencedFeeds(id), s3Service);
    }

    public Long countOwnFeeds() {
        return feedRepository.countByMember_IdAndIsDeleteIsFalse(
            SecurityUtil.getLoggedInMemberPrimaryKey());
    }

    public List<FeedSummaryResponse> getOwnSummaryFeeds() {
        return feedRepository.findAllByMember_IdAndIsDeleteIsFalse(
                                 SecurityUtil.getLoggedInMemberPrimaryKey())
                             .stream()
                             .map(feed -> new FeedSummaryResponse(feed, s3Service))
                             .toList();
    }

    public FeedSummaryWithCountResponse getOwnSummaryFeedsWithCount() {
        return new FeedSummaryWithCountResponse(getOwnSummaryFeeds(), countOwnFeeds(),
            countAllInfluencedOwnFeeds());
    }

    public Long countOtherFeeds(String email) {
        return feedRepository.countByMember_IdAndIsDeleteIsFalseAndIsPrivateFalse(
            memberService.getMemberByEmail(email)
                         .getId());
    }

    public List<FeedSummaryResponse> getOtherSummaryFeeds(String email) {
        return feedRepository.findAllByMember_IdAndIsDeleteIsFalseAndIsPrivateFalse(
                                 memberService.getMemberByEmail(email)
                                              .getId())
                             .stream()
                             .map(feed -> new FeedSummaryResponse(feed, s3Service))
                             .toList();
    }

    public FeedSummaryWithCountResponse getOtherSummaryFeedsWithCount(String email) {
        return new FeedSummaryWithCountResponse(getOtherSummaryFeeds(email), countOtherFeeds(email),
            countAllInfluencedOtherFeeds(email));
    }

    public List<FeedEntity> getFeedsByRootId(Long rootId) {
        return feedRepository.findAllByRoot_Id(rootId);
    }

    public List<FeedEntity> getFeedsByMemberId(Integer memberId) {
        return feedRepository.findAllByMember_Id(memberId);
    }

    public Integer countInfluencedFeeds(Long id) {
        List<FeedEntity> feeds = getFeedsByRootId(getFeedById(id).getRoot()
                                                                 .getId());

        int size = feeds.size();
        Integer[] parent = new Integer[size];
        Integer[] count = new Integer[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;
            count[i] = 0;
        }

        for (int i = size - 1; i >= 0; i--) {
            FeedEntity parentFeed = feeds.get(i)
                                         .getParent();

            if (parentFeed == null) {
                continue;
            }

            Integer feedId = i;
            Integer parentId = binarySearch(feeds, parentFeed.getId());

            union(parent, count, feedId, parentId);
        }

        return count[binarySearch(feeds, id)];
    }

    public Integer countAllInfluencedOwnFeeds() {
        List<FeedEntity> feeds = getFeedsByMemberId(SecurityUtil.getLoggedInMemberPrimaryKey());

        Integer count = 0;

        for (FeedEntity feed : feeds) {
            count += countInfluencedFeeds(feed.getId());
        }

        return count;
    }

    public Integer countAllInfluencedOtherFeeds(String email) {
        List<FeedEntity> feeds = getFeedsByMemberId(memberService.getMemberByEmail(email)
                                                                 .getId());

        Integer count = 0;

        for (FeedEntity feed : feeds) {
            count += countInfluencedFeeds(feed.getId());
        }

        return count;
    }

    @Transactional
    public void deleteFeed(Long id) {
        FeedEntity feed = getFeedByIdAndMemberId(id, SecurityUtil.getLoggedInMemberPrimaryKey());

        accessCheck(feed);

        feed.setIsDelete(true);
        s3Service.deleteFile(feed.getThumbnailUrl());
        s3Service.deleteFile(feed.getTimelapseUrl());

        feedRepository.save(feed);
    }

    @Transactional
    public void updateFeed(Long id, String review, Boolean isPrivate) {
        FeedEntity feed = getFeedByIdAndMemberId(id, SecurityUtil.getLoggedInMemberPrimaryKey());

        accessCheck(feed);

        if (review != null) {
            feed.setReview(review);
        }
        if (isPrivate != null) {
            feed.setIsPrivate(isPrivate);
        }

        feedRepository.save(feed);
    }

    public FeedNodeResponse getFeedTree(Long id) {
        Map<Long, FeedNodeResponse> map = new HashMap<>();

        Long rootId = getFeedById(id).getRoot()
                                     .getId();
        feedRepository.findAllByRoot_Id(rootId)
                      .forEach(feed -> {
                          FeedNodeResponse node = new FeedNodeResponse(feed, s3Service);
                          map.put(feed.getId(), node);

                          if (feed.getParent() == null) {
                              return;
                          }

                          FeedNodeResponse parent = map.get(feed.getParent()
                                                                .getId());
                          parent.getChilds()
                                .add(node);
                      });

        return map.get(rootId);
    }

    public FeedEntity getFeedByIdAndMemberId(Long id, Integer memberId) {
        return feedRepository.findByIdAndMember_Id(id, memberId)
                             .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_FEED));
    }

    public FeedEntity getFeedById(Long id) {
        return feedRepository.findById(id)
                             .orElseThrow(() -> new CustomException(
                                 ErrorCode.NOT_EXIST_FEED));
    }

    public List<CommentResponse> getCommentsByFeedId(Long id, Integer page) {
        FeedEntity feed = getFeedById(id);

        accessCheck(feed);

        return commentService.getCommentsByFeedId(id, page, 10);
    }

    public void saveComment(Long id, String content) {
        FeedEntity feed = getFeedById(id);

        accessCheck(feed);

        commentService.saveComment(id, content);
    }

    public void saveLike(Long id) {
        FeedEntity feed = getFeedById(id);

        accessCheck(feed);

        likeService.saveLike(id);
    }

    public void deleteLike(Long id) {
        FeedEntity feed = getFeedById(id);

        accessCheck(feed);

        likeService.deleteLike(id);
    }

    public S3Object getTimelapseFileCheckAccess(Long id) {
        FeedEntity feed = getFeedById(id);

        accessCheck(feed);

        return s3Service.getFile(feed.getTimelapseUrl());
    }

    public S3Object getTimelapseFile(Long id) {
        return s3Service.getFile(getFeedById(id).getTimelapseUrl());
    }

    @Transactional
    public void convertToFeed(Long challengeId) {
        ChallengeEntity challenge = challengeService.getChallengeById(challengeId);

        // challengeService.checkOwnChallenge(SecurityUtil.getLoggedInMemberPrimaryKey(), challengeId);

        // TODO: On Checking, 테스트를 위하여 주석처리
        // challengeService.checkCompletedChallenge(challengeId);

        List<SnapshotEntity> snapshots = snapshotService.getAllSnapshotByChallenge(challengeId);
        Map<String, String> requestBody = getMovieBySnapshotRequestBody(challenge,
            snapshots);
        ResponseSpec response = visionAIService.getMovieBySnapshots(requestBody);

        ResponseEntity<byte[]> responseEntity = response.toEntity(byte[].class);
        byte[] mp4File = response.body(byte[].class);
        HttpStatusCode status = responseEntity.getStatusCode();
        if (status.is4xxClientError()) {
            challenge.setIsProcess(false);
            challengeService.saveChallengeByEntity(challenge);
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        String timelapseUrl = "/" + s3Service.uploadMp4(mp4File,
            "video");

        FeedEntity feed = feedRepository.save(new FeedEntity(challenge, timelapseUrl));
        feed.setRelation(challenge.getParent());
        feedHashTagService.saveHashTagsByFeedId(feed.getId(),
            challengeHashTagService.getHashTagOptionByChallengeId(challengeId));

        challengeService.deleteChallengeProcedure(SecurityUtil.getLoggedInMemberPrimaryKey(),
            challengeId);
    }


    private Map<String, String> getMovieBySnapshotRequestBody(ChallengeEntity challenge,
        List<SnapshotEntity> snapshots) {

        Map<String, String> body = new HashMap<>();
        body.put("object", s3Service.getS3StartUrl() + challenge.getObjectUrl());
        StringBuilder sb = new StringBuilder();

        for (SnapshotEntity snapshot : snapshots) {
            sb.append(s3Service.getS3StartUrl())
              .append(snapshot.getImageUrl())
              .append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        body.put("snapshots", sb.toString());

        return body;
    }

    public void jwtCheck(String jwt) {
        String email = jwtService.extractUsername(jwt);

        if (email == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        if (!jwtService.isTokenValid(jwt, email)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
        String isLogout = redisService.getValue("member:" + email + ":LOGOUT");
        if (isLogout != null) {
            throw new CustomException(ErrorCode.LOGGED_OUT_MEMBER_TOKEN);
        }
    }

    public void accessCheck(FeedEntity feed) {
        if (feed.getIsDelete()) {
            throw new CustomException(ErrorCode.DELETED_FEED);
        }
        if (!feed.getMember()
                 .getId()
                 .equals(SecurityUtil.getLoggedInMemberPrimaryKey()) && feed.getIsPrivate()) {
            throw new CustomException(ErrorCode.PRIVATE_FEED);
        }
    }

    private int find(Integer[] parent, Integer x) {
        if (parent[x].equals(x)) {
            return x;
        }
        return parent[x] = find(parent, parent[x]);
    }

    private void union(Integer[] parent, Integer[] count, Integer x, Integer y) {
        int a = find(parent, x);
        int b = find(parent, y);

        if (a < b) {
            parent[b] = parent[a];
            count[a] += count[b] + 1;
        } else {
            parent[a] = parent[b];
            count[b] += count[a] + 1;
        }
    }

    private Integer binarySearch(List<FeedEntity> feeds, Long parentId) {
        int l = 0;
        int r = feeds.size() - 1;
        int mid = 0;

        while (l <= r) {
            mid = (l + r) / 2;
            Long id = feeds.get(mid)
                           .getId();

            if (id.equals(parentId)) {
                break;
            }

            if (id > parentId) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }

        return mid;
    }

    public FeedSearchResponse getFeedsByHashtag(Long id, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 12);

        List<FeedEntity> feedList = feedHashTagRepository.findByFeedId(id, pageable)
                                                         .toList();
        FeedSearchResponse response = new FeedSearchResponse();
        for (FeedEntity feed : feedList) {
            response.setFeed(feed, s3Service);
        }

        return response;
    }

    public List<FeedDetailResponse> getRecommendFeedsByScore(Integer page) {

        Pageable pageable = PageRequest.of(page - 1, 9);

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        return feedRepository.findFeedsWithScore(memberId, pageable)
                             .stream()
                             .map(feed -> getFeedDetail(
                                 feed.getId()))
                             .toList();
    }
}
