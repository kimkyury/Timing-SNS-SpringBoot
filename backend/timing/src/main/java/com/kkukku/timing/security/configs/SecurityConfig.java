package com.kkukku.timing.security.configs;

import com.kkukku.timing.jwt.filters.JwtAuthenticationFilter;
import com.kkukku.timing.jwt.handlers.JwtAccessDeniedHandler;
import com.kkukku.timing.jwt.handlers.JwtAuthenticationEntryPoint;
import com.kkukku.timing.oauth2.handlers.OAuth2AuthenticationFailureHandler;
import com.kkukku.timing.oauth2.handlers.OAuth2AuthenticationSuccessHandler;
import com.kkukku.timing.oauth2.handlers.OAuth2LogoutHandler;
import com.kkukku.timing.oauth2.services.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2LogoutHandler oAuth2LogoutHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String[] WHITE_LIST_URL = {
        "/api/v1/feeds/*/videos/streaming",
        "/api/v1/auth/reissue",
        "/oauth2/authorization/kakao/**",
        "/h2-console/**",
        "/v2/api-docs",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html",
        "/api/profile",
        "/api/v1/test/ping",
        "/actuator/health"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
            .headers(
                httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                    FrameOptionsConfig::disable))
            .httpBasic(HttpBasicConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(
                configurer -> configurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2Configurer -> oauth2Configurer
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .userInfoEndpoint(
                    userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                        oAuth2UserService)))
            .logout(logout ->
                logout.logoutUrl("/api/v1/auth/logout")
                      .addLogoutHandler(oAuth2LogoutHandler)
                      .logoutSuccessHandler(
                          (request, response, authentication) -> SecurityContextHolder.clearContext()))
            .authorizeHttpRequests(
                request -> request.requestMatchers(CorsUtils::isPreFlightRequest)
                                  .permitAll()
                                  .requestMatchers(WHITE_LIST_URL)
                                  .permitAll()
                                  .anyRequest()
                                  .authenticated())
            .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(
                exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                      .accessDeniedHandler(jwtAccessDeniedHandler));

        return http.build();
    }

}
