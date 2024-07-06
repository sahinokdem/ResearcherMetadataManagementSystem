package com.sahinokdem.researcher_metadata.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
@Getter
@Setter
public class SecurityConfig {
    @Value("${app.jwtSecret}")
    private String jwtSecret;
}
