package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.sahinokdem.researcher_metadata.service.MetadataRegistryTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueMapper {

    private final UserRepository userRepository;
    private final MetadataRegistryTypeService metadataRegistryTypeService;

    public MetadataValueResponse toResponse(MetadataValue metadataValue) {
        return new MetadataValueResponse(
                metadataValue.getId(),
                metadataValue.getUserId(),
                metadataValue.getMetadataRegistryId(),
                metadataValue.getValue()
        );
    }

    public MetadataValue toEntity(MetadataValueRequest request) {
        assertUserIsResearcher(request.getUserId());
        metadataRegistryTypeService.assertValueIsValid(request.getRegistryId(), request.getValue());
        return new MetadataValue(
                request.getUserId(),
                request.getRegistryId(),
                request.getValue()
        );
    }

    private void assertUserIsResearcher(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
        if (!user.getUserRole().equals(UserRole.RESEARCHER)) throw BusinessExceptions.NON_RESEARCHER_WITH_METADATA;
    }
}
