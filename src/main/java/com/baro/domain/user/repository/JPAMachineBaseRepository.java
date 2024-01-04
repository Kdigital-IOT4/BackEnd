package com.baro.domain.user.repository;

import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.domain.MachineBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAMachineBaseRepository extends JpaRepository<MachineBase, Long> {

}
