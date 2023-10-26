package com.kkukku.timing.security.configs;

import com.kkukku.timing.oauth2.handlers.OAuth2AuthenticationFailureHandler;
import com.kkukku.timing.oauth2.handlers.OAuth2AuthenticationSuccessHandler;
import com.kkukku.timing.oauth2.services.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    private final String[] WHITE_LIST_URL = {
        "/h2-console",
        "/v2/api-docs",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(
                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2Configurer -> oauth2Configurer
                .successHandler(new OAuth2AuthenticationSuccessHandler())
                .failureHandler(new OAuth2AuthenticationFailureHandler())
                .userInfoEndpoint(
                    userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                        oAuth2UserService)))
            .authorizeHttpRequests(request -> request.requestMatchers(CorsUtils::isPreFlightRequest)
                                                     .permitAll()
                                                     .requestMatchers(WHITE_LIST_URL)
                                                     .permitAll()
                                                     .requestMatchers("/api/v1/ping")
                                                     .permitAll()

                                                     .anyRequest()
                                                     .authenticated());

        return http.build();
    }
}
