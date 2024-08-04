package com.sahinokdem.researcher_metadata.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class MetadataValueUpdateRequest {
    @NotEmpty
    private final String value;

    @JsonCreator
    public MetadataValueUpdateRequest(@JsonProperty String  value) {
        this.value = value;
    }
}
