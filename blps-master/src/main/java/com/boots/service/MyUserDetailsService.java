package com.boots.service;

import com.boots.entity.MyUserDetails;
import com.boots.entity.Role;
import com.boots.entity.User;
import com.boots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class MyUserDetailsService implements UserDetailsService {

    //get user from the database, via Hibernate
    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
//CUSTOM USER HERE vvv
        User user = userRepository.findByUsername(username);
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        List<GrantedAuthority> authorities =
                buildUserAuthority(user.getRoles().stream().collect(Collectors.toList()));
//if you're implementing UserDetails you wouldn't need to call this method and instead return the User as it is
        return buildUserForAuthentication(user, authorities);

    }

    // Converts user to spring.springframework.security.core.userdetails.User
    private MyUserDetails buildUserForAuthentication(User user,
                                                     List<GrantedAuthority> authorities) {
        return new MyUserDetails(user);

    }

    private List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // add user's authorities
        for (Role userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }

        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

        return Result;
    }

}