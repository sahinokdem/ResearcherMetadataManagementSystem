package com.sahinokdem.researcher_metadata.model.request;

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
