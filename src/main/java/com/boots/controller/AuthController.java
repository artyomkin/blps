package com.boots.controller;

import com.boots.dto.RegDTO;
import com.boots.service.UserService;
import com.boots.service.serviceResponses.RegistrationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/registration")
    public ResponseEntity registration(@RequestBody RegDTO regDTO)
    {
        RegistrationStatus regResp = userService.saveUser(regDTO.username, regDTO.email, regDTO.password, regDTO.passwordConfirm);
        if (regResp.equals(RegistrationStatus.USER_ALREADY_EXISTS)) {
            return ResponseEntity.badRequest().body("User " + regDTO.username + " already exists.");
        } else if (regResp.equals(RegistrationStatus.PASSWORDS_DO_NOT_MATCH)){
            return ResponseEntity.badRequest().body("Password do not match password confirmation.");
        }
        return ResponseEntity.ok("Registered.");
    }
}
