package com.example.demo.controller;

import com.example.demo.model.MoodEntry;
import com.example.demo.model.MoodType;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moods")
@RequiredArgsConstructor
public class MoodController {

    private final MoodService moodService;
    private final UserRepository userRepository;

    private User getCurrentUserOrThrow() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + username));
    }

    @PostMapping
    public ResponseEntity<MoodEntry> createMood(
            @RequestBody MoodRequest request
    ) {
        User user = getCurrentUserOrThrow();
        MoodEntry entry = moodService.createMood(user, request.getMoodType(), request.getNote());
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/today")
    public ResponseEntity<MoodEntry> getTodayMood() {
        User user = getCurrentUserOrThrow();
        MoodEntry entry = moodService.getTodayMood(user);
        if (entry == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/history")
    public ResponseEntity<List<MoodEntry>> getHistory(
            @RequestParam(defaultValue = "7") int days
    ) {
        User user = getCurrentUserOrThrow();
        List<MoodEntry> history = moodService.getHistory(user, days);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoodEntry> updateMood(
            @PathVariable Long id,
            @RequestBody MoodRequest request
    ) {
        User user = getCurrentUserOrThrow();
        MoodEntry updated = moodService.updateMood(id, request.getMoodType(), request.getNote(), user);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Long id) {
        User user = getCurrentUserOrThrow();
        moodService.deleteMood(id, user);
        return ResponseEntity.noContent().build();
    }

    @lombok.Data
    public static class MoodRequest {
        private MoodType moodType;
        private String note;
    }
}
