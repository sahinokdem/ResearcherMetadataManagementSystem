package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.MetadataValueMapper;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MetadataValueService {

    private final MetadataValueOwnerService metadataValueOwnerService;
    private final UserRoleService userRoleService;
    private final MetadataValueMapper metadataValueMapper;
    private final MetadataRegistryRepository metadataRegistryRepository;
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
        MetadataValue metadataValue = metadataValueOwnerService.getMetadataValue(metadataValueId);
        return metadataValueMapper.toResponse(metadataValue);
    }

    public List<MetadataValueResponse> getAllMetadataValues() {
        List<MetadataValue> metadataValues = metadataValueOwnerService.getAllMetadataValues();
        return metadataValues.stream()
                .map(metadataValueMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<MetadataValueResponse> getMetadataValuesByRegistry(String registryName) {
        List<MetadataValue> metadataValues = metadataValueOwnerService.getAllMetadataValuesByRegistry(registryName);
        return metadataValues.stream()
                .map(metadataValueMapper::toResponse)
                .collect(Collectors.toList());
    }
}