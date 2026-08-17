package com.example.vehicledealershipbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Management API")
                        .description("REST API for vehicle and dealer management.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Mateus Lima")
                        )
                );
    }
}
