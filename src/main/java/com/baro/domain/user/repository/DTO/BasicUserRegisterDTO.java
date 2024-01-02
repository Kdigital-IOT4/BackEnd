package com.baro.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class BasicUserRegisterDTO {
    private int basicUserPhoneNumber;
    private String basicUserPassword;

    public boolean isCreateValid() {
        // 전화번호 자릿수 체크
        if (String.valueOf(basicUserPhoneNumber).length() != 11) {
            log.error("basicUserPhoneNumber는 11자여야 합니다.");
            return false;
        }

        // Null 또는 빈 문자열 체크
        if (basicUserPassword == null || basicUserPassword.isEmpty()) {
            log.error("basicUserPassword가 null 또는 빈 문자열입니다.");
            return false;
        }

        // basicUserPassword 조건 체크
        if (!basicUserPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&*()-_+=<>?]).+$")) {
            log.error("basicUserPassword는 영어, 숫자, 특수기호(@#$%^&*()-_+=<>?)를 최소한 하나 이상 포함해야 합니다.");
            return false;
        }

        // 모든 조건을 통과하면 유효한 경우로 간주
        return true;
    }
}
