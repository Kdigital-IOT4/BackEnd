package com.baro.domain.user.repository.DTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class MachineRegisterDTO {
    private String machineId;
    private String machinePassWord;
    private int line;
    public boolean isCreateValid() {
        // Null 또는 빈 문자열 체크
        if (machineId == null || machineId.isEmpty() || machinePassWord == null || machinePassWord.isEmpty()) {
            log.error("machineId 또는 machinePassWord 가 null 또는 빈 문자열입니다.");
            return false;
        }

        // machineId 조건 체크
        if (machineId.length() > 30 || !machineId.matches("[a-zA-Z0-9]+")) {
            log.error("machineId는 30자 이하이어야 하며, 영어와 숫자만 허용됩니다.");
            return false;
        }

        // machinePassWord 조건 체크
        if (machinePassWord.length() > 40 && machinePassWord.matches("[a-zA-Z0-9!@#$%^&*()-_+=<>?]+")) {
            log.error("machinePassWord는 40자 이하이어야 하며, 영어, 숫자, 특수기호만 허용됩니다.");
            return false;
        }
        if (line < 1) {
            log.error("line은 실수");
            return false;
        }
        // 모든 조건을 통과하면 유효한 경우로 간주
        return true;
    }
}

