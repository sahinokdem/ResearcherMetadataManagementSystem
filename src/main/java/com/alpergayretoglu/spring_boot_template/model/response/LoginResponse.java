package com.alpergayretoglu.spring_boot_template.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private final String id;
    private final String token;
}
