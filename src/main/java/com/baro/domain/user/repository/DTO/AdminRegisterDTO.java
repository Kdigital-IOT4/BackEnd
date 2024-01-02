package com.baro.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class AdminRegisterDTO {
    private String adminId;
    private String adminPassword;


    public boolean isCreateValid() {
        // Null 또는 빈 문자열 체크
        if (adminId == null || adminId.isEmpty() || adminPassword == null || adminPassword.isEmpty()) {
            log.error("adminId 또는 adminPassword가 null 또는 빈 문자열입니다.");
            return false;
        }

        // adminId 조건 체크
        if (!adminId.matches("[a-zA-Z0-9]+")) {
            log.error("adminId는 숫자와 영어만 허용됩니다.");
            return false;
        }

        // adminPassword 조건 체크
        if (!adminPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&*()-_+=<>?]).+$")) {
            log.error("adminPassword는 영어, 숫자, 특수기호(@#$%^&*()-_+=<>?)를 최소한 하나 이상 포함해야 합니다.");
            return false;
        }

        // 모든 조건을 통과하면 유효한 경우로 간주
        return true;
    }
}
