package com.baro.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/basic/register")
    public ResponseEntity basicUser_register_controller(){
        return null;
    }
    @PostMapping("/machine/register")
    public ResponseEntity machine_register_controller(){
        return null;
    }
    @PostMapping("/admin/register")
    public ResponseEntity admin_register_controller(){
        return null;
    }


}
