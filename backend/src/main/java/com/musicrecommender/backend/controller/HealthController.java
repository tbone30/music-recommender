package com.musicrecommender.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Music Recommender Backend is running!";
    }
    
    @GetMapping("/")
    public String home() {
        return "Welcome to Music Recommender API";
    }
}
