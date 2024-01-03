package com.baro.domain.cocktail.repository.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class CockTailUploadDTO {

    private String EN_Name;
    private String KR_Name;
    private int price;
    private int amount;
    private int alcohol;
    private String content;

    private List<String> baseList;
}
