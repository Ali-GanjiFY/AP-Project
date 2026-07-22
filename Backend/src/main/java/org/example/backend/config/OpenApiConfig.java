package org.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Represents open api config.
 */
@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    /**
     * Custom open a p i.
     * @return the result
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Makes every endpoint require the scheme by default in Swagger UI
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                // Swagger auto-prefixes "Bearer " for you, just paste raw token
                                .description("Paste only the JWT token returned by /api/auth/login")
                        )
                );
    }
}