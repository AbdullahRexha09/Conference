package com.example.Conference.repository;

import com.example.Conference.domain.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {
    List<ConferenceRoom> findByName(String name);

}
