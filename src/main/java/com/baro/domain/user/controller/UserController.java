package com.baro.domain.user.controller;

import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.repository.DTO.AdminRegisterDTO;
import com.baro.domain.user.repository.DTO.BasicUserRegisterDTO;
import com.baro.domain.user.repository.DTO.MachineLoginDTO;
import com.baro.domain.user.repository.DTO.MachineRegisterDTO;
import com.baro.domain.user.service.AdminService;
import com.baro.domain.user.service.BasicUserService;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
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

    @PostMapping("/basic/login")
    public ResponseEntity basicUser_login_controller(){
        return null;
    }
    @PostMapping("/machine/login")
    public ResponseEntity machine_login_controller(@RequestBody MachineLoginDTO machineLoginDTO){
        Map<String, Object> response = new HashMap<>();

        if(!machineService.check_machine_id(machineLoginDTO.getMachineId())){
            //존재하지 않음
            response.put("machine_id", machineLoginDTO.getMachineId());
            response.put("status", "fail");
            response.put("message", "not found ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String return_text = machineService.machine_login_service(machineLoginDTO);
        if(return_text.equals("success")){
            //로그인성공
            response.put("machine_id", machineLoginDTO.getMachineId());
            response.put("status", "success");
            response.put("message", "success login");


            return ResponseEntity.ok(response);
        }else{
            response.put("machine_id", machineLoginDTO.getMachineId());
            response.put("status", "fail");
            response.put("message", "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }
    }
    @GetMapping("/admin/login")
    public ResponseEntity admin_login_controller(){
        return null;
    }


    /**
     register start
     */
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

    /**
     register end
     */




}
