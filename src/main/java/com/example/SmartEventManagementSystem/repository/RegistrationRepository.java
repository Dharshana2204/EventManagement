package com.example.SmartEventManagementSystem.repository;

import com.example.SmartEventManagementSystem.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Check if user already registered for a specific event
    boolean existsByUsernameAndEventIdAndEventType(String username, Long eventId, String eventType);

    // Find all registrations for a specific user
    List<Registration> findByUsername(String username);
}

