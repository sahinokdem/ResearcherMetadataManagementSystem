package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class MetadataRegistryTypeService {

    private final MetadataRegistryRepository metadataRegistryRepository;

    public void assertValueIsValid(String metadataRegistryId, String value) {
        MetadataRegistryType metadataRegistryType = getType(metadataRegistryId);
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

    private MetadataRegistryType getType(String metadataRegistryId) {
        MetadataRegistry metadataRegistry = metadataRegistryRepository.findById(metadataRegistryId).orElseThrow(
                () -> BusinessExceptions.REGISTRY_NOT_FOUND
        );
        return metadataRegistry.getType();
    }

    private void assertValueIsString(String value) {
        if (value.matches(".*\\d.*")) throw BusinessExceptions.METADATA_VALUE_INVALID;
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
        Set<String> validDegrees = new HashSet<>(Arrays.asList
                ("HIGH_SCHOOL", "ASSOCIATE", "BACHELOR", "MASTER", "DOCTORATE", "POSTDOCTORAL"));
        if (!validDegrees.contains(value.toUpperCase())) {
            throw BusinessExceptions.METADATA_VALUE_INVALID;
        }
    }

    private void assertValueIsResearchField(String value) {
        Set<String> validFields = new HashSet<>(Arrays.asList(
                "COMPUTER SCIENCE", "PHYSICS", "CHEMISTRY", "BIOLOGY",
                "MATHEMATICS", "PSYCHOLOGY", "SOCIOLOGY", "ECONOMICS", "HISTORY", "LITERATURE"
        ));
        if (!validFields.contains(value.toUpperCase())) {
            throw BusinessExceptions.METADATA_VALUE_INVALID;
        }
    }
}