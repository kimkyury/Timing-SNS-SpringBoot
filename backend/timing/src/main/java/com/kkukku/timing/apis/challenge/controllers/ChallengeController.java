package com.kkukku.timing.apis.challenge.controllers;


import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateMember(
        @Valid @RequestBody ChallengeCreateRequest challengeCreateRequest) {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.createChallengeProcedure(memberId, challengeCreateRequest);

        return ApiResponseUtil.success();
    }

}
