package com.alpergayretoglu.spring_boot_template.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty
    @Email
    private final String email;

    @NotEmpty
    private final String password;

}
