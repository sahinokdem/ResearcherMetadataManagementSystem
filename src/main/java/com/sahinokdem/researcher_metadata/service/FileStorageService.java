package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
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

    public FileResponse storeFile(MultipartFile file) throws IOException {
        String fileLocation = fileLocationService.createLocation(file);
        FileInfo fileInfo = fileInfoRepository.save(FileInfo.builder()
                .name(file.getOriginalFilename())
                .location(fileLocation)
                .size(FileUtils.compressFile(file.getBytes())).build());

        return new FileResponse(
                fileInfo.getId(),
                fileInfo.getName(),
                fileInfo.getSize()
        );
    }

    public FileSystemResource downloadFile(String fileId) {
        FileInfo fileInfo = null;
        try {
            fileInfo = fileInfoRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException(fileId));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String filePath = fileInfo.getLocation();
        File file = new File(filePath);
        return new FileSystemResource(file);
    }
}

