package com.sahinokdem.researcher_metadata.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class FormRequest {
    @NotEmpty
    private final String nameAndSurname;

    @NotEmpty
    @Email
    private final String email;

    @NotEmpty
    private final Date dateOfBirth;

    private final String externalApiId;
}
