package com.example.SmartEventManagementSystem.controller;

import com.example.SmartEventManagementSystem.dto.ApiResponse;
import com.example.SmartEventManagementSystem.dto.RegistrationRequest;
import com.example.SmartEventManagementSystem.dto.UserEventResponse;
import com.example.SmartEventManagementSystem.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/registerEvent")
    public ResponseEntity<ApiResponse> registerForEvent(@RequestBody RegistrationRequest request) {
        ApiResponse response = registrationService.registerForEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserEvents")
    public ResponseEntity<List<UserEventResponse>> getUserEvents(@RequestParam String username) {
        List<UserEventResponse> events = registrationService.getUserRegisteredEvents(username);
        return ResponseEntity.ok(events);
    }
}
