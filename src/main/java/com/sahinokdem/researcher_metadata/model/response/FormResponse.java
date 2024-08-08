package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import lombok.Data;

@Data
public class FormResponse {
    private final String id;
    private final FormAndCvResult result;
    private final String reason;
}
