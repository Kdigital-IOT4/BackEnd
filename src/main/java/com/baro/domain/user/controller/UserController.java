package com.baro.domain.user.controller;

import com.baro.domain.user.repository.DTO.MachineRegisterDTO;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {
    /**
     * user list
     * basic user
     * machine
     * admin
     */

    private final MachineService machineService;

    @PostMapping("/basic/register")
    public ResponseEntity basicUser_register_controller(){
        return null;
    }
    @PostMapping("/machine/register")
    public ResponseEntity machine_register_controller(@RequestBody MachineRegisterDTO machineRegisterDTO){
        log.info("machine_register_controller : {} , {}",machineRegisterDTO.getMachineId(),machineRegisterDTO.getMachinePassWord());

        String return_text = machineService.machine_register_service(machineRegisterDTO);
        if(return_text.equals("success")){
            log.info("machine register Success");
            return ResponseEntity.ok(return_text);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
        }
    }
    @PostMapping("/admin/register")
    public ResponseEntity admin_register_controller(){
        return null;
    }


}
