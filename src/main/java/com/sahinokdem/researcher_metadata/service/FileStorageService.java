package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.exception.StorageExceptions;
import com.sahinokdem.researcher_metadata.repository.FileInfoRepository;
import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@AllArgsConstructor
public class FileStorageService {

    private final FileInfoRepository fileInfoRepository;
    private final FileLocationService fileLocationService;

    public FileResponse storeFile(MultipartFile file) {
        String fileLocation = fileLocationService.createLocation(file);
        if (fileLocation == null) throw StorageExceptions.LOCATION_NULL_ERROR;

        FileInfo fileInfo = fileInfoRepository.save(FileInfo.builder()
                .name(file.getOriginalFilename())
                .location(fileLocation)
                .size(file.getSize()).build());
        return new FileResponse(
                fileInfo.getId(),
                fileInfo.getName(),
                fileInfo.getSize()
        );
    }

    public ResponseEntity<?> downloadFile(String fileId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> StorageExceptions.FILE_NOT_FOUND);
        String fileLocation = fileInfo.getLocation();
        byte[] fileBytesForm = null;
        try {
            fileBytesForm = Files.readAllBytes(new File(fileLocation).toPath());
        } catch (IOException e) {
            throw StorageExceptions.FILE_READ_ERROR;
        }
        String contentDisposition = "attachment; filename=\"" + fileInfo.getName() + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(fileBytesForm);
    }
}