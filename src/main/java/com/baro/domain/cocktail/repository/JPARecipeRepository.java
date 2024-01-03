package com.baro.domain.cocktail.repository;

import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPARecipeRepository extends JpaRepository<Recipe, Long> {
}
