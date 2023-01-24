package com.example.Conference.repository;

import com.example.Conference.domain.Booking;
import com.example.Conference.domain.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByConferenceRoomAndStartTimeBetween(ConferenceRoom room, LocalDateTime start, LocalDateTime end);
}
