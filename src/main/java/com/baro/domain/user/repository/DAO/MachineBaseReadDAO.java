package com.baro.domain.user.repository.DAO;

import com.baro.domain.cocktail.repository.DAO.BaseDAO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MachineBaseReadDAO {

    String machineId;
    List<BaseDAO> baseList;
}
