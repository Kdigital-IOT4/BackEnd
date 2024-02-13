package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class CartCocktailDetailDAO {
    private Long seq;
    private String en_name;
    private String kr_name;
    private int price;
    private String img_URL;
}
