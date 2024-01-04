package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class ListCockTailDAO {

    private Long seq;
    private String EN_Name;
    private String KR_Name;
    private String fileURL;
}
