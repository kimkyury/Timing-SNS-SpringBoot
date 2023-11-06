package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedOtherResponse {

    private Long id;
    private String thumbnailUrl;
    private Long feedCount;
    private Integer contributeCount;

    public FeedOtherResponse(FeedEntity feed, Long feedCount, Integer contributeCount) {
        this.id = feed.getId();
        this.thumbnailUrl = feed.getThumbnailUrl();
        this.feedCount = feedCount;
        this.contributeCount = contributeCount;
    }
}
