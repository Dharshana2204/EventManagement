package com.example.SmartEventManagementSystem.repository;

import com.example.SmartEventManagementSystem.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find all events ordered by isTrending (true first), then by id
    List<Event> findAllByOrderByIsTrendingDescIdAsc();
}

