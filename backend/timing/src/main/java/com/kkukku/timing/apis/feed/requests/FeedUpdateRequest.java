package com.kkukku.timing.apis.feed.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedUpdateRequest {

    private String review;
    private Boolean isPrivate;
    
}
