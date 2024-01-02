package com.baro.domain.user.repository;

import com.baro.domain.user.domain.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPABasicUserRepository extends JpaRepository<BasicUser, Long> {
    boolean existsByPhoneNumber(int phoneNumber);
}
