package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.FileMapper;
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
public class FileService {

    private final FileInfoRepository fileInfoRepository;
    private final FileStorageService fileStorageService;
    private final FileInfoService fileInfoService;
    private final FileMapper fileMapper;

    public FileResponse storeFile(MultipartFile file) {
        String fileLocation = fileStorageService.createLocation(file);
        FileInfo fileInfo = fileInfoRepository.save(fileInfoService.buildFileInfo(file, fileLocation));
        return fileMapper.toResponse(fileInfo);
    }

    public ResponseEntity<?> downloadFile(String fileId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> BusinessExceptions.FILE_NOT_FOUND);
        String fileLocation = fileInfo.getLocation();
        byte[] fileContent = fileStorageService.getFileContent(fileLocation);
        String contentDisposition = fileStorageService.createContentDisposition(fileInfo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(fileContent);
    }
}