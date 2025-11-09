package com.example.SmartEventManagementSystem.service;

import com.example.SmartEventManagementSystem.dto.MeetupResponse;
import com.example.SmartEventManagementSystem.entity.Meetup;
import com.example.SmartEventManagementSystem.repository.MeetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetupService {

    @Autowired
    private MeetupRepository meetupRepository;

    public List<MeetupResponse> getAllMeetups() {
        // Fetch all meetups ordered by trending first
        List<Meetup> meetups = meetupRepository.findAllByOrderByIsTrendingDescIdAsc();

        // Convert to DTO
        return meetups.stream()
                .map(meetup -> new MeetupResponse(
                        meetup.getId(),
                        meetup.getTitle(),
                        meetup.getDescription(),
                        meetup.getLocation(),
                        meetup.getTiming(),
                        meetup.getStartingDate(),
                        meetup.getIsTrending()
                ))
                .collect(Collectors.toList());
    }
}

