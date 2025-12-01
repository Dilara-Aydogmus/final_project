package com.example.demo.controller;

import com.example.demo.model.MoodEntry;
import com.example.demo.model.MoodType;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moods")
@RequiredArgsConstructor
public class MoodController {

    private final MoodService moodService;
    private final UserRepository userRepository;

    // Geçici
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    // Yeni mood ekle
    @PostMapping
    public ResponseEntity<MoodEntry> createMood(
            @RequestParam Long userId,
            @RequestBody MoodRequest request
    ) {
        User user = getUserOrThrow(userId);
        MoodEntry entry = moodService.createMood(user, request.getMoodType(), request.getNote());
        return ResponseEntity.ok(entry);
    }

    // Bugünün mood kaydını getir
    @GetMapping("/today")
    public ResponseEntity<MoodEntry> getTodayMood(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        MoodEntry entry = moodService.getTodayMood(user);
        if (entry == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entry);
    }

    // Son X günün geçmişi
    @GetMapping("/history")
    public ResponseEntity<List<MoodEntry>> getHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "7") int days
    ) {
        User user = getUserOrThrow(userId);
        List<MoodEntry> history = moodService.getHistory(user, days);
        return ResponseEntity.ok(history);
    }

    // Mood güncelle
    @PutMapping("/{id}")
    public ResponseEntity<MoodEntry> updateMood(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody MoodRequest request
    ) {
        User user = getUserOrThrow(userId);
        MoodEntry updated = moodService.updateMood(id, request.getMoodType(), request.getNote(), user);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Mood sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMood(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        User user = getUserOrThrow(userId);
        moodService.deleteMood(id, user);
        return ResponseEntity.noContent().build();
    }

    // INNER DTO CLASS
    @lombok.Data
    public static class MoodRequest {
        private MoodType moodType;
        private String note;
    }
}
