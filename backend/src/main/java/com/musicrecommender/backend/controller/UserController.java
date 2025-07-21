package com.musicrecommender.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.dto.UserRegistrationRequest;
import com.musicrecommender.backend.dto.UserLoginRequest;
import com.musicrecommender.backend.service.UserService;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, Object>>> registerUser(@RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request)
            .map(user -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "User registered successfully");
                response.put("userId", user.getId());
                response.put("username", user.getUsername());
                return ResponseEntity.ok(response);
            })
            .onErrorReturn(ResponseEntity.badRequest().body(createErrorResponse("Registration failed")));
    }
    
    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> loginUser(@RequestBody UserLoginRequest request) {
        return userService.authenticateUser(request)
            .map(user -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("userId", user.getId());
                response.put("username", user.getUsername());
                return ResponseEntity.ok(response);
            })
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Invalid credentials")));
    }
    
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<Map<String, Object>>> getUser(@PathVariable Long userId) {
        return userService.findById(userId)
            .map(user -> {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("email", user.getEmail());
                userInfo.put("createdAt", user.getCreatedAt());
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("user", userInfo);
                return ResponseEntity.ok(response);
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId)
            .then(Mono.just(ResponseEntity.ok(createSuccessResponse("User deleted successfully"))))
            .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Failed to delete user")));
    }
    
    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}
