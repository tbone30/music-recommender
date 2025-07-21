package com.musicrecommender.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.entity.User;
import com.musicrecommender.backend.repository.UserRepository;
import com.musicrecommender.backend.dto.UserRegistrationRequest;
import com.musicrecommender.backend.dto.UserLoginRequest;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public Mono<User> registerUser(UserRegistrationRequest request) {
        return userRepository.existsByUsername(request.getUsername())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new RuntimeException("Username already exists"));
                }
                return userRepository.existsByEmail(request.getEmail());
            })
            .flatMap(emailExists -> {
                if (emailExists) {
                    return Mono.error(new RuntimeException("Email already exists"));
                }
                
                String hashedPassword = hashPassword(request.getPassword());
                User user = new User(request.getUsername(), request.getEmail(), hashedPassword);
                return userRepository.save(user);
            });
    }
    
    public Mono<User> authenticateUser(UserLoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .filter(user -> verifyPassword(request.getPassword(), user.getPasswordHash()))
            .switchIfEmpty(Mono.error(new RuntimeException("Invalid username or password")));
    }
    
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Mono<User> updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    private boolean verifyPassword(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }
}
