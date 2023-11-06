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

    public FeedOtherResponse(FeedEntity feed) {
        this.id = feed.getId();
        this.thumbnailUrl = feed.getThumbnailUrl();
    }
}
