package com.baro.domain.cocktail.repository;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.domain.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPACockTailRepository extends JpaRepository<Cocktail, Long> {
    boolean existsByName(String en_name);
}
