package com.meetandride.service;

import com.meetandride.model.User;
import com.meetandride.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Registra un nuovo utente
    public User registerUser(String username, String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, email, encodedPassword);
        return userRepository.save(user);
    }

    // 🔹 Trova utente per username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 🔹 Controlla se esiste già un utente con quell'email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 🔹 Recupera l'utente attualmente loggato (più avanti useremo SecurityContext)
    public User getAuthenticatedUser() {
        // per ora ritorna null, sarà implementato dopo con SecurityContext
        return null;
    }
}
