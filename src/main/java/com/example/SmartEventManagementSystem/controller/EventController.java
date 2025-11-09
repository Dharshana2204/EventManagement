package com.example.SmartEventManagementSystem.controller;

import com.example.SmartEventManagementSystem.dto.EventResponse;
import com.example.SmartEventManagementSystem.dto.TopEventResponse;
import com.example.SmartEventManagementSystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/getAllEvents")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/getTopEvents")
    public ResponseEntity<List<TopEventResponse>> getTopEvents() {
        List<TopEventResponse> topEvents = eventService.getTopThreeEvents();
        return ResponseEntity.ok(topEvents);
    }
}
