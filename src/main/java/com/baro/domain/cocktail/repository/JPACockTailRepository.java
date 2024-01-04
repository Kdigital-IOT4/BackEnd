package com.baro.domain.cocktail.repository;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.domain.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPACockTailRepository extends JpaRepository<Cocktail, Long> {
    boolean existsByName(String en_name);
    boolean existsBySeq(Long seq);
    List<Cocktail> findAll();

    Cocktail findBySeq(Long seq);

    Cocktail findByName(String en_name);
}
