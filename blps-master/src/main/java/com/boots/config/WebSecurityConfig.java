package com.boots.config;

import com.boots.security.jwt.JwtConfigurer;
import com.boots.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));

        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(request -> corsConfiguration)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(REGISTER_ENDPOINT).permitAll()
                .antMatchers(COMMENT_ENDPOINT).permitAll()
                .antMatchers(VIDEO_ENDPOINT).permitAll()
                .antMatchers(REPORT_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_ENDPOINT).permitAll()
                .antMatchers(API_SPECIFICATION_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_HTML_ENDPOINT).permitAll()
                .antMatchers(WEBJARS_ENDPOINT).permitAll()
                .antMatchers(V2_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_RESOURCES_ENDPOINT).permitAll()
                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}