package com.rishi.service.impl;

import com.rishi.service.FileUploadService;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public String uploadFile(byte[] fileData, String fileName) {
        // TODO: Implement file upload logic
        return "uploaded/path/" + fileName;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        // TODO: Implement file download logic
        return new byte[0];
    }

    @Override
    public void deleteFile(String fileName) {
        // TODO: Implement file delete logic
    }
} 