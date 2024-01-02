package com.baro.domain.user.repository;

import com.baro.domain.user.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAAdminRepository extends JpaRepository<Admin, Long> {
}
