package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.MetadataValueMapper;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueService {

    private final UserService userService;
    private final MetadataValueMapper metadataValueMapper;
    private final MetadataValueRepository metadataValueRepository;

    public MetadataValueResponse addMetadataValue(MetadataValueRequest request) {
        userService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataValue metadataValue = metadataValueMapper.toEntity(request);
        metadataValueRepository.save(metadataValue);
        return metadataValueMapper.toResponse(metadataValue);
    }

    public MetadataValueResponse updateMetadataValue(String metadataValueId, MetadataValueRequest request) {
        userService.assertCurrentUserRole(UserRole.EDITOR);
        MetadataValue metadataValue = metadataValueRepository.findById(metadataValueId).orElseThrow(
                () -> BusinessExceptions.METADATA_VALUE_NOT_FOUND);
        MetadataValue updatedMetadataValue = metadataValueMapper.toEntity(request, metadataValue);
        metadataValueRepository.save(updatedMetadataValue);
        return metadataValueMapper.toResponse(updatedMetadataValue);
    }

    public void deleteMetadataValue(String metadataValueId) {
        userService.assertCurrentUserRole(UserRole.EDITOR);
        metadataValueRepository.deleteById(metadataValueId);
    }
}