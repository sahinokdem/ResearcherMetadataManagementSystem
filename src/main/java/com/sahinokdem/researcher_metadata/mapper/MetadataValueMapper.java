package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import org.springframework.stereotype.Service;

@Service
public class MetadataValueMapper {
    public MetadataValueResponse toResponse(MetadataValue metadataValue) {
        return new MetadataValueResponse(
                metadataValue.getId(),
                metadataValue.getUserId(),
                metadataValue.getMetadataRegistryId(),
                metadataValue.getValue()
        );
    }
}
