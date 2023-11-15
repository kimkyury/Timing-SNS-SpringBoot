package com.kkukku.timing.elasticsearch.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AutoCompleteDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String search;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private List<HashtagDto> hashtags;
    }
}
