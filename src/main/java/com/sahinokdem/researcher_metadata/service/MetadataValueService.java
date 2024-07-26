package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.MetadataValueMapper;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueService {

    private final AuthenticationService authenticationService;
    private final UserRoleService userRoleService;
    private final MetadataValueMapper metadataValueMapper;
    private final MetadataValueRepository metadataValueRepository;
    private final UserRepository userRepository;


    public MetadataValueResponse addMetadataValue(MetadataValueRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        User owner = userRepository
                .findById(request.getUserId())
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
        MetadataValue metadataValue = metadataValueMapper.toEntity(owner, request);
        metadataValueRepository.save(metadataValue);
        return metadataValueMapper.toResponse(metadataValue);
    }

    public MetadataValueResponse updateMetadataValue(String metadataValueId, MetadataValueRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataValue metadataValue = metadataValueRepository.findById(metadataValueId).orElseThrow(
                () -> BusinessExceptions.METADATA_VALUE_NOT_FOUND);
        User owner = userRepository
                .findById(request.getUserId())
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
        MetadataValue updatedMetadataValue = metadataValueMapper.toEntity(owner, request, metadataValue);
        metadataValueRepository.save(updatedMetadataValue);
        return metadataValueMapper.toResponse(updatedMetadataValue);
    }

    public void deleteMetadataValue(String metadataValueId) {
        userRoleService.assertCurrentUserRole(UserRole.EDITOR);
        metadataValueRepository.deleteById(metadataValueId);
    }

    public MetadataValueResponse getMetadataValue(String metadataValueId) {
        MetadataValue metadataValue = getMetadataValueEntity(metadataValueId);
        return metadataValueMapper.toResponse(metadataValue);
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