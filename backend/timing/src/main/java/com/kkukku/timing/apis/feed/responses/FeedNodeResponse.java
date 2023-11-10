package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedNodeResponse {

    private List<FeedNodeResponse> childs;
    private Long id;
    private String thumbnailUrl;
    private Boolean isPrivate;
    private Boolean isDelete;

    public FeedNodeResponse(FeedEntity feed) {
        this.childs = new ArrayList<>();
        this.id = feed.getId();

        if (feed.getIsPrivate() || feed.getIsDelete()) {
            this.thumbnailUrl = "/default_thumbnail.png";
        } else {
            this.thumbnailUrl = feed.getThumbnailUrl();
        }

        this.isPrivate = feed.getIsPrivate();
        this.isDelete = feed.getIsDelete();
    }

}
