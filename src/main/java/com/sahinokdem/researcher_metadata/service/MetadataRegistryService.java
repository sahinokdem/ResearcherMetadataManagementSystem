package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.MetadataRegistryMapper;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataRegistryService {

    private final UserService userService;
    private final MetadataRegistryMapper metadataRegistryMapper;
    private final MetadataRegistryRepository metadataRegistryRepository;

    public MetadataRegistryResponse addMetadataRegistry(MetadataRegistryRequest request) {
        userService.assertCurrentUserRole(UserRole.EDITOR);
        assertRegistryNameUnique(request);
        MetadataRegistry metadataRegistry = metadataRegistryMapper.toEntity(request);
        metadataRegistryRepository.save(metadataRegistry);
        return metadataRegistryMapper.toResponse(metadataRegistry);
    }

    public MetadataRegistryResponse updateMetadataRegistry(String metadataRegistryId, MetadataRegistryRequest request) {
        userService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataRegistry metadataRegistry = metadataRegistryRepository.findById(metadataRegistryId).orElseThrow(
                ()-> BusinessExceptions.REGISTRY_NOT_FOUND);
        if (!request.getName().equals(metadataRegistry.getName())) assertRegistryNameUnique(request);
        metadataRegistry.setName(request.getName());
        metadataRegistry.setType(metadataRegistryMapper.toType(request));
        metadataRegistryRepository.save(metadataRegistry);
        return metadataRegistryMapper.toResponse(metadataRegistry);
    }

    private void assertRegistryNameUnique(MetadataRegistryRequest request) {
        metadataRegistryRepository.findByName(request.getName()).ifPresent(
                metadataRegistry -> { throw BusinessExceptions.REGISTRY_NAME_ALREADY_EXIST;
                });
    }

    public void deleteMetadataRegistry(String metadataRegistryId) {
        userService.assertCurrentUserRole(UserRole.EDITOR);
        metadataRegistryRepository.deleteById(metadataRegistryId);
    }
}
