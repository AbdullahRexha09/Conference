package com.example.Conference.service;

import com.example.Conference.domain.ConferenceRoom;

import java.time.LocalDateTime;
import java.util.List;

public interface ConferenceRoomService {
    boolean isTimeAvailable(ConferenceRoom room, LocalDateTime startTime, LocalDateTime endTime);
    ConferenceRoom create(ConferenceRoom conferenceRoom);
    ConferenceRoom findById(long roomId);

    List<ConferenceRoom> findByName(String name);

    List<ConferenceRoom> conferences();
}
