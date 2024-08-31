package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.Result;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FormResponse {
    private final String id;
    private final String nameAndSurname;
    private final String email;
    private final LocalDate dateOfBirth;
    private final String externalApiId;
    private final Result result;
    private final String reason;
}
