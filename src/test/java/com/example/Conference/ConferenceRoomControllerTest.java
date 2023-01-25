package com.example.Conference;

import com.example.Conference.domain.ConferenceRoom;
import com.example.Conference.dto.ConferenceRoomDTO;
import com.example.Conference.dto.ParticipantDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest

public class ConferenceRoomControllerTest {
    final String baseURI = "http://localhost:8080";
    private String getJWTToken(){
        RestAssured.baseURI = baseURI;
        String loginJson = "{\"username\":\"Shpetim\",\"password\":\"1234\"}"; // Admin role

        Response loginResponse = RestAssured.given()
                .contentType("application/json")
                .body(loginJson)
                .post("/api/v1/user/authenticate");
        return loginResponse.jsonPath().getString("token");
    }
    private ConferenceRoom CreateConference(){
        ConferenceRoomDTO conferenceRoomDTO = new ConferenceRoomDTO();
        conferenceRoomDTO.setName("Conference Room 1");
        conferenceRoomDTO.setMaxCapacity(100);
        conferenceRoomDTO.setStartTime(LocalDateTime.parse("2022-01-01T10:00:00"));
        conferenceRoomDTO.setEndTime(LocalDateTime.parse("2022-01-01T12:00:00"));

        ParticipantDTO participantDTO1 = new ParticipantDTO();
        participantDTO1.setFirstName("John");
        participantDTO1.setLastName("Doe");

        ParticipantDTO participantDTO2 = new ParticipantDTO();
        participantDTO2.setFirstName("Jane");
        participantDTO2.setLastName("Smith");

        conferenceRoomDTO.setParticipantDTO(Arrays.asList(participantDTO1, participantDTO2));

        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .contentType("application/json")
                .body(conferenceRoomDTO)
                .post("/api/v1/conference/create");

        return response.getBody().as(ConferenceRoom.class);
    }
    @Test
    public void checkAvailabilityTest() {
        Long roomId = 1L;
        int registeredParticipants = 50;
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 17, 0);

        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .queryParam("registeredParticipants", registeredParticipants)
                .queryParam("startTime", startTime)
                .queryParam("endTime", endTime)
                .get("/"+ roomId +"/availability");

        response.then()
                .statusCode(200)
                .body("availability", equalTo(true));
    }

    @Test
    public void cancelConferenceTest() {

        var room = CreateConference();
        Long roomId = room.getId();

        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .post("/"+ roomId +"/cancel");

        response.then()
                .statusCode(200)
                .body("cancelled", equalTo(true));
    }

    @Test
    public void createConferenceRoomTest() {

        ConferenceRoomDTO conferenceRoomDTO = new ConferenceRoomDTO();
        conferenceRoomDTO.setName("Conference Room 1");
        conferenceRoomDTO.setMaxCapacity(100);
        conferenceRoomDTO.setStartTime(LocalDateTime.parse("2022-01-01T10:00:00"));
        conferenceRoomDTO.setEndTime(LocalDateTime.parse("2022-01-01T12:00:00"));

        ParticipantDTO participantDTO1 = new ParticipantDTO();
        participantDTO1.setFirstName("John");
        participantDTO1.setLastName("Doe");

        ParticipantDTO participantDTO2 = new ParticipantDTO();
        participantDTO2.setFirstName("Jane");
        participantDTO2.setLastName("Smith");

        conferenceRoomDTO.setParticipantDTO(Arrays.asList(participantDTO1, participantDTO2));

        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .contentType("application/json")
                .body(conferenceRoomDTO)
                .post("/api/v1/conference/create");

        response.then()
                .statusCode(201)
                .body("name", equalTo("Conference Room 1"))
                .body("maxCapacity", equalTo(100))
                .body("totalParticipant", equalTo(2));

    }

    @Test
    public void registerParticipantTest() {
        Long roomId = 1L;
        ParticipantDTO participantDTO1 = new ParticipantDTO();
        participantDTO1.setFirstName("John");
        participantDTO1.setLastName("Doe");

        ParticipantDTO participantDTO2 = new ParticipantDTO();
        participantDTO2.setFirstName("Jane");
        participantDTO2.setLastName("Smith");

        List<ParticipantDTO> request = Arrays.asList(participantDTO1, participantDTO2);

        RestAssured.baseURI = "http://localhost:8080";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .contentType("application/json")
                .body(request)
                .post("/"+ roomId +"/register");

        response.then()
                .statusCode(201)
                .body("message", equalTo("users successfully added!"));
    }

    @Test
    public void deleteParticipantTest() {
        Long roomId = 1L;
        Long participantId = 1L;

        RestAssured.baseURI = "http://localhost:8080";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .queryParam("participantId", participantId)
                .delete("/"+ roomId +"/delete");

        response.then()
                .statusCode(201)
                .body("message", equalTo("user successfully deleted!"));
    }
}
