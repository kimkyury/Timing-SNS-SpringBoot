package com.kkukku.timing.oauth2.handlers;

import com.kkukku.timing.jwt.services.JwtService;
import com.kkukku.timing.redis.services.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LogoutHandler implements LogoutHandler {

    private final RedisService redisService;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);

        redisService.deleteValue("member:" + email + ":RT");
        redisService.saveDataWithTimeout("member:" + email + "LOGOUT", new Date().toString(),
            jwtService.getAccessTokenExpiration());

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");

                response.addCookie(cookie);
            }
        }
    }
}
