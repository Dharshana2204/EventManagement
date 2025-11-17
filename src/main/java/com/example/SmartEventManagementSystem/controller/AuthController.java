package com.example.SmartEventManagementSystem.controller;

import com.example.SmartEventManagementSystem.dto.ApiResponse;
import com.example.SmartEventManagementSystem.dto.LoginRequest;
import com.example.SmartEventManagementSystem.dto.RegisterRequest;
import com.example.SmartEventManagementSystem.service.AuthService;
import com.example.SmartEventManagementSystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        ApiResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {

        ApiResponse response = authService.login(request);

        if ("Success".equalsIgnoreCase(response.getStatus())) {
            emailService.sendLoginEmail(response.getEmail(), response.getName());
        }

        return ResponseEntity.ok(response);
    }
}
