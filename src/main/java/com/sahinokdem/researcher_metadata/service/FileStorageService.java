package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.exception.StorageExceptions;
import com.sahinokdem.researcher_metadata.repository.FileInfoRepository;
import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import com.sahinokdem.researcher_metadata.util.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    public FileSystemResource downloadFile(String fileId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> StorageExceptions.FILE_NOT_FOUND);

        String fileLocation = fileInfo.getLocation();
        if (fileLocation == null) throw StorageExceptions.LOCATION_NULL_ERROR;
        File file = new File(fileLocation);
        return new FileSystemResource(file);
    }
}

