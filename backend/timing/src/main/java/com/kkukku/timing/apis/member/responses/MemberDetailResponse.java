package com.kkukku.timing.apis.member.responses;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetailResponse {

    private String nickname;
    private String profileImageUrl;

}
