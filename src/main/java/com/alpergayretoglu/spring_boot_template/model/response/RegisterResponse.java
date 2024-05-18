package com.alpergayretoglu.spring_boot_template.model.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private final String id;
    private final String token;
}
