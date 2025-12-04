package com.example.demo.service;

import com.example.demo.model.MoodEntry;
import com.example.demo.model.MoodType;
import com.example.demo.model.User;
import com.example.demo.repository.MoodEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodEntryRepository moodRepo;

    public MoodEntry createMood(User user, MoodType moodType, String note) {
        MoodEntry entry = MoodEntry.builder()
                .user(user)
                .moodType(moodType)
                .note(note)
                .createdAt(LocalDateTime.now())
                .build();
        return moodRepo.save(entry);
    }

    public MoodEntry getTodayMood(User user) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        List<MoodEntry> entries = moodRepo.findByUserAndCreatedAtBetween(user, start, end);
        return entries.isEmpty() ? null : entries.get(0);
    }

    public List<MoodEntry> getHistory(User user, int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return moodRepo.findByUserAndCreatedAtBetween(user, start, end);
    }

    public MoodEntry updateMood(Long id, MoodType moodType, String note, User user) {
        MoodEntry entry = moodRepo.findById(id).orElse(null);
        if (entry == null || !entry.getUser().getId().equals(user.getId())) {
            return null;
        }
        entry.setMoodType(moodType);
        entry.setNote(note);
        return moodRepo.save(entry);
    }

    public void deleteMood(Long id, User user) {
        MoodEntry entry = moodRepo.findById(id).orElse(null);
        if (entry != null && entry.getUser().getId().equals(user.getId())) {
            moodRepo.delete(entry);
        }
    }
}
