package com.kkukku.timing.apis.test.controllers;

import static com.kkukku.timing.response.ApiResponseUtil.success;

import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "0. Test", description = "Test API")
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;
    private final TestRepository testRepository;
    private final TestFeedRepository testFeedRepository;

    @Operation(summary = "응답테스트", tags = {"0. Test"})
    @GetMapping("/ping")
    public String ping() {
        return "pong3";
    }

    @Operation(summary = "s3사진 업로드 테스트", tags = {"0. Test"})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("image") MultipartFile image) {

        String fileName = s3Service.uploadFile(image);
        return success("Saved fileName: " + fileName);
    }

    @Operation(summary = "s3사진 가져오기 테스트", tags = {"0. Test"})
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

    @PostMapping("/search")
    public void searchTest(@RequestBody SearchTestDto searchTestDto, Pageable pageable) {
        List<Test> list = testRepository.findAllByNameContaining(searchTestDto.getName(), pageable);
    }

    @PostMapping("/search/nori")
    public void searchTest2(@RequestBody SearchTestDto searchTestDto, Pageable pageable) {
        List<TestFeed> list = testFeedRepository.findAllByContentsContaining(searchTestDto.getName(), pageable);
//        System.out.println(list.size());
//        for(TestFeed t : list) {
//            System.out.println(t);
//        }
    }
}
