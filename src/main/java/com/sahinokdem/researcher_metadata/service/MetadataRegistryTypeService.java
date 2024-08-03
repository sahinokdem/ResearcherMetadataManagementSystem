package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.EducationDegree;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.enums.ResearchField;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Locale;

@Service
@AllArgsConstructor
public class MetadataRegistryTypeService {

    private final MetadataRegistryRepository metadataRegistryRepository;

    public MetadataRegistry getMetadataRegistry(String metadataRegistryId) {
        return metadataRegistryRepository.findById(metadataRegistryId).orElseThrow(
                () -> BusinessExceptions.REGISTRY_NOT_FOUND);
    }

    public void assertValueIsValid(String metadataRegistryId, String value) {
        MetadataRegistry metadataRegistry = metadataRegistryRepository.findById(metadataRegistryId).orElseThrow(
                () -> BusinessExceptions.REGISTRY_NOT_FOUND);
        MetadataRegistryType metadataRegistryType = metadataRegistry.getType();
        switch (metadataRegistryType) {
            case STRING:
                assertValueIsString(value);
                break;
            case POSITIVE_INTEGER:
                assertValueIsPositiveInteger(value);
                break;
            case EDUCATION_DEGREE:
                assertValueIsEducationDegree(value);
                break;
            case RESEARCH_FIELD:
                assertValueIsResearchField(value);
                break;
            default:
                throw BusinessExceptions.REGISTRY_TYPE_NOT_FOUND;
        }
    }

    private void assertValueIsString(String value) {
        if (value.isBlank()) throw BusinessExceptions.METADATA_VALUE_INVALID;
    }

    private void assertValueIsPositiveInteger(String value) {
        try {
            int valueInt = Integer.parseInt(value);
            if (valueInt <= 0) throw BusinessExceptions.METADATA_VALUE_INVALID;
        } catch (NumberFormatException e) {
            throw BusinessExceptions.METADATA_VALUE_INVALID;
        }
    }

    private void assertValueIsEducationDegree(String value) {
        try {
            EducationDegree.valueOf(value.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            throw BusinessExceptions.METADATA_VALUE_INVALID;
        }
    }

    private void assertValueIsResearchField(String value) {
        try {
            ResearchField.valueOf(value.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            throw BusinessExceptions.METADATA_VALUE_INVALID;
        }
    }
}