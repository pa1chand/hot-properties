package edu.depaul.hot_properties.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    // This should match your application.properties setting:
    private final String uploadDir = "uploads";

    public String saveImage(MultipartFile file) throws IOException {
        // Create uploads folder if it doesn't exist
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate a unique filename to avoid collisions
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Define the path to save the file
        Path filePath = Paths.get(uploadDir, uniqueFilename);

        // Save the file to disk
        Files.write(filePath, file.getBytes());

        // Return the filename (so it can be stored in the DB)
        return uniqueFilename;
    }
}
