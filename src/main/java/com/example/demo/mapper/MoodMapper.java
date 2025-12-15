package com.example.demo.mapper;

import com.example.demo.dto.MoodResponse;
import com.example.demo.model.MoodEntry;

public class MoodMapper {

    private MoodMapper() {}

    public static MoodResponse toDto(MoodEntry entry) {
        return new MoodResponse(
                entry.getId(),
                entry.getMoodType(),
                entry.getNote(),
                entry.getCreatedAt(),
                entry.getUser().getUsername()
        );
    }
}
