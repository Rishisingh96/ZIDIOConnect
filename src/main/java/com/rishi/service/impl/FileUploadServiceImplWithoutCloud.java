//package com.rishi.service.impl;
//
//import com.rishi.config.FileStorageProperties;
//import com.rishi.service.FileUploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Service
//public class FileUploadServiceImpl implements FileUploadService {
//
//    @Autowired
//    private FileStorageProperties fileStorageProperties;
//    private Path rootPath;
//
//    public FileUploadServiceImpl(FileStorageProperties props) {
//        this.rootPath = Paths.get(props.getUploadDir()).toAbsolutePath().normalize();
//    }
//
//    @Override
//    public String storeFile(MultipartFile file, String folderName) {
//        try{
//            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path dir = this.rootPath.resolve(folderName);
//            if (!dir.toFile().exists()) {
//                dir.toFile().mkdirs(); // Create the directory if it doesn't exist
//            }
//            Path targetLocation = dir.resolve(filename);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return folderName + "/" + filename;
//        }catch (IOException e){
//            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public Resource loadFile(String filename, String folderName) {
//        try {
//            Path filePath = this.rootPath.resolve(folderName).resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists()) {
//                return resource;
//            }
//            throw new RuntimeException("File not found");
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("File not found", e);
//        }
//    }
//
//    @Override
//    public void deleteFile(String filename, String folderName) {
//        try {
//            Path filePath = this.rootPath.resolve(folderName).resolve(filename).normalize();
//            Files.deleteIfExists(filePath);
//        } catch (IOException e) {
//            // Silent fail if not found
//            System.err.println("Failed to delete file: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String replaceFile(MultipartFile newFile, String existingPath, String folderName) {
//        // Delete old file
//        if (existingPath != null && !existingPath.isEmpty()) {
//            String[] parts = existingPath.split("/", 2);
//            if (parts.length == 2) {
//                String oldFile = parts[1]; // Get filename only
//                deleteFile(oldFile, folderName);
//            }
//        }
//        // Upload new file
//        return storeFile(newFile, folderName);
//    }
//}