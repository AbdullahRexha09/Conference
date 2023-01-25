package com.example.Conference.api;

import com.example.Conference.domain.Booking;
import com.example.Conference.domain.ConferenceRoom;
import com.example.Conference.domain.Participant;
import com.example.Conference.dto.ConferenceRoomDTO;
import com.example.Conference.dto.ConferenceRoomResponseDTO;
import com.example.Conference.dto.ParticipantDTO;
import com.example.Conference.dto.ResponseMessageDTO;
import com.example.Conference.service.BookingService;
import com.example.Conference.service.ConferenceRoomService;
import com.example.Conference.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/conference")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ConferenceController {
    @Autowired
    private ConferenceRoomService conferenceRoomService;
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ParticipantService participantService;

    public ConferenceController(ConferenceRoomService conferenceRoomService, BookingService bookingService, ParticipantService participantService){
        this.conferenceRoomService = conferenceRoomService;
        this.bookingService = bookingService;
        this.participantService = participantService;
    }

    @GetMapping("/conferences")
    public ResponseEntity<?> conferences(){
        var dtoToRet = new ArrayList<ConferenceRoomResponseDTO>();

        var conferences =  conferenceRoomService.conferences();


        for(ConferenceRoom conferenceRoom : conferences){
            var participants = conferenceRoom.getParticipants().
                    stream().map(s -> new ParticipantDTO(s.getId(), s.getFirstName(), s.getLastName())).collect(Collectors.toList());

            dtoToRet.add(new ConferenceRoomResponseDTO(
                    conferenceRoom.getId(),
                    conferenceRoom.getName(),
                    conferenceRoom.getTotalParticipant(),
                    conferenceRoom.getMaxCapacity(),
                    conferenceRoom.getBookings().stream().findFirst().get().getStartTime(),
                    conferenceRoom.getBookings().stream().findFirst().get().getEndTime(),
                    conferenceRoom.isIsCancelled(),
                    participants));
        }

        return new ResponseEntity<>(dtoToRet, HttpStatus.OK);
    }

    @GetMapping("/{roomId}/availability")
    public ResponseEntity<?> checkAvailability(@PathVariable("roomId") Long roomId,
                                                     @RequestParam("registeredParticipants") int registeredParticipants,
                                                     @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                     @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        var room = conferenceRoomService.findById(roomId);
        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(room.isIsCancelled()){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        var isParticipantsLimitReached = registeredParticipants > room.getMaxCapacity();
        if (isParticipantsLimitReached) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        var isTimeAvailable = conferenceRoomService.isTimeAvailable(room, startTime, endTime);
        return new ResponseEntity<>(isTimeAvailable
                ? new ResponseMessageDTO("Available")
                : new ResponseMessageDTO("Not available"), HttpStatus.OK);
    }

    @PostMapping("/{roomId}/cancel")
    public ResponseEntity<?> cancelConference(@PathVariable("roomId") Long roomId){
        var room = conferenceRoomService.findById(roomId);
        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        room.setIsCancelled(true);
        room.setName("Was_" + room.getName());
        conferenceRoomService.create(room);
        return new ResponseEntity<>(new ResponseMessageDTO("Successfully cancelled"), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> createConferenceRoom(@RequestBody ConferenceRoomDTO conferenceRoomDTO) {

        var conferencesByName = conferenceRoomService.findByName(conferenceRoomDTO.getName()); // Name is unique
        if(conferencesByName.size() > 0 && !conferencesByName.get(0).isIsCancelled()){ // Checking if conference exists and also is not cancelled
            return new ResponseEntity<>("Conference is already scheduled!", HttpStatus.BAD_REQUEST);
        }

        var conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName(conferenceRoomDTO.getName());
        conferenceRoom.setMaxCapacity(conferenceRoomDTO.getMaxCapacity());
        conferenceRoom.setTotalParticipant(conferenceRoomDTO.getParticipantDTO().size());
        ConferenceRoom createdRoom = conferenceRoomService.create(conferenceRoom);

        var booking = new Booking();
        booking.setStartTime(conferenceRoomDTO.getStartTime());
        booking.setEndTime(conferenceRoomDTO.getEndTime());
        booking.setConferenceRoom(conferenceRoom);
        bookingService.create(booking);

        var participantants = new ArrayList<Participant>();
        for(ParticipantDTO participantDTO: conferenceRoomDTO.getParticipantDTO()){
            var participant = new Participant();
            participant.setFirstName(participantDTO.getFirstName());
            participant.setLastName(participantDTO.getLastName());
            participant.setConferenceRoom(conferenceRoom);
            participantants.add(participant);
        }
        participantService.saveAll(participantants);

        return new ResponseEntity<ConferenceRoom>(createdRoom, HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}/register")
    public ResponseEntity<?> registerParticipant(@PathVariable("roomId") Long roomId,
                                                 @RequestBody List<ParticipantDTO> request){
        var room = conferenceRoomService.findById(roomId);
        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if((room.getTotalParticipant() + request.size() > room.getMaxCapacity())){
            return new ResponseEntity<>(new ResponseMessageDTO("No available seats! left:" + (room.getMaxCapacity() -room.getTotalParticipant())), HttpStatus.BAD_REQUEST);
        }

        var numberOfParticipants = request.size();
        room.setTotalParticipant(room.getTotalParticipant() + numberOfParticipants );
        conferenceRoomService.create(room);

        var participantants = new ArrayList<Participant>();
        for(ParticipantDTO participantDTO: request){
            var participant = new Participant();
            participant.setFirstName(participantDTO.getFirstName());
            participant.setLastName(participantDTO.getLastName());
            participant.setConferenceRoom(room);
            participantants.add(participant);
        }

        return new ResponseEntity<>(new ResponseMessageDTO("users successfully added!"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{roomId}/delete")
    public ResponseEntity<?> deleteParticipant(@PathVariable("roomId") Long roomId,
                                                 @RequestParam("participantId") Long participantId){

        var room = conferenceRoomService.findById(roomId);
        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var participant = participantService.findById(participantId);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        room.setTotalParticipant(room.getTotalParticipant() - 1);
        conferenceRoomService.create(room);

        participantService.deleteById(participantId);
        return new ResponseEntity<>(new ResponseMessageDTO("user successfully deleted!"), HttpStatus.CREATED);

    }



}
