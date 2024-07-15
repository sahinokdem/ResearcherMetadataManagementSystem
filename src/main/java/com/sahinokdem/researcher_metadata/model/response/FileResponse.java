package com.sahinokdem.researcher_metadata.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FileResponse {
    private final String id;
    private final String name;
    private final long size;
}
