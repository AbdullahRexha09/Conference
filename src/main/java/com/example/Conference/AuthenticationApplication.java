package com.example.Conference;

import com.example.Conference.domain.*;
import com.example.Conference.service.BookingService;
import com.example.Conference.service.ConferenceRoomService;
import com.example.Conference.service.ParticipantService;
import com.example.Conference.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class AuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}
	@Autowired
	UserService userService;

	@Autowired
	ConferenceRoomService conferenceRoomService;

	@Autowired
	BookingService bookingService;

	@Autowired
	ParticipantService participantService;

	// feeding database with initial data
	// Adding roles and mapping  users with specific roles
	@Bean
	CommandLineRunner run(UserService userService){
		return orgs ->{
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));


			userService.saveUser(new User(null,"Abdullah Rexha","Abdullah","1234", new ArrayList<>()));
			userService.saveUser(new User(null,"Shpetim Rexha","Shpetim","1234", new ArrayList<>()));

			userService.addRoleToUser("Abdullah","ROLE_USER");
			userService.addRoleToUser("Abdullah","ROLE_MANAGER");

			userService.addRoleToUser("Shpetim","ROLE_ADMIN");

			var addedConference = conferenceRoomService.create(new ConferenceRoom(0L,"Conference Room_01", 2, 10, false, null, null));
			var participants = Arrays.asList(
					new Participant(0L, "John", "Doe", addedConference),
					new Participant(0L, "Jane", "Smith", addedConference)
			);

			participantService.saveAll(participants);
			bookingService.create(new Booking(0L, LocalDateTime.parse("2022-01-01T10:00:00"), LocalDateTime.parse("2022-01-01T12:00:00"),addedConference));


		};
	}

}
