package com.kkukku.timing.apis.auth.controllers;

import com.kkukku.timing.apis.auth.responses.ReissueResponse;
import com.kkukku.timing.apis.auth.services.AuthService;
import com.kkukku.timing.response.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Auth", description = "Auth API")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "AccessToken 재발급", tags = {"1. Auth"},
        description = "Challenge가 생성 && 새로운 hashTag 생성 && 연관 관계 정보 생성 ")
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(
        @CookieValue("refresh-token") String refreshToken) {
        return ApiResponseUtil.success(authService.reissue(refreshToken));
    }
}
