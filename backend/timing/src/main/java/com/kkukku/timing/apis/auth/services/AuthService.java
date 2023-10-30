package com.kkukku.timing.apis.auth.services;

import com.kkukku.timing.apis.auth.responses.ReissueResponse;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.jwt.services.JwtService;
import com.kkukku.timing.response.codes.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    public ReissueResponse reissue(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);

        if (!jwtService.isTokenValid(refreshToken, email)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        return new ReissueResponse(jwtService.generateAccessToken(email));
    }
}
