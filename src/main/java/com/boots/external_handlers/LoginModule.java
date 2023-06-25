package com.boots.external_handlers;

import com.boots.entity.User;
import com.boots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginModule {
    @Autowired
    UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }
}
