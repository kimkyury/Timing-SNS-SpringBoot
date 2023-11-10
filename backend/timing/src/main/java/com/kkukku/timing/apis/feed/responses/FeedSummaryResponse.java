package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.s3.services.S3Service;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedSummaryResponse {

    private Long id;
    private String thumbnailUrl;
    private Boolean isPrivate;

    public FeedSummaryResponse(FeedEntity feed, S3Service s3Service) {
        this.id = feed.getId();
        this.thumbnailUrl = s3Service.getS3StartUrl() + feed.getThumbnailUrl();
        this.isPrivate = feed.getIsPrivate();
    }
}
