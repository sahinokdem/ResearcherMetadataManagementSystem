package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileContentService {
    public byte[] getFileContent(String fileLocation) {
        try {
            return Files.readAllBytes(new File(fileLocation).toPath());
        } catch (IOException e) {
            throw BusinessExceptions.FILE_READ_ERROR;
        }
    }
}
