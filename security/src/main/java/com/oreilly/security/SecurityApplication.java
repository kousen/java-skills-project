package com.oreilly.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main Spring Boot application for security demonstrations.
 * This module contains examples for:
 * - Topic 22: Input Validation (XSS, SQL injection prevention)
 * - Topic 23: Cryptographic APIs (PBKDF2, AES, RSA, digital signatures)
 * 
 * Security as a Layer: This demonstrates how security is typically
 * layered onto existing web services rather than built from scratch.
 */
@SpringBootApplication
@EnableWebSecurity
public class SecurityApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
    
    /**
     * Security configuration - demonstrates layering security onto web services
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/security/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) // Disabled for demo purposes
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.deny())); // XSS protection
        
        return http.build();
    }
}