package com.baro.domain.user.service;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.repository.DAO.BaseDAO;
import com.baro.domain.cocktail.repository.DAO.BaseMachineReadDAO;
import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.domain.MachineBase;
import com.baro.domain.user.repository.DAO.MachineBaseReadDAO;
import com.baro.domain.user.repository.DTO.Machine.MachineBaseReadDTO;
import com.baro.domain.user.repository.JPAMachineBaseRepository;
import com.baro.domain.user.repository.JPAMachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MachineBaseService {

    private final JPAMachineBaseRepository machineBaseRepository;
    private final JPAMachineRepository machineRepository;

    public boolean already_exists_machineBase_check_service(String machineId){
        Machine machine = machineRepository.findById(machineId);
        if(machineBaseRepository.existsByMachine(machine)){
            //존재
            log.info("이미 머신베이스가 존재합니다");
            return true;
        }else{
            //존재하지않음
            return false;
        }

    }
    @Transactional
    public boolean delete_machineBase_service(String machineId){
        log.info("머신베이스 삭제처리 서비스를 시작합니다.");
        //머신데이터 찾아오기
        Machine machine = machineRepository.findById(machineId);
        List<MachineBase> machineBaseList= machineBaseRepository.findByMachineAndIsDELETED(machine , false);
        for (MachineBase machineBase : machineBaseList) {
            machineBase.setDELETED(true);
        }
        try {
            machineBaseRepository.saveAll(machineBaseList);
            log.info("머신베이스 삭제처리 서비스가 완료되었습니다.");
            return true;
        }catch (Exception e){
            log.warn("머신베이스 삭제중 문제발생 \n{}",e);
            return false;
        }

    }

    public MachineBaseReadDAO read_machine_base_service(String machineId) {
        Machine machine = machineRepository.findById(machineId);
        List<MachineBase> machineBaseList = machineBaseRepository.findByMachineAndIsDELETED(machine, false);

        List<BaseMachineReadDAO> baseList = machineBaseList.stream()
                .map(this::mapToBaseDAO)
                .collect(Collectors.toList());

        MachineBaseReadDAO machineBaseReadDAO = new MachineBaseReadDAO();
        machineBaseReadDAO.setMachineId(machineId);
        machineBaseReadDAO.setBaseList(baseList);

        return machineBaseReadDAO;
    }

    private BaseMachineReadDAO mapToBaseDAO(MachineBase machineBase) {
        BaseMachineReadDAO baseDAO = new BaseMachineReadDAO();
        Base base = machineBase.getBase();
        int base_line = machineBase.getLineNumber();

        baseDAO.setBase_line(base_line);
        baseDAO.setEN_Name(base.getName());
        baseDAO.setKR_Name(base.getKrName());
        baseDAO.setPrice(base.getPrice());
        baseDAO.setAmount(base.getAmount());
        baseDAO.setAlcohol(base.getAlcohol());
        baseDAO.setContent(base.getContentL());
        baseDAO.setImgURL(base.getFileURL());

        return baseDAO;
    }

}
