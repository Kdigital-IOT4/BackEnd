package com.baro.domain.cocktail.repository;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPARecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCocktail(Cocktail cocktail);
    List<Recipe> findByBase(Base base);
}
