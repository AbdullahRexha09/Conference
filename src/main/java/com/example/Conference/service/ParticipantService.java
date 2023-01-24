package com.example.Conference.service;

import com.example.Conference.domain.Participant;

import java.util.List;

public interface ParticipantService {
    List<Participant> saveAll(List<Participant> participants);
    Participant findById(Long id);

    void deleteById(Long id);
}
