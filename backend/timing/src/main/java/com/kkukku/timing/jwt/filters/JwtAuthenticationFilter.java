package com.kkukku.timing.jwt.filters;


import com.kkukku.timing.jwt.services.JwtService;
import com.kkukku.timing.redis.services.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);

        String isLogout = redisService.getValue("member:" + email + ":LOGOUT");

        if (Optional.ofNullable(isLogout)
                    .isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Jwt token unauthorized. Check login status!");
            return;
        }

        // Todo JWT 맞으면 처리하기!

        filterChain.doFilter(request, response);
    }
}
