package com.example.demo.controller;

import com.example.demo.dto.MoodResponse;
import com.example.demo.mapper.MoodMapper;
import com.example.demo.model.MoodEntry;
import com.example.demo.model.MoodType;
import com.example.demo.model.User;
import com.example.demo.repository.MoodEntryRepository;
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
    private final MoodEntryRepository moodEntryRepository;

    private User getCurrentUserOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }


    // CREATE (entity dönmesi OK)

    @PostMapping
    public ResponseEntity<MoodEntry> createMood(@RequestBody MoodRequest request) {
        User user = getCurrentUserOrThrow();
        return ResponseEntity.ok(
                moodService.createMood(
                        user,
                        request.getMoodType(),
                        request.getNote()
                )
        );
    }

    // TODAY DTO + MAPPER
    @GetMapping("/today")
    public ResponseEntity<MoodResponse> getTodayMood() {

        User user = getCurrentUserOrThrow();

        Long moodId = moodService.getTodayMoodId(user);
        if (moodId == null) {
            return ResponseEntity.noContent().build();
        }

        MoodEntry entry = moodEntryRepository.findById(moodId).orElseThrow();

        return ResponseEntity.ok(MoodMapper.toDto(entry));
    }

    // HISTORY  DTO
    @GetMapping("/history")
    public ResponseEntity<List<MoodResponse>> getHistory(
            @RequestParam(defaultValue = "7") int days
    ) {
        User user = getCurrentUserOrThrow();

        return ResponseEntity.ok(
                moodService.getHistory(user, days)
                        .stream()
                        .map(MoodMapper::toDto)
                        .toList()
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<MoodEntry> updateMood(
            @PathVariable Long id,
            @RequestBody MoodRequest request
    ) {
        User user = getCurrentUserOrThrow();
        MoodEntry updated = moodService.updateMood(
                id,
                request.getMoodType(),
                request.getNote(),
                user
        );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Long id) {
        User user = getCurrentUserOrThrow();
        moodService.deleteMood(id, user);
        return ResponseEntity.noContent().build();
    }

    // REQUEST BODY, inner class
    @lombok.Data
    public static class MoodRequest {
        private MoodType moodType;
        private String note;
    }
}
