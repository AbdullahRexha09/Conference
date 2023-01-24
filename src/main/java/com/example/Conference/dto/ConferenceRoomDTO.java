package com.example.Conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceRoomDTO {
    private String name;

    private int totalParticipant;
    private int maxCapacity;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<ParticipantDTO> participantDTO;
}
