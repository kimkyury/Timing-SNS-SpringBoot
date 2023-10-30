package com.kkukku.timing.jwt.handlers;

import com.kkukku.timing.response.codes.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.sendError(ErrorCode.TOKEN_ACCESS_DENIED.getStatus(),
            ErrorCode.TOKEN_ACCESS_DENIED.getMessage());
    }
}
