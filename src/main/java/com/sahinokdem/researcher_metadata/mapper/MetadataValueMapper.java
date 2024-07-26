package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.sahinokdem.researcher_metadata.service.MetadataRegistryTypeService;
import com.sahinokdem.researcher_metadata.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueMapper {

    private final UserRepository userRepository;
    private final MetadataRegistryTypeService metadataRegistryTypeService;
    private final UserRoleService userRoleService;

    public MetadataValueResponse toResponse(MetadataValue metadataValue) {
        return new MetadataValueResponse(
                metadataValue.getId(),
                metadataValue.getOwner().getId(),
                metadataValue.getMetadataRegistry().getId(),
                metadataValue.getValue()
        );
    }

    public MetadataValue toEntity(User owner, MetadataValueRequest request) {
        assertUserIsResearcher(request.getUserId());
        metadataRegistryTypeService.assertValueIsValid(request.getRegistryId(), request.getValue());
        return new MetadataValue(
                owner,
                metadataRegistryTypeService.getMetadataRegistry(request.getRegistryId()),
                request.getValue()
        );
    }

    public MetadataValue toEntity(User owner, MetadataValueRequest request, MetadataValue metadataValue) {
        assertUserIsResearcher(request.getUserId());
        metadataRegistryTypeService.assertValueIsValid(request.getRegistryId(), request.getValue());
        metadataValue.setOwner(owner);
        metadataValue.setMetadataRegistry(
                metadataRegistryTypeService.getMetadataRegistry(request.getRegistryId()));
        metadataValue.setValue(request.getValue());
        return metadataValue;
    }

    private void assertUserIsResearcher(String userId) {
        try {
            userRoleService.checkSpecificUserRole(userId, UserRole.RESEARCHER);
        } catch (BusinessException authorizationMissingException) {
            throw BusinessExceptions.NON_RESEARCHER_WITH_METADATA;
        }
    }
}
