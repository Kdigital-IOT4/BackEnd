package com.baro.domain.cocktail.repository.DTO;

import com.baro.domain.cocktail.repository.DAO.CocktailDAO;
import com.baro.domain.cocktail.repository.DAO.RecipeDAO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class CocktailDetailsDTO {
    private CocktailDAO cocktailDetail;
    private List<RecipeDAO> recipeList;
}
