package com.kkukku.timing.apis.test.controllers;

import static com.kkukku.timing.response.ApiResponseUtil.success;

import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.apis.comment.services.CommentService;
import com.kkukku.timing.apis.feed.repositories.FeedRepository;
import com.kkukku.timing.apis.feed.services.FeedService;
import com.kkukku.timing.apis.hashtag.services.FeedHashTagService;
import com.kkukku.timing.apis.like.services.LikeService;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.test.requests.FeedDummyRequest;
import com.kkukku.timing.apis.test.responses.FeedResponse;
import com.kkukku.timing.apis.test.responses.MemberResponse;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "0. Test", description = "Test API")
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;
    private final FeedService feedService;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedHashTagService feedHashTagService;
    private final LikeService likeService;
    private final CommentService commentService;

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

    @GetMapping(value = "/feeds")
    public ResponseEntity<List<FeedResponse>> getFeeds() {
        return ApiResponseUtil.success(feedRepository.findAll()
                                                     .stream()
                                                     .map(
                                                         feed -> new FeedResponse(feed,
                                                             feedHashTagService.getHashTagsByFeedId(
                                                                 feed.getId()), s3Service))
                                                     .toList());
    }

    @GetMapping(value = "/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ApiResponseUtil.success(memberRepository.findAll()
                                                       .stream()
                                                       .map(MemberResponse::new)
                                                       .toList());
    }

    @PostMapping(value = "/feeds", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> saveFeed(
        @RequestPart List<MultipartFile> snapshots,
        @RequestPart @Valid FeedDummyRequest feedDummyRequest) {
        if (snapshots.size() % 21 != 0) {
            return ResponseEntity.badRequest()
                                 .body("스냅샷 개수가 21의 배수만 가능");
        }

        System.out.println(feedDummyRequest);
        System.out.println(snapshots);

        return ApiResponseUtil.success();

    }
}
