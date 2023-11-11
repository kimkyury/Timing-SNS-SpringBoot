package com.kkukku.timing.apis.test.requests;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedDummyRequest {

    @NotNull(message = "Must need hashTags")
    private List<String> hashTags;
    @NotNull(message = "Must need memberEmail")
    private String memberEmail;
    private Long parentId;
    @NotNull(message = "Must need startedAt")
    private LocalDate startedAt;
    private String goalContent;
    private String review;

}
