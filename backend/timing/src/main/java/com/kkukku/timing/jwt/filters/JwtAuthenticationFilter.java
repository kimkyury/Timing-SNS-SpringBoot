package com.kkukku.timing.jwt.filters;


import com.kkukku.timing.jwt.services.JwtService;
import com.kkukku.timing.redis.services.RedisService;
import com.kkukku.timing.security.services.MemberDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final MemberDetailService memberDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            jwt = authHeader.substring(7);
        }

        if (jwt != null) {
            String email = jwtService.extractUsername(jwt);

            if (email != null && jwtService.isTokenValid(jwt, email)) {
                String isLogout = redisService.getValue("member:" + email + ":LOGOUT");

                if (isLogout == null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        memberDetailService.loadUserByUsername(email), null,
                        AuthorityUtils.NO_AUTHORITIES);

                    SecurityContextHolder.getContext()
                                         .setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
