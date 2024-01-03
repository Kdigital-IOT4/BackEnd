package com.baro.domain.cocktail.repository;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.user.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPABaseRepository extends JpaRepository<Base, Long> {
    boolean existsByName(String en_name);
}
