package com.kkukku.timing.apis.challenge.controllers;


import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(summary = "Challenge의 생성", tags = {"2. Challenge"},
        description = "Challenge가 생성 && 새로운 hashTag 생성 && 연관관계 정보 생성 ")
    @PostMapping(value = "")
    public ResponseEntity<Void> createChallenge(
        @Valid @RequestBody ChallengeCreateRequest challengeCreateRequest) {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.createChallengeProcedure(memberId, challengeCreateRequest);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "본인 Challenge 목록 가져오기", tags = {"2. Challenge"},
        description = "Main, Mypage에 사용될 본인 Challenge 목록들입니다. ")
    @GetMapping(value = "")
    public ResponseEntity<ChallengeResponse> getChallenge() {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        ChallengeResponse challengeResponse = challengeService.getChallenge(memberId);

        return ApiResponseUtil.success(challengeResponse);
    }

}
