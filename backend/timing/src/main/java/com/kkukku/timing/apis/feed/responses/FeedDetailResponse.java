package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.apis.hashtag.responses.FeedHashTagResponse;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.s3.services.S3Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedDetailResponse {

    private Long id;
    private MemberDetailResponse writer;
    private MemberDetailResponse parentWriter;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private String goalContent;
    private String thumbnailUrl;
    private Boolean isPrivate;
    private String review;
    private LocalDateTime createdAt;
    private Boolean isLiked;
    private List<FeedHashTagResponse> hashTags;
    private Long commentCount;
    private Long likeCount;
    private Integer shareCount;

    public FeedDetailResponse(FeedEntity feed, Boolean isLiked,
        List<FeedHashTagResponse> hashTags,
        Long commentCount, Long likeCount, Integer shareCount, S3Service s3Service) {
        this.id = feed.getId();

        MemberEntity writer = feed.getMember();
        writer.saveProfileImgUrlWithS3(s3Service);

        if (feed.getParent() != null) {
            MemberEntity parent = feed.getParent()
                                      .getMember();
            parent.saveProfileImgUrlWithS3(s3Service);
            this.parentWriter = new MemberDetailResponse(parent);
        }

        this.writer = new MemberDetailResponse(writer);
        this.startedAt = feed.getStartedAt();
        this.endedAt = feed.getEndedAt();
        this.goalContent = feed.getGoalContent();
        this.thumbnailUrl = feed.getThumbnailUrl();
        this.isPrivate = feed.getIsPrivate();
        this.review = feed.getReview();
        this.createdAt = feed.getCreatedAt();
        this.isLiked = isLiked;
        this.hashTags = hashTags;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
    }
}
