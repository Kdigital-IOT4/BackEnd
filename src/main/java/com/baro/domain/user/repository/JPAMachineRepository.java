package com.baro.domain.user.repository;

import com.baro.domain.user.domain.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAMachineRepository extends JpaRepository<Machine, Long> {
    boolean existsById(String machineId);

    Machine findById(String machineId);
}
