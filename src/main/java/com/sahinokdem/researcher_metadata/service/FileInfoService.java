package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileInfoService {
    public FileInfo buildFileInfo(MultipartFile file, String fileLocation) {
        FileInfo fileInfo = FileInfo.builder()
                .name(file.getOriginalFilename())
                .location(fileLocation)
                .size(file.getSize()).build();
        return fileInfo;
    }
}
