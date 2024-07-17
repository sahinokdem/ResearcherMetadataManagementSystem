package com.sahinokdem.researcher_metadata.util;

import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MetadataRegistryType {

    private static final Map<String, MetadataRegistryType> TYPES = new HashMap<>();

    public static final MetadataRegistryType STRING = new MetadataRegistryType("STRING");
    public static final MetadataRegistryType POSITIVE_INT = new MetadataRegistryType("POSITIVE_INT");

    private final String type;

    private MetadataRegistryType(String type) {
        this.type = type;
        TYPES.put(type, this);
    }

    public static MetadataRegistryType getSpecificType(String type) {
        if (!TYPES.containsKey(type)) throw BusinessExceptions.INVALID_REGISTRY_TYPE;
        return TYPES.get(type);
    }

    public static MetadataRegistryType addType(@NonNull String type) {
        if (TYPES.containsKey(type)) {
            throw BusinessExceptions.REGISTRY_TYPE_ALREADY_EXIST;
        }
        return new MetadataRegistryType(type);
    }
}

