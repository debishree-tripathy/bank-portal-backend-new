package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for Postman/testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll()  // signup/login
                        .requestMatchers("/hello").permitAll()         // allow hello
                        .requestMatchers("/api/transactions/**").permitAll() // allow balance/recent endpoints
                        .anyRequest().authenticated()                  // all other endpoints require auth
                )
                .formLogin(form -> form.disable())  // disable default login form
                .httpBasic(basic -> basic.disable()); // disable basic auth

        return http.build();
    }
}
