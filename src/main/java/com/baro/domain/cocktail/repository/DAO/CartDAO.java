package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class CartDAO {
    private List<CartCocktailDetailDAO> cartData;
}
