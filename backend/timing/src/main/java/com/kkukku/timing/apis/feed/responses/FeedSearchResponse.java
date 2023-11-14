package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.s3.services.S3Service;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedSearchResponse {

    private List<FeedSummaryResponse> feeds = new ArrayList<>();

    public void setFeed(FeedEntity feed, S3Service s3Service) {
        feeds.add(new FeedSummaryResponse(feed, s3Service));
    }
}
