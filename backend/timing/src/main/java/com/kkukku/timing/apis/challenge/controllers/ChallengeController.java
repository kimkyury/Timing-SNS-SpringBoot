package com.kkukku.timing.apis.challenge.controllers;


import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.requests.ChallengeRelayRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/challenges")
@Tag(name = "2. Challenge", description = "Challenge API")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(summary = "본인의 Challenge 생성", tags = {"2. Challenge"},
        description = "Challenge가 생성 && 새로운 hashTag 생성 && 연관관계 정보 생성 ")
    @PostMapping(value = "")
    public ResponseEntity<Void> createChallenge(
        @Validated @RequestBody ChallengeCreateRequest challengeCreateRequest) {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.createChallengeProcedure(memberId, challengeCreateRequest);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "본인의 Challenge 목록 가져오기", tags = {"2. Challenge"},
        description = "Main, Mypage에 사용될 본인 Challenge 목록들입니다. ")
    @GetMapping(value = "")
    public ResponseEntity<ChallengeResponse> getChallenge() {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        ChallengeResponse challengeResponse = challengeService.getChallenge(memberId);

        return ApiResponseUtil.success(challengeResponse);
    }

    @Operation(summary = "본인의 특정 Challenge 삭제하기", tags = {"2. Challenge"},
        description = "본인의 특정 Challenge를 삭제하며, 관련 Snapshot들도 삭제합니다.  ")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.deleteChallenge(memberId, id);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "본인의 특정 Challenge 기간 연장하기", tags = {"2. Challenge"},
        description = "본인의 특정 Challenge 완료 후, 21일을 연장할 수 있습니다. ")
    @PatchMapping(value = "/{id}/extension")
    public ResponseEntity<Void> extendChallenge(@PathVariable Long id) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.extendChallenge(memberId, id);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "타 멤버의 특정 Challenge 이어하기", tags = {"2. Challenge"},
        description = "타 회원의 Feed 정보를 이어서 본인의 Challenge로 생성합니다. HastTag정보가 연동됩니다. GoalContent는 연동되지 않습니다.(별도 작성) ")
    @PostMapping(value = "/{id}/relay")
    public ResponseEntity<Void> relayChallenge(@PathVariable Long id,
        @Valid @RequestBody ChallengeRelayRequest request) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.relayChallenge(memberId, id, request);

        return ApiResponseUtil.success();
    }
}
