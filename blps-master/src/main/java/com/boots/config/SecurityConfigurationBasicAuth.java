package com.boots.config;

import com.boots.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigurationBasicAuth extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private static final String REGISTER_ENDPOINT = "/api/v1/registration";
    private static final String COMMENT_ENDPOINT = "/api/v1/comments/**";
    private static final String VIDEO_ENDPOINT = "/api/v1/videos/**";
    private static final String REPORT_ENDPOINT = "/api/v1/reports/**";
    private static final String SWAGGER_ENDPOINT = "/swagger-ui/**";
    private static final String SWAGGER_HTML_ENDPOINT = "/swagger-ui.html";
    private static final String WEBJARS_ENDPOINT = "/webjars/**";
    private static final String V2_ENDPOINT = "/v2/**";
    private static final String SWAGGER_RESOURCES_ENDPOINT = "/swagger-resources/**";
    private static final String API_SPECIFICATION_ENDPOINT = "/v2/**";
    private static final String ADMIN_ENDPOINT = "/api/admin/**";

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

/*
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(bCryptPasswordEncoder.encode("userPass"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(bCryptPasswordEncoder.encode("adminPass"))
                .roles("USER", "ADMIN")
                .build());
        return manager;
    }

 */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //configuring our custom auth provider
        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        LOGIN_ENDPOINT,
                        REGISTER_ENDPOINT,
                        COMMENT_ENDPOINT,
                        VIDEO_ENDPOINT,
                        REPORT_ENDPOINT,
                        SWAGGER_ENDPOINT,
                        SWAGGER_HTML_ENDPOINT,
                        WEBJARS_ENDPOINT,
                        V2_ENDPOINT,
                        SWAGGER_RESOURCES_ENDPOINT,
                        API_SPECIFICATION_ENDPOINT
                )
                .permitAll()
                .antMatchers(
                        ADMIN_ENDPOINT
                )
                .hasAnyRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

                        /***/
}
