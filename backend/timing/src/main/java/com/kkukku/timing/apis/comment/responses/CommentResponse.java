package com.kkukku.timing.apis.comment.responses;

import com.kkukku.timing.apis.comment.entities.CommentEntity;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.s3.services.S3Service;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

    private Long id;
    private MemberDetailResponse writer;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponse(CommentEntity comment, S3Service s3Service) {
        this.id = comment.getId();

        MemberEntity writer = comment.getMember();
        writer.saveProfileImgUrlWithS3(s3Service);

        this.writer = new MemberDetailResponse(writer);
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
    
}
