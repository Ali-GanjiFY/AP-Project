package org.example.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // POST /api/uploads/images  (multipart/form-data, key = "files")
    @PostMapping("/images")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> savedUrls = new ArrayList<>();
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String originalName = StringUtils.cleanPath(
                        file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
                String extension = "";
                int dotIndex = originalName.lastIndexOf('.');
                if (dotIndex >= 0) {
                    extension = originalName.substring(dotIndex);
                }

                String newFileName = UUID.randomUUID() + extension;
                Path targetPath = uploadPath.resolve(newFileName);
                Files.copy(file.getInputStream(), targetPath);

                savedUrls.add("/uploads/" + newFileName);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}