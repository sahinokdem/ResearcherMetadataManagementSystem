package com.sahinokdem.researcher_metadata.model.response;

import lombok.Data;

@Data
public class MetadataValueResponse {
    private final String id;
    private final String userId;
    private final String registryId;
    private final String value;
}