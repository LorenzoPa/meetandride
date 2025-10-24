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
            //Disattiva CSRF solo se non hai form HTML diretti (Vaadin lo gestisce da sé)
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                //Rotte pubbliche (registrazione, login)
                .requestMatchers(
                    "/", "/login", "/register",
                    "/images/**", "/styles/**", "/VAADIN/**",
                    "/frontend/**", "/webjars/**", "/icons/**",
                    "/manifest.webmanifest", "/sw.js", "/offline.html"
                ).permitAll()

                //admin
                .requestMatchers("/admin/**").hasRole("ADMIN")

                //Tutto il resto richiede autenticazione
                .anyRequest().authenticated()
            )

            //Configurazione form login
            .formLogin(login -> login
                .loginPage("/login")                  //pagina custom Vaadin
                .defaultSuccessUrl("/", true)         //redirect dopo login
                .failureUrl("/login?error")           //redirect se login fallisce
                .permitAll()
            )

            //logout
            .logout(logout -> logout
                .logoutUrl("/logout")
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
