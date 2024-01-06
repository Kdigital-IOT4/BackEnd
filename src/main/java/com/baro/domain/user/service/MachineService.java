package com.baro.domain.user.service;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.repository.DAO.BaseMachineUploadDAO;
import com.baro.domain.cocktail.service.BaseService;
import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.domain.MachineBase;
import com.baro.domain.user.repository.DTO.Machine.MachineBaseDTO;
import com.baro.domain.user.repository.DTO.MachineLoginDTO;
import com.baro.domain.user.repository.DTO.MachineRegisterDTO;
import com.baro.domain.user.repository.JPAMachineBaseRepository;
import com.baro.domain.user.repository.JPAMachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MachineService {
    private final JPAMachineRepository machineRepository;
    private final JPAMachineBaseRepository machineBaseRepository;
    private final MachineBaseService machineBaseService;

    private final BaseService baseService;
    /**
     * todo
     * 1. temp register
     * 2. temp login
     */
    public Machine find_machine_data_service(String machineId){
        return  machineRepository.findById(machineId);
    }

    public boolean check_machine_id(String machineId){
        return machineRepository.existsById(machineId);
    }

    public boolean check_machine_line_service(String machineId , int lineCount){
       Machine machine =  machineRepository.findById(machineId);
       log.info("machine line check start");
       if(! (machine.getLine() < lineCount)){
           return true;
       }else{
           log.warn("line check!");
           return false;
       }
    }
    public String machine_data_reUpload_service(Machine machine , List<MachineBaseDTO> machineBaseList){
        log.info("machine data reUpload -> db start");
        machineBaseService.delete_machineBase_service(machine.getId());
        log.info("머신 데이터 삭제처리가 완료되었습니다. 다음스텝...");

        String return_text = machine_data_upload_service(machine , machineBaseList);
        return return_text;
    }


    public String machine_data_upload_service(Machine machine ,List<MachineBaseDTO> machineBaseList){
        log.info("machine data upload -> db start");
        String return_text;
        List<BaseMachineUploadDAO> baseList = find_base_Machine_list(machineBaseList);
        int checkFlag = baseList.size();


        for (MachineBaseDTO machineBaseDTO : machineBaseList){
            machineBaseDTO.getMachine_base_line();
        }


        for(BaseMachineUploadDAO base : baseList) {
            try {
                machineBaseRepository.save(
                        MachineBase.builder()
                                .machine(machine)
                                .base(base.getBase())
                                .lineNumber(base.getBase_line())
                                .build()
                );
                log.info("정상등록성공 ... 계속됩니다.");
                checkFlag--;
            } catch (DataIntegrityViolationException e) {
                // 데이터베이스 무결성 제약 조건 위반 - 키 중복  or 조건 위배
                return_text = "사용자의 데이터 제대로 검증되지 않았습니다.";
                log.warn("admin_register_service : {}", return_text);
            } catch (JpaSystemException e) {
                // JPA 연동 중 문제 발생
                return_text = "데이터베이스 연동 중 오류가 발생";
                log.warn("admin_register_service : {}", return_text);
            } catch (DataAccessException e) {
                // 데이터 액세스 오류
                return_text = "데이터베이스 액세스 중 오류가 발생";
                log.warn("admin_register_service : {}", return_text);
            } catch (Exception e) {
                // 다른 모든 예외 처리
                return_text = "알 수 없는 오류가 발생";
                log.warn(e.getMessage());

            }

        }
        log.info("등록이 완료되었습니다.");
        if(checkFlag ==0){
            return_text = "success";
        }else{
            log.warn("등록중오류가 발생하였습니다.");
            return_text = "fail";
            /**
             * 기능 구현 완료 후 삭제-> 재시도 로직 추가하기
             */
        }
        return return_text;
    }

    public String machine_login_service(MachineLoginDTO machineLoginDTO){
        String return_text;

        log.info("machineLogin Service {}", machineLoginDTO.getMachineId());
        if(machineRepository.existsById(machineLoginDTO.getMachineId())){
            //id 가 존재
            Machine machine = machineRepository.findById(machineLoginDTO.getMachineId());
            //log.info("machone pass {} , {}",machine.getPassword() , machineLoginDTO.getMachinePassword());
            if(machine.getPassword().equals(machineLoginDTO.getMachinePassWord())){
                //로그인성공
                return_text = "success";
            }else{
                //로그인실패
                log.info("패스워드가 틀렸습니다. {}" , machineLoginDTO.getMachineId());
                return_text ="패스워드가 틀렸습니다.";
            }

        }else{
            //존재 하지 않음
            log.info("존재하지않은 로그인 시도 {}" , machineLoginDTO.getMachineId());
            return_text="id가 존재하지 않습니다.";
        }
        return return_text;
    }

    public String machine_register_service(MachineRegisterDTO machineRegisterDTO){
        String return_text;

       if (machineRegisterDTO.isCreateValid()){
            if(! machineRepository.existsById(machineRegisterDTO.getMachineId())){
                //존재하지 않음
                try {
                    machineRepository.save(
                            Machine.builder()
                                    .id(machineRegisterDTO.getMachineId())
                                    .password(machineRegisterDTO.getMachinePassWord())
                                    .line(machineRegisterDTO.getLine())
                                    .build()
                    );
                    return_text ="success";
                }catch (DataIntegrityViolationException e) {
                    // 데이터베이스 무결성 제약 조건 위반 - 키 중복  or 조건 위배
                    return_text = "사용자의 데이터 제대로 검증되지 않았습니다.";
                    log.warn("machine_service : {}" , return_text);
                } catch (JpaSystemException e) {
                    // JPA 연동 중 문제 발생
                    return_text = "데이터베이스 연동 중 오류가 발생";
                    log.warn("machine_service : {}" , return_text);
                } catch (DataAccessException e) {
                    // 데이터 액세스 오류
                    return_text = "데이터베이스 액세스 중 오류가 발생";
                    log.warn("machine_service : {}" , return_text);
                } catch (Exception e) {
                    // 다른 모든 예외 처리
                    return_text = "알 수 없는 오류가 발생";
                    log.warn(e.getMessage());
                }
            }else{
                //존재한다
                return_text ="이미 존재하는 아이디입니다.";
                log.warn("machine_service : {}" , return_text);
            }
       }else{
           return_text ="잘못된 정보를 입력하였습니다.";
           log.warn("machine_service : {}" , return_text);
       }
        return return_text;
    }



    private List<BaseMachineUploadDAO> find_base_Machine_list(List<MachineBaseDTO> machineBaseList) {
        List<BaseMachineUploadDAO> baseList = new ArrayList<>();

        for (MachineBaseDTO baseData : machineBaseList) {
            Base base = baseService.findBaseToSeq(baseData.getBase_seq());
            log.info("add base {}", base.getName());


            int lineNum = baseData.getMachine_base_line();

            BaseMachineUploadDAO baseMachineUploadDAO = new BaseMachineUploadDAO();
            baseMachineUploadDAO.setBase(base);
            baseMachineUploadDAO.setBase_line(lineNum);

            baseList.add(baseMachineUploadDAO);
        }

        return baseList;
    }
}
