package com.rishi.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String storeFile(MultipartFile file, String folderName);
    Resource loadFile(String filename, String folderName);
    void deleteFile(String filename, String folderName);

    // Add this new method:
    String replaceFile(MultipartFile newFile, String existingPath, String folderName);
} 