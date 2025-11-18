package com.example.SmartEventManagementSystem.repository;

import com.example.SmartEventManagementSystem.entity.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long> {


    List<Meetup> findAllByOrderByIsTrendingDescIdAsc();
}

