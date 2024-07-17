package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import lombok.Data;

@Data
public class MetadataRegistryResponse {
    private final String id;
    private final String name;
    private final MetadataRegistryType type;
}
