package com.example.demo.repository;

import com.example.demo.model.MoodEntry;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {

    List<MoodEntry> findByUser(User user);

    List<MoodEntry> findByUserAndCreatedAtBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );
}
