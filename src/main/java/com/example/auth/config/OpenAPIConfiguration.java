package com.example.auth.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfiguration {

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components())
//                .tags(List.of(
//                        new Tag()
//                                .name("Authentication")
//                                .description("Login/logout controller")
//                ))
//                .path("/logout", new PathItem()
//                        .post(new Operation()
//                                .tags(List.of("Authentication"))
//                                .summary("Logout")
//                                .description("Logout the current user.")
//                                .operationId("logout")
//                                .responses(new ApiResponses()
//                                        .addApiResponse("200", new ApiResponse().description("OK")))
//                        )
//                );
//    }

}
