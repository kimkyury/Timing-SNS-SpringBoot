package com.kkukku.timing.elasticsearch.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HashtagDto {

    private Long id;
    private String hashtag;
}
