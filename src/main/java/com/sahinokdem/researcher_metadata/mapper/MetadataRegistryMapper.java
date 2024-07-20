package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MetadataRegistryMapper {
    public MetadataRegistryResponse toResponse(MetadataRegistry metadataRegistry) {
        return new MetadataRegistryResponse(
                metadataRegistry.getId(),
                metadataRegistry.getName(),
                metadataRegistry.getType()
        );
    }

    public MetadataRegistry toEntity(MetadataRegistryRequest request) {
        return new MetadataRegistry(
                request.getName(),
                toType(request.getType())
        );
    }

    public MetadataRegistryType toType(String type) {
        String normalizedType = type.replaceAll("\\s+", "_").toUpperCase(Locale.ENGLISH);
        try {
            return MetadataRegistryType.valueOf(normalizedType);
        } catch (IllegalArgumentException e) {
            throw BusinessExceptions.INVALID_REGISTRY_TYPE;
        }
    }
}
