package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.enums.UserRole;
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
}