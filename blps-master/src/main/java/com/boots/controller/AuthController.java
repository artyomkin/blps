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


    //@PostMapping("/api/v1/login")
    //public ResponseEntity login(@RequestBody AuthDTO authDTO){
    //    try{

    //        String username = authDTO.username;
    //        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authDTO.password));
    //        User user = userService.findByUsername(authDTO.username);
    //        if (user == null){
    //            throw new UsernameNotFoundException("Username " + username + " not found.");
    //        }
    //        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRoles());
    //        Map<Object, Object> response = new HashMap<>();
    //        response.put("username", user.getUsername());
    //        response.put("token", token);
    //        return ResponseEntity.ok(response);
    //    } catch (AuthenticationException e){
    //        return ResponseEntity.badRequest().body("Incorrect username or password.");
    //    }
    //}

    @PostMapping("/api/v1/registration")
    public ResponseEntity registration(@RequestBody RegDTO regDTO)
    {
        RegistrationStatus regResp = userService.saveUser(regDTO.username, regDTO.password, regDTO.passwordConfirm);
        if (regResp.equals(RegistrationStatus.USER_ALREADY_EXISTS)) {
            return ResponseEntity.badRequest().body("User " + regDTO.username + " already exists.");
        } else if (regResp.equals(RegistrationStatus.PASSWORDS_DO_NOT_MATCH)){
            return ResponseEntity.badRequest().body("Password do not match password confirmation.");
        }
        return ResponseEntity.ok("Registered.");
    }
}
