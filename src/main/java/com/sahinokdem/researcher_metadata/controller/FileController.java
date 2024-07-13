package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.response.FileResponse;
import com.sahinokdem.researcher_metadata.service.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileStorageService storageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return storageService.storeFile(file);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String fileId) {
        FileSystemResource file = storageService.downloadFile(fileId);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}

