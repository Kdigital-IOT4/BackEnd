package com.baro.domain.user.controller;

import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.repository.DTO.MachineLoginDTO;
import com.baro.domain.user.service.ImgUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class ImgUploadTestController {
    private  final ImgUploadService imgUploadService;
    @PostMapping("/s3/imgUpload")
    public ResponseEntity machine_login_controller(@RequestParam("file") MultipartFile file){
        String return_text = imgUploadService.uploadFile(file);
        if(return_text.equals("fail")){
            log.warn("IMG upload Error");
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
        }else{
            return ResponseEntity.ok(return_text);
        }
    }
}
