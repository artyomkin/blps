package com.boots.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
    private AbstractJaasAuthenticationProvider jaasAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.jaasAuthenticationProvider);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
        http
            .csrf().disable()
            .cors().configurationSource(request -> corsConfiguration)
            .and()
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

}
