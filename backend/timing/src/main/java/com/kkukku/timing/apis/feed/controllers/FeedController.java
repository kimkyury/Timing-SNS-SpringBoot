package com.kkukku.timing.apis.feed.controllers;

import com.kkukku.timing.apis.comment.requests.CommentSaveRequest;
import com.kkukku.timing.apis.comment.responses.CommentResponse;
import com.kkukku.timing.apis.comment.services.CommentService;
import com.kkukku.timing.apis.feed.responses.FeedDetailResponse;
import com.kkukku.timing.apis.feed.services.FeedService;
import com.kkukku.timing.apis.like.services.LikeService;
import com.kkukku.timing.response.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feeds")
@Tag(name = "3. Feed", description = "Feed API")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Operation(summary = "추천 피드 상세 조회", tags = {"3. Feed"},
        description = "현재는 랜덤 피드를 뽑아옵니다.")
    @GetMapping("/recommeded")
    public ResponseEntity<List<FeedDetailResponse>> getRecommendFeeds() {
        return ApiResponseUtil.success(feedService.getRecommendFeeds());
    }

    @Operation(summary = "피드 상세 조회", tags = {"3. Feed"})
    @GetMapping("/{id}")
    public ResponseEntity<FeedDetailResponse> getFeedDetail(@PathVariable Long id) {
        return ApiResponseUtil.success(feedService.getFeedDetail(id));
    }

    @Operation(summary = "피드 요약 조회", tags = {"3. Feed"},
        description = "email이 있으면 다른 사람, email이 없으면 로그인한 유저")
    @GetMapping("")
    public ResponseEntity<?> getSomeoneFeeds(
        @RequestParam(name = "email", required = false) @Email(message = "Invalid email format") @NotBlank(message = "Email cannot be blank") String email) {
        if (email != null) {
            return ApiResponseUtil.success(feedService.getOtherFeeds(email));
        }

        return ApiResponseUtil.success(feedService.getOwnFeeds());
    }

    @Operation(summary = "피드 댓글 조회", tags = {
        "3. Feed"}, description = "page 값은 1부터 가능하고, 10개씩 주어집니다.")
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id,
        @RequestParam(name = "page") Integer page) {
        return ApiResponseUtil.success(commentService.getCommentsByFeedId(id, page, 10));
    }

    @Operation(summary = "피드 댓글 작성", tags = {"3. Feed"})
    @PostMapping("/{id}/comments")
    public void saveComment(@PathVariable Long id,
        @RequestBody @Valid CommentSaveRequest commentSaveRequest) {
        commentService.saveComment(id, commentSaveRequest.getContent());
    }

    @Operation(summary = "피드 좋아요", tags = {"3. Feed"})
    @PostMapping("/{id}/likes")
    public void likeFeed(@PathVariable Long id) {
        likeService.saveLike(id);
    }

    @Operation(summary = "피드 좋아요 취소", tags = {"3. Feed"})
    @DeleteMapping("/{id}/likes")
    public void dislikeFeed(@PathVariable Long id) {
        likeService.deleteLike(id);
    }
}
