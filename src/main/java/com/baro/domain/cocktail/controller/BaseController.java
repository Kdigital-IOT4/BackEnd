package com.baro.domain.cocktail.controller;

import com.baro.domain.cocktail.repository.DTO.BaseUploadDTO;
import com.baro.domain.cocktail.service.BaseService;
import com.baro.domain.user.service.ImgUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/base")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class BaseController {

    private final BaseService baseService;
    private final ImgUploadService imgUploadService;
    @PostMapping("/upload")
    public ResponseEntity base_upload_controller(@RequestParam("image") MultipartFile image, @ModelAttribute BaseUploadDTO baseUploadDTO){
        log.info("EN_Name: {}", baseUploadDTO.getEN_Name());
        log.info("KR_Name: {}", baseUploadDTO.getKR_Name());
        log.info("Price: {}", baseUploadDTO.getPrice());
        log.info("Amount: {}", baseUploadDTO.getAmount());
        log.info("Alcohol: {}", baseUploadDTO.getAlcohol());

        String return_text;
        /**
         * en check ->
         * img upload
         * success -> db store
         */
        if(baseService.checkBase(baseUploadDTO.getEN_Name())){
            //존재함
            return_text ="이미 존재하는 베이스입니다. 수정을 원하시면 수정요청을 이용하시길 바랍니다.";
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(return_text);
        }else{
            //존재 하지않음
            log.info("이미지 업로드 시작...");
            String img_return_text = imgUploadService.uploadFile(image , "base" , baseUploadDTO.getEN_Name());
            if(img_return_text.equals("fail")){
                log.warn("이미지 업로드 실패..");
                return_text ="이미지 업로드에 실패하였습니다. 관리자에게 연락바랍니다.";
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
            }else{
                log.info("이미지 업로드 성공 ...\nDB 저장을 시작합니다.");
                return_text = baseService.base_upload_service(img_return_text , baseUploadDTO);
                if(return_text.equals("success")){
                    log.info("DB 저장 성공...");
                    return ResponseEntity.ok(return_text);
                }else{
                    log.info("DB 저장 실패...");
                    return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
                }
            }
        }
    }
}
