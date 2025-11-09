package com.example.SmartEventManagementSystem.repository;

import com.example.SmartEventManagementSystem.entity.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long> {

    // Find all meetups ordered by isTrending (true first), then by id
    List<Meetup> findAllByOrderByIsTrendingDescIdAsc();
}

