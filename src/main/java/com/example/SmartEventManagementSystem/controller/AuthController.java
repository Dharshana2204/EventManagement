package com.example.SmartEventManagementSystem.controller;

import com.example.SmartEventManagementSystem.dto.ApiResponse;
import com.example.SmartEventManagementSystem.dto.LoginRequest;
import com.example.SmartEventManagementSystem.dto.RegisterRequest;
import com.example.SmartEventManagementSystem.service.AuthService;
import com.example.SmartEventManagementSystem.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    // -------------------- REGISTER --------------------
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        ApiResponse response = authService.register(request);

        if (!"Success".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {

        ApiResponse response = authService.login(request);

        if ("Success".equalsIgnoreCase(response.getStatus())) {

            // 🔥 FIXED: Only send email if email exists
            if (response.getEmail() != null && !response.getEmail().trim().isEmpty()) {

                try {
                    emailService.sendLoginEmail(response.getEmail(), response.getName());
                } catch (Exception e) {
                    System.out.println("❌ Error sending login email: " + e.getMessage());
                }
            }
        }

        if (!response.getStatus().equalsIgnoreCase("Success")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
