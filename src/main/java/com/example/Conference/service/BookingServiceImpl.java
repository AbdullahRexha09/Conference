package com.example.Conference.service;

import com.example.Conference.domain.Booking;
import com.example.Conference.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Component
@Transactional
@Slf4j
@Service(value = "bookingService")
public class BookingServiceImpl implements BookingService{

    private BookingRepository bookingRepository;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }
    @Override
    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }
}
