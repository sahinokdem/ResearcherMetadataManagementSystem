package com.sahinokdem.researcher_metadata.model.request;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class MetadataValueUpdateRequest {
    @NotEmpty
    private final String value;
}
