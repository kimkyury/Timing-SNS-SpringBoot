package com.kkukku.timing.apis.test.controllers;

import static com.kkukku.timing.response.ApiResponseUtil.success;

import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Test", description = "Test API")
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;
    private final RedisProperties redisProperties;

    @GetMapping("/ping")
    public String ping() {
<<<<<<< Updated upstream
        return "pong";
=======
        System.out.println(redisProperties.getHost());
        System.out.println(redisProperties.getPort());
        System.out.println(redisProperties.getPassword());
        return "pong2";
>>>>>>> Stashed changes
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("image") MultipartFile image) {

        String fileName = s3Service.uploadFileProcedure(image);
        return success("Saved fileName: " + fileName);
    }

    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestParam String fileName) {

        Optional<S3Object> s3ObjectOptional = Optional.ofNullable(s3Service.getFile(fileName));
        if (s3ObjectOptional.isEmpty()) {
            return ApiResponseUtil.error(ErrorCode.NOT_FOUND);
        }

        S3Object s3Object = s3ObjectOptional.get();
        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(s3Object.getObjectMetadata()
                                                                           .getContentType()))
                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                 "attachment; filename=\"" + fileName + "\"")
                             .body(resource);

    }
}
