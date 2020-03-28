package com.safaricom.microservice.he.mssdpheaderdecryptor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.TRANSACTION_TYPE;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String SERVICE_DESCRIPTION = "Enables user to opt in to Device Financing service.";
    private final BuildProperties buildProperties;

    @Autowired
    public SwaggerConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    /**
     * Setup custom configurations for swagger Docket.
     *
     * @return Docker instance builder that provides custom configuration of Swagger documentation.
     */
    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.safaricom.microservices"))
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag(TRANSACTION_TYPE, SERVICE_DESCRIPTION));
    }

    /**
     * Setup custom Micro-service details to swagger
     *
     * @return ApiInfo object containing custom information about the API
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Opt-in Service")
                .description(SERVICE_DESCRIPTION)
                .version(buildProperties.getVersion())
                .contact(new Contact("Derrick Dickens Otina", "", "dotina@safaricom.co.ke"))
                .build();
    }
}
