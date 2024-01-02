package com.baro.domain.user.service;

import com.baro.domain.user.domain.BasicUser;
import com.baro.domain.user.repository.DTO.BasicUserRegisterDTO;
import com.baro.domain.user.repository.JPAAdminRepository;
import com.baro.domain.user.repository.JPABasicUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicUserService {
    private final JPABasicUserRepository basicUserRepository;

    public String basicUser_register_service(BasicUserRegisterDTO basicUserRegisterDTO){
        log.info("basicUSer_register_service start");
        String return_text;

        if(basicUserRegisterDTO.isCreateValid()){
            if(! basicUserRepository.existsByPhoneNumber(basicUserRegisterDTO.getBasicUserPhoneNumber())){
                //존재하지 않음
                try {
                    basicUserRepository.save(
                            BasicUser.builder()
                                    .phoneNumber(basicUserRegisterDTO.getBasicUserPhoneNumber())
                                    .password(basicUserRegisterDTO.getBasicUserPassword())
                                    .build()

                    );
                    return_text = "success";
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
            }else{
                //존재함
                log.warn("전화번호가 이미 등록되어있습니다.");
                return_text = "해당 전화번호는 이미 등록되어있습니다.";
            }

        }else{
            log.warn("잘못된 검증값");
            return_text="감증 오류 , 잘못 입력하였습니다.";
        }

        return return_text;
    }
}
