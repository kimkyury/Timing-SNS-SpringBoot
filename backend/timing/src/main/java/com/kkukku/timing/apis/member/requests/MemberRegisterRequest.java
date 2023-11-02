package com.kkukku.timing.apis.member.requests;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRegisterRequest {

    @NotNull(message = "Nickname cannot be null")
    @NotEmpty(message = "Nickname cannot be Empty")
    private String nickname;

    @NotNull(message = "birthyear cannot be null")
    @NotEmpty(message = "birthyear cannot be Empty")
    @Past(message = "birthyear must be in the past")
    private Integer birthyear;

    @NotNull(message = "gender cannot be null")
    @NotEmpty(message = "gender cannot be Empty")
    private MemberEntity.Gender gender;

}
