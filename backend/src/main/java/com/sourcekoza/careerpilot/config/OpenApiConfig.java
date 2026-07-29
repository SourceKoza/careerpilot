package com.sourcekoza.careerpilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI documentation configuration.
 *
 * <p>Provides Swagger UI at /swagger-ui.html and OpenAPI spec at /v3/api-docs.</p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI careerPilotOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CareerPilot AI API")
                        .description("AI-powered Job Search and Application Automation Platform")
                        .version("0.1.0")
                        .contact(new Contact()
                                .name("SourceKoza Labs")
                                .url("https://github.com/sourcekoza/careerpilot-ai"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
