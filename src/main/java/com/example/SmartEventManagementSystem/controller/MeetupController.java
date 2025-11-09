package com.example.SmartEventManagementSystem.controller;

import com.example.SmartEventManagementSystem.dto.MeetupResponse;
import com.example.SmartEventManagementSystem.service.MeetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MeetupController {

    @Autowired
    private MeetupService meetupService;

    @GetMapping("/getAllMeetups")
    public ResponseEntity<List<MeetupResponse>> getAllMeetups() {
        List<MeetupResponse> meetups = meetupService.getAllMeetups();
        return ResponseEntity.ok(meetups);
    }
}

