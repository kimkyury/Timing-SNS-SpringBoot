package com.kkukku.timing.apis.auth.controllers;

import com.kkukku.timing.apis.auth.responses.ReissueResponse;
import com.kkukku.timing.apis.auth.services.AuthService;
import com.kkukku.timing.response.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(
        @CookieValue("refresh-token") String refreshToken) {
        return ApiResponseUtil.success(authService.reissue(refreshToken));
    }
}
