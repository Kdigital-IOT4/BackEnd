package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseMachineReadDAO {
    private int base_line;

    private String EN_Name;
    private String KR_Name;
    private int price;
    private int amount;
    private int alcohol;
    private  String content;
    private String imgURL;
}
