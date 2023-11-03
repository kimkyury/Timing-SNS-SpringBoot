package com.kkukku.timing.apis.member.entities;

import com.kkukku.timing.apis.member.requests.MemberRegisterRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "members")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String email;

    private String profileImageUrl;

    private String nickname;

    private Integer birthyear;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    public enum Gender {
        M, F;
    }

    public MemberEntity(String email, String profileImageUrl, String nickname) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }

    public void registerInfo(MemberRegisterRequest registerRequest, String profileImageUrl) {
        this.nickname = registerRequest.getNickname();
        this.birthyear = registerRequest.getBirthyear();
        this.gender = registerRequest.getGender();
        this.profileImageUrl = profileImageUrl;
    }

    public void delete() {
        isDelete = true;
    }
}