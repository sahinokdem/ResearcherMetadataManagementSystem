package com.sahinokdem.researcher_metadata.model.request;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class MetadataValueRequest {
    @NotEmpty
    private final String userId;
    @NotEmpty
    private final String registryId;
    @NotEmpty
    private final String value;
}