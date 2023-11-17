package com.kkukku.timing.apis.hashtag.responses;

import com.kkukku.timing.apis.hashtag.entities.FeedHashTagEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedHashTagResponse {

    private String content;

    public FeedHashTagResponse(FeedHashTagEntity feedHashTagEntity) {
        this.content = feedHashTagEntity.getHashTagOption()
                                        .getContent();
    }
}
