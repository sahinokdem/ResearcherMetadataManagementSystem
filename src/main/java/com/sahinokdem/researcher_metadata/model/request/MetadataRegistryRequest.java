package com.sahinokdem.researcher_metadata.model.request;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class MetadataRegistryRequest {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String type;
}
