package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.sahinokdem.researcher_metadata.service.MetadataRegistryTypeService;
import com.sahinokdem.researcher_metadata.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueMapper {

    private final MetadataRegistryTypeService metadataRegistryTypeService;
    private final UserRoleService userRoleService;
    private final UserRepository userRepository;

    public MetadataValueResponse toResponse(MetadataValue metadataValue) {
        return new MetadataValueResponse(
                metadataValue.getId(),
                metadataValue.getOwner().getId(),
                metadataValue.getMetadataRegistry().getId(),
                metadataValue.getValue()
        );
    }

    public MetadataValue toEntity(MetadataValueCreateRequest request) {
        User owner = userRepository.findById(request.getUserId()).orElseThrow(
                () -> BusinessExceptions.USER_NOT_FOUND);
        userRoleService.assertUserIsResearcher(BusinessExceptions.NON_RESEARCHER_WITH_METADATA, request.getUserId());
        metadataRegistryTypeService.assertValueIsValid(request.getRegistryId(), request.getValue());
        return new MetadataValue(
                owner,
                metadataRegistryTypeService.getMetadataRegistry(request.getRegistryId()),
                request.getValue()
        );
    }

    public MetadataValue toEntity(MetadataValueUpdateRequest request, MetadataValue metadataValue) {
        metadataRegistryTypeService.assertValueIsValid(metadataValue.getMetadataRegistry().getId(),
                request.getValue());
        metadataValue.setValue(request.getValue());
        return metadataValue;
    }
}
