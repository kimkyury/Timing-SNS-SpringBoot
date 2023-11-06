package com.kkukku.timing.apis.comment.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {

    @NotNull(message = "Content cannot be null")
    @NotEmpty(message = "Content cannot be empty")
    private String content;

}
