package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello").permitAll()           // allow hello
                        .requestMatchers("/notifications/**").permitAll() // allow notifications endpoints
                        .anyRequest().authenticated()                     // everything else requires auth
                )
                .formLogin(form -> form.disable()) // disable default login form
                .httpBasic(basic -> basic.disable()); // disable basic auth

        return http.build();
    }
}
