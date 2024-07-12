package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileLocationService {

    private final String storageDir = "cdn";

    public String createLocation(MultipartFile file) {
        LocalDate currentDate = LocalDate.now();
        String year = String.valueOf(currentDate.getYear());
        String month = String.format("%02d", currentDate.getMonthValue());
        String day = String.format("%02d", currentDate.getDayOfMonth());

        String directoryPath = storageDir + "/" + year + "/" + month + "/" + day + "/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String fileExtension = getExtension(file.getOriginalFilename());
        String fileName = uuid + "." + fileExtension;
        String fileLocation = directoryPath + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(fileLocation), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw BusinessExceptions.FILE_NOT_SAVED;
        }
        return fileLocation;
    }

    private String getExtension(String originalFilename) {
        int lastIndexOf = originalFilename.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return originalFilename.substring(lastIndexOf + 1);
    }
}
