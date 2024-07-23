package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.sahinokdem.researcher_metadata.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueMapper {

    private final UserService userService;
    private final UserRepository userRepository;

    public MetadataValueResponse toResponse(MetadataValue metadataValue) {
        return new MetadataValueResponse(
                metadataValue.getId(),
                metadataValue.getUserId(),
                metadataValue.getMetadataRegistryId(),
                metadataValue.getValue()
        );
    }

    public MetadataValue toEntity(MetadataValueRequest request) {
        assertUserExists(request.getUserId());
        return new MetadataValue(
                request.getUserId(),
                request.getRegistryId(),
                request.getValue()
        );
    }

    private void assertUserExists(String userId) {
        userRepository.findById(userId)
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
    }
}
