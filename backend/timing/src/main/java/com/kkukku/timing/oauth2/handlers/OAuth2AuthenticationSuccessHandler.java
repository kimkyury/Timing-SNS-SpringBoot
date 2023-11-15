package com.kkukku.timing.oauth2.handlers;

import com.kkukku.timing.jwt.services.JwtService;
import com.kkukku.timing.redis.services.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${application.security.cors.front-domain}")
    private String BASE_URL;

    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String email = ((Map<String, Object>) defaultOAuth2User.getAttributes()
                                                               .get("kakao_account")).get("email")
                                                                                     .toString();

        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);

        String referer = request.getHeader("referer");
        String baseURL = referer == null ? "" : referer;

        String redirectURI = UriComponentsBuilder.fromUriString(baseURL)
//        String redirectURI = UriComponentsBuilder.fromUriString(BASE_URL)
                                                 .path("/login/oauth2/redirect/kakao")
                                                 .queryParam("access-token", accessToken)
                                                 .build()
                                                 .toString();

        Cookie cookie = new Cookie("refresh-token", refreshToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtService.getRefreshTokenExpiration()
                                   .intValue());

        response.addCookie(cookie);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        redisService.saveDataWithTimeout("member:" + email + ":RT", refreshToken,
            jwtService.getRefreshTokenExpiration());

        response.sendRedirect(redirectURI);
    }

}
