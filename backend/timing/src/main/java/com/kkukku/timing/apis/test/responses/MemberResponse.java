package com.kkukku.timing.apis.test.responses;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Integer id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private Boolean isDelete;

    public MemberResponse(MemberEntity member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.profileImageUrl = member.getProfileImageUrl();
        this.isDelete = member.getIsDelete();
    }
    
}
