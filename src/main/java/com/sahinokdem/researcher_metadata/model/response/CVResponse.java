package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import lombok.Data;

@Data
public class CVResponse {
    private final String id;
    private final String fileId;
    private final String name;
    private final String cvAssociation;
    private final FormAndCvResult result;
}
