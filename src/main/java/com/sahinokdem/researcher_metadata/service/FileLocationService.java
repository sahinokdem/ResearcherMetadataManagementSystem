package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.exception.StorageExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileLocationService {

    private final String storageDir = "C:\\Users\\sahin\\Desktop\\javaCalisma\\cdn";

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

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw StorageExceptions.FILE_READ_ERROR;
        } try {
            Files.copy(inputStream, Paths.get(fileLocation), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw StorageExceptions.FILE_NOT_SAVED;
        }
        return fileLocation;
    }

    private String getExtension(String originalFilename) {
        int lastIndexOf = originalFilename.lastIndexOf(".");
        if (lastIndexOf == -1) {
            throw StorageExceptions.INVALID_FILE_FORMAT_ERROR;
        }
        return originalFilename.substring(lastIndexOf + 1);
    }
}
