package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class LIstBaseDAO {
    /**
     * list 용 baseDAO 필요한 정보를 프론트에서 요청하면 이를 주기위한
     */

    private Long seq;
    private String EN_Name;
    private String KR_Name;
    private String fileURL;
}
