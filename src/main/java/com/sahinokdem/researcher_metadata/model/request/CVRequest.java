package com.sahinokdem.researcher_metadata.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class CVRequest {
    @NotEmpty
    private final String cvAssociation;

    @JsonCreator
    public CVRequest(@JsonProperty String cvAssociation) {
        this.cvAssociation = cvAssociation;
    }
}
