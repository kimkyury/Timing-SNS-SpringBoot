package com.kkukku.timing.s3.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    private AmazonS3 amazonS3;

    public String uploadFileProcedure(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        uploadFile(file, fileName);
        return fileName;
    }

    public String uploadFileByChallenge(Long categoryId, MultipartFile file) {
        String fileName =
            categoryId + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        uploadFile(file, fileName);
        return fileName;
    }

    public S3Object getFile(String fileName) {
        return amazonS3.getObject(new GetObjectRequest(BUCKET_NAME, fileName));
    }

    private void uploadFile(MultipartFile file, String fileName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(
                new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_SAVE_FILE_S3);
        }
    }
}
