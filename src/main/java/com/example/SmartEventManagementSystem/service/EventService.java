package com.example.SmartEventManagementSystem.service;

import com.example.SmartEventManagementSystem.dto.EventResponse;
import com.example.SmartEventManagementSystem.dto.TopEventResponse;
import com.example.SmartEventManagementSystem.entity.Event;
import com.example.SmartEventManagementSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventResponse> getAllEvents() {
        // Fetch all events ordered by trending first
        List<Event> events = eventRepository.findAllByOrderByIsTrendingDescIdAsc();

        // Convert to DTO
        return events.stream()
                .map(event -> new EventResponse(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getTiming(),
                        event.getStartingDate(),
                        event.getIsTrending()
                ))
                .collect(Collectors.toList());
    }

    public List<TopEventResponse> getTopThreeEvents() {
        // Fetch all events ordered by trending first
        List<Event> events = eventRepository.findAllByOrderByIsTrendingDescIdAsc();

        // Get first 3 events and convert to TopEventResponse
        return events.stream()
                .limit(3)
                .map(event -> new TopEventResponse(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartingDate()
                ))
                .collect(Collectors.toList());
    }
}
