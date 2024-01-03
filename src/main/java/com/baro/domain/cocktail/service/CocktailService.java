package com.baro.domain.cocktail.service;

import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.repository.DTO.CockTailUploadDTO;
import com.baro.domain.cocktail.repository.JPACockTailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CocktailService {
    private final JPACockTailRepository cockTailRepository;

    public boolean checkCocktailToName(String en_name){
        return cockTailRepository.existsByName(en_name);
    }

    public Cocktail cocktail_upload_service(String imgURL, CockTailUploadDTO cockTailUploadDTO){
        String return_text;

        try {
            Cocktail savedCocktail = cockTailRepository.save(
                    Cocktail.builder()
                            .name(cockTailUploadDTO.getEN_Name())
                            .krName(cockTailUploadDTO.getKR_Name())
                            .alcohol(cockTailUploadDTO.getAlcohol())
                            .price(cockTailUploadDTO.getPrice())
                            .amount(cockTailUploadDTO.getAmount())
                            .content(cockTailUploadDTO.getContent())
                            .fileURL(imgURL)
                            .build()
            );
            return  savedCocktail;
        }catch (DataIntegrityViolationException e) {
            // 데이터베이스 무결성 제약 조건 위반 - 키 중복  or 조건 위배
            return_text = "사용자의 데이터 제대로 검증되지 않았습니다.";
            log.warn("admin_register_service : {}" , return_text);
        } catch (JpaSystemException e) {
            // JPA 연동 중 문제 발생
            return_text = "데이터베이스 연동 중 오류가 발생";
            log.warn("admin_register_service : {}" , return_text);
        } catch (DataAccessException e) {
            // 데이터 액세스 오류
            return_text = "데이터베이스 액세스 중 오류가 발생";
            log.warn("admin_register_service : {}" , return_text);
        } catch (Exception e) {
            // 다른 모든 예외 처리
            return_text = "알 수 없는 오류가 발생";
            log.warn(e.getMessage());
        }
        return  null;
    }
}
