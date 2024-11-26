package com.sahinokdem.researcher_metadata.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class MetadataRegistryUpdateRequest {
    @NotEmpty
    private final String name;

    @JsonCreator
    public MetadataRegistryUpdateRequest(@JsonProperty String name) {
        this.name = name;
    }
}
