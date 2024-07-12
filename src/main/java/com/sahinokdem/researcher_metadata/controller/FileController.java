package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import com.sahinokdem.researcher_metadata.service.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileStorageService storageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        FileResponse response = null;
        try {
            response = storageService.storeFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }
}

