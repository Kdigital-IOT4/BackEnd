package com.baro.domain.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImgUploadService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file , String folder , String name) {
        try {
            String key = folder + "/" + name;
            String fileUrl = "https://" + bucket +".s3.ap-northeast-2.amazonaws.com"+ "/" + key;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,key,file.getInputStream(),metadata);
            return fileUrl;
        } catch (IOException e) {
            log.info("upload file Error : {}" , e);
            return  "fail";
        }

    }
}
