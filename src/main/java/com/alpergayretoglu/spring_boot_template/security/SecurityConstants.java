package com.alpergayretoglu.spring_boot_template.security;

public class SecurityConstants {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "spring-boot-template";
    public static final String TOKEN_AUDIENCE = "spring-boot-template";

    private SecurityConstants() {
    }
}
