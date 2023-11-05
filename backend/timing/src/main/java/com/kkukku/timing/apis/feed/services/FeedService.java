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
                             .map(feed -> new FeedDetailResponse(feed, likeService.isLiked(
                                 feed.getId()),
                                 feedHashTagService.getHashTagsByFeedId(
                                     feed.getId()),
                                 commentService.getCommentCountByFeedId(feed.getId()),
                                 likeService.getLikeCountByFeedId(feed.getId()),
                                 countInfluencedFeeds(feed.getId()), s3Service))
                             .toList();
    }

    public Long countOwnFeeds() {
        return feedRepository.countByMember_IdAndIsDeleteIsFalse(
            SecurityUtil.getLoggedInMemberPrimaryKey());
    }

    public List<FeedOwnResponse> getOwnFeeds() {
        return feedRepository.findAllByMember_IdAndIsDeleteIsFalse(
                                 SecurityUtil.getLoggedInMemberPrimaryKey())
                             .stream()
                             .map(FeedOwnResponse::new)
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
                             .map(FeedOtherResponse::new)
                             .toList();
    }

    public FeedEntity getFeed(Long id) {
        return feedRepository.findById(id)
                             .orElseThrow(() -> new CustomException(
                                 ErrorCode.NOT_EXIST_FEED));
    }

    public List<FeedEntity> getFeedsByRootId(Long rootId) {
        return feedRepository.findAllByRoot_Id(rootId);
    }

    public Long countInfluencedFeeds(Long id) {
        return 0L;
    }

}
