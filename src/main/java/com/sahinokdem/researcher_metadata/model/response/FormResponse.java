package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.Result;
import lombok.Data;

@Data
public class FormResponse {
    private final String id;
    private final Result result;
    private final String reason;
}
