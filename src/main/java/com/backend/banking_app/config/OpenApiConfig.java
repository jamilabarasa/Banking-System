package com.backend.banking_app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> openApi.getComponents()
                .addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
    }
}