package com.kkukku.timing.apis.feed.responses;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.s3.services.S3Service;
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

    public FeedNodeResponse(FeedEntity feed, S3Service s3Service) {
        this.childs = new ArrayList<>();
        this.id = feed.getId();

        if (feed.getIsPrivate() || feed.getIsDelete()) {
            this.thumbnailUrl = s3Service.getS3StartUrl() + "/default_thumbnail.png";
        } else {
            this.thumbnailUrl = s3Service.getS3StartUrl() + feed.getThumbnailUrl();
        }

        this.isPrivate = feed.getIsPrivate();
        this.isDelete = feed.getIsDelete();
    }

}
