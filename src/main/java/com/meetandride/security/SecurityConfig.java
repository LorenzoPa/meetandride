package com.meetandride.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // 🔓 Rotte pubbliche (accesso libero)
                .requestMatchers("/", "/login", "/register",
                        "/images/**", "/styles/**", "/VAADIN/**",
                        "/frontend/**", "/webjars/**", "/icons/**", "/manifest.webmanifest"
                ).permitAll()
                // 🔒 Rotte riservate agli Admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 🔒 Tutto il resto richiede login
                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/", true) // 👈 reindirizza alla home dopo login
                .permitAll()
                )
                .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
