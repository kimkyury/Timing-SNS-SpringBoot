package com.kkukku.timing.apis.member.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequest {

    @NotNull(message = "Nickname cannot be null")
    @NotEmpty(message = "Nickname cannot be Empty")
    private String nickname;

}
