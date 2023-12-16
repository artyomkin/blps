package com.boots.helpers;
import com.boots.entity.User;
import com.boots.service.UserService;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public class UserHelper {
    private String username;
    private User user;

    private UserService userService;

    public UserHelper(Principal principal, UserService userService) {
        this.username = principal.getName();;
        this.user = userService.findByUsername(username);
    }
    private ResponseEntity<String> checkUserName(){
        if (this.username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        return null;
    }
    private ResponseEntity<String> checkUser(){
        if (this.user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        return null;
    }
    private Long getUid(){
        return this.user.getId();
    }
    private void checkUnauthorizedUser(){
        checkUserName();
        checkUser();
    }
    public Long registeryUser(){
        checkUnauthorizedUser();
        return getUid();
    }
}
