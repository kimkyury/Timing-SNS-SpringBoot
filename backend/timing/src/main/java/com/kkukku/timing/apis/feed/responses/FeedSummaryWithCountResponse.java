package com.kkukku.timing.apis.feed.responses;


import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedSummaryWithCountResponse {

    private List<FeedSummaryResponse> feeds;
    private Long feedCount;
    private Integer contributeCount;

    public FeedSummaryWithCountResponse(List<FeedSummaryResponse> feeds, Long feedCount,
        Integer contributeCount) {
        this.feeds = feeds;
        this.feedCount = feedCount;
        this.contributeCount = contributeCount;
    }

}
