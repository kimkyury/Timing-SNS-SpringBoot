package com.kkukku.timing.apis.feed.services;

import com.kkukku.timing.apis.comment.services.CommentService;
import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.apis.feed.repositories.FeedRepository;
import com.kkukku.timing.apis.feed.responses.FeedDetailResponse;
import com.kkukku.timing.apis.feed.responses.FeedOtherResponse;
import com.kkukku.timing.apis.feed.responses.FeedOwnResponse;
import com.kkukku.timing.apis.hashtag.services.FeedHashTagService;
import com.kkukku.timing.apis.like.services.LikeService;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import com.kkukku.timing.security.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedHashTagService feedHashTagService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final S3Service s3Service;
    private final MemberService memberService;

    public List<FeedDetailResponse> getRecommendFeeds() {
        return feedRepository.findRandomFeeds()
                             .stream()
                             .map(feed -> getFeedDetail(feed.getId()))
                             .toList();
    }

    public FeedDetailResponse getFeedDetail(Long id) {
        return new FeedDetailResponse(getFeedById(id), likeService.isLiked(id),
            feedHashTagService.getHashTagsByFeedId(id), commentService.getCommentCountByFeedId(id),
            likeService.getLikeCountByFeedId(id), countInfluencedFeeds(id), s3Service);
    }

    public Long countOwnFeeds() {
        return feedRepository.countByMember_IdAndIsDeleteIsFalse(
            SecurityUtil.getLoggedInMemberPrimaryKey());
    }

    public List<FeedOwnResponse> getOwnFeeds() {
        return feedRepository.findAllByMember_IdAndIsDeleteIsFalse(
                                 SecurityUtil.getLoggedInMemberPrimaryKey())
                             .stream()
                             .map(feed -> new FeedOwnResponse(feed, countOwnFeeds(),
                                 countAllInfluencedOwnFeeds()))
                             .toList();
    }

    public Long countOtherFeeds(String email) {
        return feedRepository.countByMember_IdAndIsDeleteIsFalseAndIsPrivateFalse(
            memberService.getMemberByEmail(email)
                         .getId());
    }

    public List<FeedOtherResponse> getOtherFeeds(String email) {
        return feedRepository.findAllByMember_IdAndIsDeleteIsFalseAndIsPrivateFalse(
                                 memberService.getMemberByEmail(email)
                                              .getId())
                             .stream()
                             .map(feed -> new FeedOtherResponse(feed, countOtherFeeds(email),
                                 countAllInfluencedOtherFeeds(email)))
                             .toList();
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

        feed.setIsDelete(true);
        s3Service.deleteFile(feed.getThumbnailUrl());
        s3Service.deleteFile(feed.getTimelapseUrl());

        feedRepository.save(feed);
    }

    @Transactional
    public void updateFeed(Long id, String review, Boolean isPrivate) {
        FeedEntity feed = getFeedByIdAndMemberId(id, SecurityUtil.getLoggedInMemberPrimaryKey());

        if (review != null) {
            feed.setReview(review);
        }
        if (isPrivate != null) {
            feed.setIsPrivate(isPrivate);
        }

        feedRepository.save(feed);
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

}
