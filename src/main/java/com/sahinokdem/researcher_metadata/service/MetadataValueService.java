package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.MetadataValueMapper;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MetadataValueService {

    private final UserRoleService userRoleService;
    private final AuthenticationService authenticationService;
    private final MetadataValueMapper metadataValueMapper;
    private final MetadataValueRepository metadataValueRepository;
    private final MetadataRegistryRepository metadataRegistryRepository;

    public List<MetadataValueResponse> getAllMetadataValues() {
        List<MetadataValue> metadataValues = getAllMetadataValueEntities();
        return metadataValues.stream()
                .map(metadataValueMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<MetadataValueResponse> getMetadataValuesByRegistry(String registryName) {
        List<MetadataValue> metadataValues = getAllMetadataValueEntitiesByRegistry(registryName);
        return metadataValues.stream()
                .map(metadataValueMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MetadataValueResponse getMetadataValue(String metadataValueId) {
        MetadataValue metadataValue = getMetadataValueEntity(metadataValueId);
        return metadataValueMapper.toResponse(metadataValue);
    }

    public MetadataValueResponse addMetadataValue(MetadataValueCreateRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataValue metadataValue = metadataValueMapper.toEntity(request);
        metadataValueRepository.save(metadataValue);
        return metadataValueMapper.toResponse(metadataValue);
    }

    public MetadataValueResponse updateMetadataValue(String metadataValueId, MetadataValueUpdateRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataValue metadataValue = metadataValueRepository.findById(metadataValueId).orElseThrow(
                () -> BusinessExceptions.METADATA_VALUE_NOT_FOUND);
        MetadataValue updatedMetadataValue = metadataValueMapper.toEntity(request, metadataValue);
        metadataValueRepository.save(updatedMetadataValue);
        return metadataValueMapper.toResponse(updatedMetadataValue);
    }

    public void deleteMetadataValue(String metadataValueId) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        metadataValueRepository.deleteById(metadataValueId);
    }

    private List<MetadataValue> getAllMetadataValueEntities() {
        if (userRoleService.checkCurrentUserRole(UserRole.RESEARCHER)) {
            User owner = authenticationService.getAuthenticatedUser();
            return metadataValueRepository.findAllByOwner(owner);
        } else if (userRoleService.checkCurrentUserRole(UserRole.EDITOR)) {
            return metadataValueRepository.findAll();
        } else {
            throw BusinessExceptions.AUTHORIZATION_MISSING;
        }
    }

    private List<MetadataValue> getAllMetadataValueEntitiesByRegistry(String registryName) {
        MetadataRegistry metadataRegistry = metadataRegistryRepository.findByName(registryName)
                .orElseThrow(() -> BusinessExceptions.REGISTRY_NOT_FOUND);
        if (userRoleService.checkCurrentUserRole(UserRole.RESEARCHER)) {
            User owner = authenticationService.getAuthenticatedUser();
            return metadataValueRepository.findAllByOwnerAndMetadataRegistry(owner, metadataRegistry);
        } else if (userRoleService.checkCurrentUserRole(UserRole.EDITOR)) {
            return metadataValueRepository.findAllByMetadataRegistry(metadataRegistry);
        } else {
            throw BusinessExceptions.AUTHORIZATION_MISSING;
        }
    }

    private MetadataValue getMetadataValueEntity(String metadataValueId) {
        if (userRoleService.checkCurrentUserRole(UserRole.RESEARCHER)) {
            User owner = authenticationService.getAuthenticatedUser();
            return metadataValueRepository.findByOwnerAndId(owner, metadataValueId)
                    .orElseThrow(() -> BusinessExceptions.METADATA_VALUE_NOT_FOUND);
        } else if (userRoleService.checkCurrentUserRole(UserRole.EDITOR)) {
            return metadataValueRepository.findById(metadataValueId)
                    .orElseThrow(() -> BusinessExceptions.METADATA_VALUE_NOT_FOUND);
        } else {
            throw BusinessExceptions.AUTHORIZATION_MISSING;
        }
    }
}