package com.example.astudy.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AmazonS3Service {
    void uploadFile(String key, MultipartFile file) throws IOException;
    void deleteFile(String key);
    String createUrl(String key);
}
