package com.meetandride.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meetandride.model.User;
import com.meetandride.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Registra un nuovo utente
    public User registerUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username già in uso.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email già registrata.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, email, encodedPassword);
        return userRepository.save(user);
    }

    //Trova utente per username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Controlla se esiste già un utente con quell'email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //Controlla se esiste già un utente con l'user scelto
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    //Recupera l'utente attualmente loggato
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }
}
