package com.rishi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rishi.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String storeFile(MultipartFile file, String folderName) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", folderName)
            );
            return uploadResult.get("secure_url").toString();  // ✅ Return public URL
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public void deleteFile(String filename, String folderName) {
        try {
            // filename = e.g., "profile/image123" (folder/filename)
            cloudinary.uploader().destroy(folderName + "/" + filename, ObjectUtils.emptyMap());
        } catch (IOException e) {
            System.err.println("Cloudinary delete failed: " + e.getMessage());
        }
    }

    @Override
    public String replaceFile(MultipartFile newFile, String existingPath, String folderName) {
        if (existingPath != null && !existingPath.isEmpty()) {
            String[] parts = existingPath.split("/");  // "https://.../profile/image.jpg"
            String fileName = parts[parts.length - 1];
            deleteFile(fileName, folderName);
        }
        return storeFile(newFile, folderName);
    }

    @Override
    public org.springframework.core.io.Resource loadFile(String filename, String folderName) {
        throw new UnsupportedOperationException("🔒 Not needed! Use Cloudinary URLs directly.");
    }
}

