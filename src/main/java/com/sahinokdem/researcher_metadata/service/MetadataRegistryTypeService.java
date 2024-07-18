package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class MetadataRegistryTypeService {

    public MetadataRegistryType getType(String type) {
        switch (normalizeString(type)) {
            case "STRING":
                return MetadataRegistryType.STRING;
            case "POSITIVEINTEGER":
                return MetadataRegistryType.POSITIVE_INTEGER;
            case "EDUCATIONDEGREE":
                return MetadataRegistryType.EDUCATION_DEGREE;
            case "RESEARCHFIELD":
                return MetadataRegistryType.RESEARCH_FIELD;
        }
        throw BusinessExceptions.INVALID_REGISTRY_TYPE;
    }

    private String normalizeString(String input) {
        return input.replaceAll("[^a-zA-Z]", "").toUpperCase(Locale.ENGLISH);
    }
}
