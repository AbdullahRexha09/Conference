package com.example.Conference.service;

import com.example.Conference.domain.Booking;
import com.example.Conference.domain.ConferenceRoom;
import com.example.Conference.repository.BookingRepository;
import com.example.Conference.repository.ConferenceRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Component
@Transactional
@Slf4j
@Service(value = "conferenceRoom")
public class ConferenceRoomImpl implements ConferenceRoomService {
    private BookingRepository bookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;
    @Autowired
    public ConferenceRoomImpl(BookingRepository bookingRepository, ConferenceRoomRepository conferenceRoomRepository){
        this.bookingRepository = bookingRepository;
        this.conferenceRoomRepository = conferenceRoomRepository;
    }
    @Override
    public boolean isTimeAvailable(ConferenceRoom room, LocalDateTime startTime, LocalDateTime endTime) {
        // get all the booked time slots for the room
        List<Booking> bookings = bookingRepository.findByConferenceRoomAndStartTimeBetween(room, startTime, endTime);
        return bookings.isEmpty();
    }

    @Override
    public ConferenceRoom create(ConferenceRoom conferenceRoom) {
        return conferenceRoomRepository.save(conferenceRoom);
    }

    @Override
    public ConferenceRoom findById(long roomId) {
        return conferenceRoomRepository.findById(roomId).orElse(null);
    }

    @Override
    public List<ConferenceRoom> findByName(String name) {
        return conferenceRoomRepository.findByName(name);
    }

    @Override
    public List<ConferenceRoom> conferences() {
        return conferenceRoomRepository.findAll();
    }
}
