package com.example.demo.service;

import com.example.demo.model.MoodEntry;
import com.example.demo.model.MoodType;
import com.example.demo.model.User;
import com.example.demo.repository.MoodEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodEntryRepository moodRepo;

    // CREATE :cache temizlenir
    @CacheEvict(value = "todayMood", allEntries = true)
    public MoodEntry createMood(User user, MoodType moodType, String note) {
        MoodEntry entry = MoodEntry.builder()
                .user(user)
                .moodType(moodType)
                .note(note)
                .createdAt(LocalDateTime.now())
                .build();
        return moodRepo.save(entry);
    }

    // SADECE ID CACHE'LENİR
    @Cacheable(value = "todayMood", key = "#user.id")
    public Long getTodayMoodId(User user) {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        return moodRepo.findByUserAndCreatedAtBetween(user, start, end)
                .stream()
                .max(Comparator.comparing(MoodEntry::getCreatedAt))
                .map(MoodEntry::getId)
                .orElse(null);
    }

    // HISTORY
    public List<MoodEntry> getHistory(User user, int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return moodRepo.findByUserAndCreatedAtBetween(user, start, end);
    }

    // UPDATE  cache temizlenir
    @CacheEvict(value = {"todayMood", "historyMood"}, key = "#user.id")
    public MoodEntry updateMood(Long id, MoodType moodType, String note, User user) {
        MoodEntry entry = moodRepo.findById(id).orElse(null);
        if (entry == null || !entry.getUser().getId().equals(user.getId())) {
            return null;
        }
        entry.setMoodType(moodType);
        entry.setNote(note);
        return moodRepo.save(entry);
    }

    // DELETE cache temizlenir
    @CacheEvict(value = {"todayMood", "historyMood"}, key = "#user.id")
    public void deleteMood(Long id, User user) {
        MoodEntry entry = moodRepo.findById(id).orElse(null);
        if (entry != null && entry.getUser().getId().equals(user.getId())) {
            moodRepo.delete(entry);
        }
    }
}
