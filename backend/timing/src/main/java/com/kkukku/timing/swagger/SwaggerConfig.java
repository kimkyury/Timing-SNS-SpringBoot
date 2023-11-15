package com.kkukku.timing.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Value("${application.security.cors.back-domain}")
    private String SERVER_URL;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(Arrays.asList(securityRequirement))
            .info(apiInfo())
            .servers(Collections.singletonList(
                new Server().url(SERVER_URL)
                            .description("Publish")));

    }

    private Info apiInfo() {
        return new Info()
            .title("Timing Swagger")
            .description("Here is Timing Swagger Zone")
            .version("1.0.0");
    }
}