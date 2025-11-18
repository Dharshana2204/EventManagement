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
        List<Event> events = eventRepository.findAllByOrderByIsTrendingDescIdAsc();

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
        List<Event> events = eventRepository.findAllByOrderByIsTrendingDescIdAsc();

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
