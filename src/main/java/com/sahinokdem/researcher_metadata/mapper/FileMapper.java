package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FileMapper {
    public FileResponse toResponse(final FileInfo fileInfo) {
        return new FileResponse(
                fileInfo.getId(),
                fileInfo.getName(),
                fileInfo.getSize()
        );
    }

    public ResponseEntity<?> toResponseEntity(FileInfo fileInfo, byte[] fileBytesForm) {
        String contentDisposition = "attachment; filename=\"" + fileInfo.getName() + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(fileBytesForm);
    }
}
