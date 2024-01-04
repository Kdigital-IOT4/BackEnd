package com.baro.domain.user.controller;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.service.BaseService;
import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.repository.DTO.Machine.MachineBaseDTO;
import com.baro.domain.user.repository.DTO.Machine.MachineDataDTO;
import com.baro.domain.user.repository.DTO.MachineDataUpload;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/machine")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class MachineController {
    private final MachineService machineService;
    private final BaseService baseService;
    @PostMapping("/data/upload")
    public ResponseEntity machine_data_upload_controller(@RequestBody MachineDataUpload machineUploadData) {
        String machineId = machineUploadData.getMachineData().getMachineId();

        log.info("머신 베이스 등록 시작 ... {}",  machineId);

        if (!machineService.check_machine_id(machineId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("머신 아이디가 존재하지 않습니다. 관리자에게 문의하세요.");
        }

        if (!check_base_list(machineUploadData.getMachineBaseList())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("베이스 정보가 존재하지 않습니다. 관리자에게 문의하세요.");
        }

        try {
            log.info("체크 성공.. 다음스텝으로 넘어갑니다.");
            Machine machine = machineService.find_machine_data_service(machineId);
            List<Base> baseList = find_base_list(machineUploadData.getMachineBaseList());

            String machine_return_text = machineService.machine_data_upload_service(machine, baseList);

            if ("success".equals(machine_return_text)) {
                return ResponseEntity.ok(machine_return_text);
            } else {
                log.warn("업로드 실패..  {}", machine_return_text);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(machine_return_text);
            }
        } catch (Exception e) {
            log.error("머신 데이터 업로드 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생. 관리자에게 문의하세요.");
        }
    }

    private boolean check_base_list(List<MachineBaseDTO> machineBaseList){
        log.info("machine check base List start");
        int checkFlag = machineBaseList.size();
        for(MachineBaseDTO baseData : machineBaseList){
            log.info("find base ... {}",baseData.getBase_seq());

            if(baseService.checkBaseToSeq(baseData.getBase_seq())){
                //존재
                checkFlag--;
            }else{
                //없음
                log.warn("base 정보가 없습니다.");
            }
        }

        if(checkFlag ==0){
            //확인됨
            return true;
        }else {
            return false;
        }

    }

    private List<Base> find_base_list(List<MachineBaseDTO> machineBaseList) {
        List<Base> baseList = new ArrayList<>();

        for (MachineBaseDTO baseData : machineBaseList) {
            Base base = baseService.findBaseToSeq(baseData.getBase_seq());
            log.info("add base {}", base.getName());
            baseList.add(base);
        }

        return baseList;
    }
}
