package com.example.supportticket.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Support Ticket API",
                version = "v1",
                description = "REST API for creating, retrieving, and updating support tickets"
        )
)
public class SwaggerConfig {
}
