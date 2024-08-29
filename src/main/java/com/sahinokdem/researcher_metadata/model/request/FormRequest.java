package com.sahinokdem.researcher_metadata.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class FormRequest {
    @NotEmpty
    private final String nameAndSurname;

    @NotEmpty
    @Email
    private final String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    private final LocalDate dateOfBirth;

    private final String externalApiId;
}
