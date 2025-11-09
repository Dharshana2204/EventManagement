package com.example.SmartEventManagementSystem.service;

import com.example.SmartEventManagementSystem.dto.ApiResponse;
import com.example.SmartEventManagementSystem.dto.RegistrationRequest;
import com.example.SmartEventManagementSystem.dto.UserEventResponse;
import com.example.SmartEventManagementSystem.entity.Event;
import com.example.SmartEventManagementSystem.entity.Meetup;
import com.example.SmartEventManagementSystem.entity.Registration;
import com.example.SmartEventManagementSystem.repository.EventRepository;
import com.example.SmartEventManagementSystem.repository.MeetupRepository;
import com.example.SmartEventManagementSystem.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MeetupRepository meetupRepository;

    public ApiResponse registerForEvent(RegistrationRequest request) {
        // Trim fields
        String username = request.getUsername() != null ? request.getUsername().trim() : "";
        String eventType = request.getEventType() != null ? request.getEventType().trim().toLowerCase() : "";
        Long eventId = request.getEventId();

        // Validate inputs
        if (username.isEmpty()) {
            return new ApiResponse("Error", "Username is required");
        }

        if (eventId == null) {
            return new ApiResponse("Error", "Event ID is required");
        }

        if (eventType.isEmpty()) {
            return new ApiResponse("Error", "Event type is required");
        }

        if (!eventType.equals("event") && !eventType.equals("meetup")) {
            return new ApiResponse("Error", "Event type must be 'event' or 'meetup'");
        }

        // Check if user already registered for this event
        if (registrationRepository.existsByUsernameAndEventIdAndEventType(username, eventId, eventType)) {
            return new ApiResponse("Error", "Already registered");
        }

        // Verify that the event/meetup exists
        if (eventType.equals("event")) {
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isEmpty()) {
                return new ApiResponse("Error", "Event not found");
            }
        } else {
            Optional<Meetup> meetup = meetupRepository.findById(eventId);
            if (meetup.isEmpty()) {
                return new ApiResponse("Error", "Meetup not found");
            }
        }

        // Create registration
        Registration registration = new Registration();
        registration.setUsername(username);
        registration.setEventId(eventId);
        registration.setEventType(eventType);
        registration.setRegTime(LocalDateTime.now());

        registrationRepository.save(registration);

        return new ApiResponse("Success", "Registration successful");
    }

    public List<UserEventResponse> getUserRegisteredEvents(String username) {
        List<UserEventResponse> userEvents = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            return userEvents;
        }

        username = username.trim();

        // Get all registrations for the user
        List<Registration> registrations = registrationRepository.findByUsername(username);

        for (Registration registration : registrations) {
            if (registration.getEventType().equals("event")) {
                // Fetch event details
                Optional<Event> eventOpt = eventRepository.findById(registration.getEventId());
                if (eventOpt.isPresent()) {
                    Event event = eventOpt.get();
                    userEvents.add(new UserEventResponse(
                            event.getId(),
                            event.getTitle(),
                            event.getDescription(),
                            event.getLocation(),
                            event.getTiming(),
                            event.getStartingDate(),
                            event.getIsTrending()
                    ));
                }
            } else if (registration.getEventType().equals("meetup")) {
                // Fetch meetup details
                Optional<Meetup> meetupOpt = meetupRepository.findById(registration.getEventId());
                if (meetupOpt.isPresent()) {
                    Meetup meetup = meetupOpt.get();
                    userEvents.add(new UserEventResponse(
                            meetup.getId(),
                            meetup.getTitle(),
                            meetup.getDescription(),
                            meetup.getLocation(),
                            meetup.getTiming(),
                            meetup.getStartingDate(),
                            meetup.getIsTrending()
                    ));
                }
            }
        }

        return userEvents;
    }
}

