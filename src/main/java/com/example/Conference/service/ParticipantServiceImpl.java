package com.example.Conference.service;

import com.example.Conference.domain.Participant;
import com.example.Conference.repository.ParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Component
@Transactional
@Slf4j
@Service(value = "participantService")
public class ParticipantServiceImpl implements ParticipantService{

    private ParticipantRepository participantRepository;
    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository){
        this.participantRepository = participantRepository;
    }
    @Override
    public List<Participant> saveAll(List<Participant> participants) {
        return participantRepository.saveAll(participants);
    }

    @Override
    public Participant findById(Long id) {
        return participantRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
         participantRepository.deleteById(id);
    }
}
