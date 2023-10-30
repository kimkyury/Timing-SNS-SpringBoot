package com.kkukku.timing.jwt.handlers;

import com.kkukku.timing.response.codes.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        response.setCharacterEncoding("utf-8");
        response.sendError(ErrorCode.TOKEN_UNAUTHORIZED.getStatus(),
            ErrorCode.TOKEN_UNAUTHORIZED.getMessage());
    }
}
