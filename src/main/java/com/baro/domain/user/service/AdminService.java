package com.baro.domain.user.service;

import com.baro.domain.user.domain.Admin;
import com.baro.domain.user.repository.DTO.AdminRegisterDTO;
import com.baro.domain.user.repository.JPAAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final JPAAdminRepository adminRepository;
    /**
     * todo
     * 1. temp register
     * 2. temp login
     */

    public String admin_register_service(AdminRegisterDTO adminRegisterDTO){
        String return_text;
        if(adminRegisterDTO.isCreateValid()){
            if(! adminRepository.existsById(adminRegisterDTO.getAdminId())){
                //존재하지않음
                //체크완료
                try{
                    adminRepository.save(
                            Admin.builder()
                                    .id(adminRegisterDTO.getAdminId())
                                    .password(adminRegisterDTO.getAdminPassword())
                                    .build()
                    );
                    return_text ="success";
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
                log.warn("admin id 가 이미 존재합니다.");
                return_text = "이미 존재하는 아이디 입니다.";
            }

        }else {
            log.warn("검증값에 문제가 발생하였습니다.");
            return_text = "검증실패 잘못입력하였습니다.";
        }

        return return_text;
    }

}
