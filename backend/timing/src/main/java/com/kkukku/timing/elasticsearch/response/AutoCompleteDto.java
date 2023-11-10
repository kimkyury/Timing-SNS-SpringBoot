package com.kkukku.timing.elasticsearch.response;

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

    public static class Response {

    }
}
