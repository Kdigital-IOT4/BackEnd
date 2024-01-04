package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class RecipeDAO {
    private String base_en_name;
    private String base_kr_name;
    private int amount;
}
