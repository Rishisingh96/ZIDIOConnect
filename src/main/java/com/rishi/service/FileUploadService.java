package com.rishi.service;

public interface FileUploadService {
    String uploadFile(byte[] fileData, String fileName);
    byte[] downloadFile(String fileName);
    void deleteFile(String fileName);
} 