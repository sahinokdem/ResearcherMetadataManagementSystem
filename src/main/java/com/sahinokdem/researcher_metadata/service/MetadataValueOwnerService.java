package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MetadataValueOwnerService {

    private final UserRoleService userRoleService;
    private final AuthenticationService authenticationService;
    private final MetadataValueRepository metadataValueRepository;
    private final MetadataRegistryRepository metadataRegistryRepository;

    public MetadataValue getMetadataValue(String metadataValueId) {
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

    public List<MetadataValue> getAllMetadataValues() {
        if (userRoleService.checkCurrentUserRole(UserRole.RESEARCHER)) {
            User owner = authenticationService.getAuthenticatedUser();
            return metadataValueRepository.findAllByOwner(owner);
        } else if (userRoleService.checkCurrentUserRole(UserRole.EDITOR)) {
            return metadataValueRepository.findAll();
        } else {
            throw BusinessExceptions.AUTHORIZATION_MISSING;
        }
    }

    public List<MetadataValue> getAllMetadataValuesByRegistry(String registryName) {
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
}
