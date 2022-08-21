package com.example.astudy.services.storage;

import com.example.astudy.config.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3ServiceImpl implements AmazonS3Service{
    private final S3AsyncClient s3AsyncClient;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AwsProperties awsProperties;

    @Override
    public void uploadFile( String key, MultipartFile file) throws IOException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3BucketName())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .key(key)
                .build();
        PutObjectResponse response = s3Client.putObject(
                objectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        log.info("DONE UPLOAD FILE");
    }

    @Override
    public void deleteFile(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(awsProperties.getS3BucketName())
                .key(key)
                .build();
        DeleteObjectResponse response = s3Client.deleteObject(request);
        log.info("DONE DELETE FILE");
    }

    @Override
    public String createUrl(String key) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(awsProperties.getS3BucketName())
                        .key(key)
                        .build();
        GetObjectPresignRequest getObjectPresignRequest =  GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest =
                s3Presigner.presignGetObject(getObjectPresignRequest);

        var url = presignedGetObjectRequest.url();
        return url.toString();
    }


}
