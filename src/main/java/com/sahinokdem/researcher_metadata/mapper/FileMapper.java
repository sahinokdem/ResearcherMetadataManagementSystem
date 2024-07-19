package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import org.springframework.stereotype.Service;

@Service
public class FileMapper {
    public FileResponse toResponse(FileInfo fileInfo) {
        return new FileResponse(
                fileInfo.getId(),
                fileInfo.getName(),
                fileInfo.getSize()
        );
    }
}
