package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedSummaryResponse {

    private Long id;
    private String thumbnailUrl;
    private Boolean isPrivate;

    public FeedSummaryResponse(FeedEntity feed) {
        this.id = feed.getId();
        this.thumbnailUrl = feed.getThumbnailUrl();
        this.isPrivate = feed.getIsPrivate();
    }
}
