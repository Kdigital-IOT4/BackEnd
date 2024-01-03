package com.baro.domain.cocktail.repository;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.user.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPABaseRepository extends JpaRepository<Base, Long> {
    boolean existsByName(String en_name);

    // 데이터 전체 조회
    List<Base> findAll();

    Base findBySeq(Long seq);
}
