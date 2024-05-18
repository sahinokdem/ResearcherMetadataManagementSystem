package com.alpergayretoglu.spring_boot_template.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SwaggerConfig {
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;
}
