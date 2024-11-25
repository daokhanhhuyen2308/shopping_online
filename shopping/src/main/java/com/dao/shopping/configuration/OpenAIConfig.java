package com.dao.shopping.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Spring Boot",
                        email = "daokhanhhuyen2308@gmail.com",
                        url = "https://spring.io/projects/spring-boot"
                ),

                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Spring Boot",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://spring.io/projects/spring-boot"
                ),

                termsOfService ="Terms of service"
        ),

        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http:/localhost:8081"
                ),

                @Server(
                        description = "Global ENV",
                        url = "https://spring.io/projects/spring-boot"
                )
        }

)

@SecurityScheme(
        name = "BearerAuth",
        description = "JWT Token description",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAIConfig {
}
