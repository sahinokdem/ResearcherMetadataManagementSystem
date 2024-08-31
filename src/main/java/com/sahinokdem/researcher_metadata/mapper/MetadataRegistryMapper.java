package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Locale;

@Service
@AllArgsConstructor
public class MetadataRegistryMapper {

    private final MetadataRegistryRepository metadataRegistryRepository;

    public MetadataRegistryResponse toResponse(MetadataRegistry metadataRegistry) {
        return new MetadataRegistryResponse(
                metadataRegistry.getId(),
                metadataRegistry.getName(),
                metadataRegistry.getType()
        );
    }

    public MetadataRegistry toEntity(MetadataRegistryCreateRequest request) {
        assertRegistryNameUnique(request.getName());
        return new MetadataRegistry(
                request.getName(),
                toType(request.getType()),
                new ArrayList<>()
        );
    }

    public MetadataRegistry toEntity(MetadataRegistryUpdateRequest request, MetadataRegistry metadataRegistry) {
        if (!request.getName().equals(metadataRegistry.getName())) assertRegistryNameUnique(request.getName());
        metadataRegistry.setName(request.getName());
        return metadataRegistry;
    }

    public MetadataRegistryType toType(String type) {
        String normalizedType = type.replaceAll("\\s+", "_").toUpperCase(Locale.ENGLISH);
        try {
            return MetadataRegistryType.valueOf(normalizedType);
        } catch (IllegalArgumentException e) {
            throw BusinessExceptions.INVALID_REGISTRY_TYPE;
        }
    }

    private void assertRegistryNameUnique(String name) {
        metadataRegistryRepository.findByName(name).ifPresent(
                metadataRegistry -> { throw BusinessExceptions.REGISTRY_NAME_ALREADY_EXIST;
                });
    }
}
