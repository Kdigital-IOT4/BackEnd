package com.baro.domain.user.controller;

import com.baro.domain.user.repository.DTO.AdminRegisterDTO;
import com.baro.domain.user.repository.DTO.BasicUserRegisterDTO;
import com.baro.domain.user.repository.DTO.MachineRegisterDTO;
import com.baro.domain.user.service.AdminService;
import com.baro.domain.user.service.BasicUserService;
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
    private final BasicUserService basicUserService;
    private final AdminService adminService;

    @PostMapping("/basic/register")
    public ResponseEntity basicUser_register_controller(@RequestBody BasicUserRegisterDTO basicUserRegisterDTO){
        log.info("basicUser_register_controller : {} ", basicUserRegisterDTO.getBasicUserPhoneNumber());
        String return_text = basicUserService.basicUser_register_service(basicUserRegisterDTO);
        if(return_text.equals("success")) {
            log.info("basic User register_success");
            return ResponseEntity.ok(return_text);
        }else{
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
        }
    }
    @PostMapping("/machine/register")
    public ResponseEntity machine_register_controller(@RequestBody MachineRegisterDTO machineRegisterDTO){
        log.info("machine_register_controller : {}",machineRegisterDTO.getMachineId());

        String return_text = machineService.machine_register_service(machineRegisterDTO);
        if(return_text.equals("success")){
            log.info("machine register Success");
            return ResponseEntity.ok(return_text);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
        }
    }
    @PostMapping("/admin/register")
    public ResponseEntity admin_register_controller(@RequestBody AdminRegisterDTO adminRegisterDTO){
        log.info("admin_register_controller :  {}",adminRegisterDTO.getAdminId());
        String return_text = adminService.admin_register_service(adminRegisterDTO);
        if(return_text.equals("success")){
            log.info("admin register Success");
            return ResponseEntity.ok(return_text);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
        }
    }


}
