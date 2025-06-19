package com.bad.batch.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme()));
    }

    private Info apiInfo() {
        return new Info()
                .title("SkillLink for Developers API")
                .version("1.0")
                .description(String.join("\n",
                        "Plataforma de Aprendizaje Colaborativo para Desarrolladores.",
                        "Funcionalidades Clave:",
                        "- Perfiles técnicos con filtros por tecnologías y experiencia.",
                        "- Mentorías en vivo con streaming y chat en tiempo real.",
                        "- Desafíos de código con sistema de puntuación y rankings.",
                        "- Mensajería entre usuarios.",
                        "- Progreso personalizado para seguir el crecimiento individual."
                ));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Autenticación mediante token JWT. Obtenga el token desde los endpoints de autenticación.");
    }
}