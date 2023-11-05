package com.kkukku.timing.apis.member.entities;

import com.kkukku.timing.s3.services.S3Service;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Setter
    private String profileImageUrl;

    @Setter
    private String nickname;

    @Column(nullable = false, columnDefinition = "TINYINT(1)", insertable = false)
    private Boolean isDelete;

    public MemberEntity(String email, String profileImageUrl, String nickname) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }

    public void delete() {
        this.email = String.valueOf(UUID.randomUUID());
        this.nickname = "탈퇴한 사용자";
        this.profileImageUrl = "/default_profile.png";
        this.isDelete = true;
    }

    public void saveProfileImgUrlWithS3(S3Service s3Service) {
        this.profileImageUrl = s3Service.getS3StartUrl() + this.profileImageUrl;
    }

}