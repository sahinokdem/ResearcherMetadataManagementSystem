package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.repository.FileInfoRepository;
import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import com.sahinokdem.researcher_metadata.util.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
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

}

