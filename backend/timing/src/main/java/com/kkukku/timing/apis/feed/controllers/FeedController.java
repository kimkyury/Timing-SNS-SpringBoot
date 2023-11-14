package com.kkukku.timing.apis.feed.controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.kkukku.timing.apis.challenge.requests.ChallengeCompleteRequest;
import com.kkukku.timing.apis.comment.requests.CommentSaveRequest;
import com.kkukku.timing.apis.comment.responses.CommentResponse;
import com.kkukku.timing.apis.feed.requests.FeedUpdateRequest;
import com.kkukku.timing.apis.feed.responses.FeedDetailResponse;
import com.kkukku.timing.apis.feed.responses.FeedNodeResponse;
import com.kkukku.timing.apis.feed.services.FeedService;
import com.kkukku.timing.response.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/v1/feeds")
@Tag(name = "3. Feed", description = "Feed API")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "추천 피드 상세 조회", tags = {"3. Feed"},
            description = "현재는 랜덤 피드를 뽑아옵니다.")
    @GetMapping("/recommended")
    public ResponseEntity<List<FeedDetailResponse>> getRecommendFeeds() {
        return ApiResponseUtil.success(feedService.getRecommendFeeds());
    }

    @Operation(summary = "피드 상세 조회", tags = {
            "3. Feed"}, description = "private은 본인만 조회 가능, delete는 조회 불가능")
    @GetMapping("/{id}")
    public ResponseEntity<FeedDetailResponse> getFeedDetail(@PathVariable Long id) {
        return ApiResponseUtil.success(feedService.getFeedDetail(id));
    }

    @Operation(summary = "피드 요약 조회", tags = {"3. Feed"},
            description = "email이 있으면 다른 사람, email이 없으면 로그인한 유저")
    @GetMapping("")
    public ResponseEntity<?> getSomeoneFeeds(
            @RequestParam(name = "email", required = false) String email) {
        if (email != null) {
            return ApiResponseUtil.success(feedService.getOtherSummaryFeedsWithCount(email));
        }

        return ApiResponseUtil.success(feedService.getOwnSummaryFeedsWithCount());
    }

    @Operation(summary = "피드 삭제", tags = {"3. Feed"})
    @DeleteMapping("/{id}")
    public void deleteFeed(@PathVariable Long id) {
        feedService.deleteFeed(id);
    }

    @Operation(summary = "피드 업데이트", tags = {"3. Feed"},
            description = "review와 isPrivate 둘 중 하나만 있어도 되고, 하나만 있어도 됩니다.")
    @PatchMapping("/{id}")
    public void updateFeed(@PathVariable Long id,
            @RequestBody FeedUpdateRequest feedUpdateRequest) {
        feedService.updateFeed(id, feedUpdateRequest.getReview(), feedUpdateRequest.getIsPrivate());
    }

    @Operation(summary = "피드가 이어받은 모든 피드 조회 (트리 조회)", tags = {
            "3. Feed"}, description = "delete 노드와 private 노드는 기본 thumbnail URL로 제공됨")
    @GetMapping("/{id}/influence")
    public ResponseEntity<FeedNodeResponse> getFeedTree(@PathVariable Long id) {
        return ApiResponseUtil.success(feedService.getFeedTree(id));
    }

    @Operation(summary = "피드 타임랩스 다운로드", tags = {
            "3. Feed"}, description = "private은 본인만 조회 가능, delete는 조회 불가능")
    @GetMapping("/{id}/videos")
    public ResponseEntity<?> getFile(@PathVariable Long id) {
        S3Object s3Object = feedService.getTimelapseFileCheckAccess(id);
        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(s3Object.getObjectMetadata()
                        .getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + s3Object.getKey() + "\"")
                .body(resource);
    }

    @Operation(summary = "피드 스트리밍", tags = {"3. Feed"})
    @GetMapping("/{id}/videos/streaming")
    public ResponseEntity<StreamingResponseBody> timelapseStreaming(@PathVariable Long id,
            @RequestParam(name = "access-token") String accessToken) {
        feedService.jwtCheck(accessToken);

        S3Object s3Object = feedService.getTimelapseFile(id);
        S3ObjectInputStream finalObject = s3Object.getObjectContent();

        final StreamingResponseBody body = outputStream -> {
            int numberOfBytesToWrite = 0;
            byte[] data = new byte[1024];
            while ((numberOfBytesToWrite = finalObject.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, numberOfBytesToWrite);
            }
            finalObject.close();
        };

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Operation(summary = "챌린지 완료, 피드 전환", tags = {"3. Feed"})
    @PostMapping("")
    public void convertToFeed(
            @Valid @RequestBody ChallengeCompleteRequest challengeCompleteRequest) {
        feedService.convertToFeed(challengeCompleteRequest.getChallengeId());
    }

    @Operation(summary = "피드 댓글 조회", tags = {
            "3. Feed"}, description = "page 값은 1부터 가능하고, 10개씩 주어집니다. private은 본인만 조회 가능, delete는 조회 불가능")
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id,
            @RequestParam(name = "page") Integer page) {
        return ApiResponseUtil.success(feedService.getCommentsByFeedId(id, page));
    }

    @Operation(summary = "피드 댓글 작성", tags = {
            "3. Feed"}, description = "private은 본인만 조회 가능, delete는 조회 불가능")
    @PostMapping("/{id}/comments")
    public void saveComment(@PathVariable Long id,
            @RequestBody @Valid CommentSaveRequest commentSaveRequest) {
        feedService.saveComment(id, commentSaveRequest.getContent());
    }

    @Operation(summary = "피드 좋아요", tags = {
            "3. Feed"}, description = "private은 본인만 조회 가능, delete는 조회 불가능")
    @PostMapping("/{id}/likes")
    public void likeFeed(@PathVariable Long id) {
        feedService.saveLike(id);
    }

    @Operation(summary = "피드 좋아요 취소", tags = {
            "3. Feed"}, description = "private은 본인만 조회 가능, delete는 조회 불가능")
    @DeleteMapping("/{id}/likes")
    public void dislikeFeed(@PathVariable Long id) {
        feedService.deleteLike(id);
    }

    @Operation(summary = "피드 해시태그 검색", tags = {
            "3. Feed"}, description = "private은 본인만 조회 가능, delete는 조회 불가능")
    @GetMapping("{id}/search")
    public ResponseEntity<?> getFeeds(@PathVariable Long id,
            @RequestParam(name = "page") Integer page) {

        return ApiResponseUtil.success(feedService.getFeedsByHashtag(id, page));
    }
}
