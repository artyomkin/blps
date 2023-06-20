package com.boots.repository;

import com.boots.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Principal;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
