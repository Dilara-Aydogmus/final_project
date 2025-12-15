package com.example.demo.dto;

import com.example.demo.model.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MoodResponse {
    private Long id;
    private MoodType moodType;
    private String note;
    private LocalDateTime createdAt;
    private String username;
}
