package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.FileAssociation;
import com.sahinokdem.researcher_metadata.enums.Result;
import lombok.Data;

@Data
public class CVResponse {
    private final String id;
    private final String fileId;
    private final String name;
    private final FileAssociation fileAssociation;
    private final Result result;
    private final String reason;
}
