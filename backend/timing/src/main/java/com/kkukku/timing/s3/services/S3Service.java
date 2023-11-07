package com.kkukku.timing.s3.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    @Value("${cloud.aws.s3.url}")
    @Getter
    private String s3StartUrl;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) {

        String originalFilename = clean(file.getOriginalFilename());
        String safeFilename = originalFilename.replaceAll("\\s+", "_"); // 공백 제거
        String fileName = UUID.randomUUID() + "_" + safeFilename;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(
                new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_SAVE_FILE_S3);
        }

        return fileName;
    }

    private String clean(final String str) {
        return str == null ? "" : str.trim(); // NULL 방지
    }

    public S3Object getFile(String fileName) {

        return amazonS3.getObject(new GetObjectRequest(BUCKET_NAME, fileName));
    }

    public void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(BUCKET_NAME, fileName.substring(1)));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_DELETE_FILE_S3);
        }
    }

}
