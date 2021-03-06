package com.projectsbyabd.coronavirusreport.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("coronavirus-report-api")
                .select()
                .apis(RequestHandlerSelectors
                        .withClassAnnotation(RestController.class))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Coronavirus Report API")
                        .version("1.0.0")
                        .description("API Documentation for Coronavirus Report")
                        .build());
    }

    @Bean
    public Docket swaggerDocketAdmin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("coronavirus-report-api-admin")
                .select()
                .apis(RequestHandlerSelectors
                        .withClassAnnotation(RestController.class))
                .paths(PathSelectors.ant("/admin/**"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Coronavirus Report API")
                        .version("1.0.0")
                        .description("API Documentation for Coronavirus Report")
                        .build());
    }
}