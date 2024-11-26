package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.MetadataRegistryMapper;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MetadataRegistryService {

    private final UserRoleService userRoleService;
    private final MetadataRegistryMapper metadataRegistryMapper;
    private final MetadataRegistryRepository metadataRegistryRepository;


    public MetadataRegistryResponse addMetadataRegistry(MetadataRegistryCreateRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataRegistry metadataRegistry = metadataRegistryMapper.toEntity(request);
        metadataRegistryRepository.save(metadataRegistry);
        return metadataRegistryMapper.toResponse(metadataRegistry);
    }

    public MetadataRegistryResponse updateMetadataRegistry(String metadataRegistryId, MetadataRegistryUpdateRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataRegistry metadataRegistry = metadataRegistryRepository.findById(metadataRegistryId).orElseThrow(
                ()-> BusinessExceptions.REGISTRY_NOT_FOUND);
        MetadataRegistry updatedMetadataRegistry = metadataRegistryMapper.toEntity(request, metadataRegistry);
        metadataRegistryRepository.save(updatedMetadataRegistry);
        return metadataRegistryMapper.toResponse(metadataRegistry);
    }

    public void deleteMetadataRegistry(String metadataRegistryId) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        metadataRegistryRepository.deleteById(metadataRegistryId);
    }

    public MetadataRegistryResponse getMetadataRegistry(String metadataRegistryId) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataRegistry metadataRegistry = metadataRegistryRepository.findById(metadataRegistryId).orElseThrow(
                () -> BusinessExceptions.REGISTRY_NOT_FOUND);
        return metadataRegistryMapper.toResponse(metadataRegistry);
    }

    public List<MetadataRegistryResponse> getAllMetadataRegistries(Pageable pageable) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        Page<MetadataRegistry> metadataRegistries = metadataRegistryRepository.findAll(pageable);
        return metadataRegistries.getContent().stream()
                .map(metadataRegistryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
