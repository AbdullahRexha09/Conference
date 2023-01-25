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
    private String getConfereceId(){

        // Creating a conferenceRoom with 2 participants and specific startdate and end date
        // Every conferenceRoom has unique Id

        ConferenceRoomDTO conferenceRoomDTO = new ConferenceRoomDTO();
        conferenceRoomDTO.setName("Conference Room_"+ LocalDateTime.now());
        conferenceRoomDTO.setMaxCapacity(10);
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

        return response.jsonPath().getString("id");
    }


    @Test
    public void cancelConferenceTest() {

        var roomId = getConfereceId();
        System.out.println("bega:" + roomId);
        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .post("api/v1/conference/"+ roomId +"/cancel");

        response.then()
                .statusCode(200)
                .body("message", equalTo("Successfully cancelled"));
    }

    @Test
    public void createConferenceRoomTest() {

        ConferenceRoomDTO conferenceRoomDTO = new ConferenceRoomDTO();
        conferenceRoomDTO.setName("Conference Room");
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

        RestAssured.baseURI = "http://localhost:8080";

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
        var roomId = getConfereceId();
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
                .post("/api/v1/conference/"+ roomId +"/register");

        response.then()
                .statusCode(201)
                .body("message", equalTo("users successfully added!"));
    }

    @Test
    public void registerMoreThanCapacityParticipantTest() {
        var roomId = getConfereceId(); // maxcapacity is 10
        ParticipantDTO participantDTO1 = new ParticipantDTO();
        participantDTO1.setFirstName("Peter");
        participantDTO1.setLastName("Gas");

        ParticipantDTO participantDTO2 = new ParticipantDTO();
        participantDTO2.setFirstName("Mathew");
        participantDTO2.setLastName("Smith");

        ParticipantDTO participantDTO3 = new ParticipantDTO();
        participantDTO2.setFirstName("Elon");
        participantDTO2.setLastName("Barkley");

        ParticipantDTO participantDTO4 = new ParticipantDTO();
        participantDTO2.setFirstName("Elon");
        participantDTO2.setLastName("Marley");

        ParticipantDTO participantDTO5 = new ParticipantDTO();
        participantDTO2.setFirstName("Elizabeth");
        participantDTO2.setLastName("Mushica");

        ParticipantDTO participantDTO6 = new ParticipantDTO();
        participantDTO2.setFirstName("Elton");
        participantDTO2.setLastName("John");

        ParticipantDTO participantDTO7 = new ParticipantDTO();
        participantDTO2.setFirstName("Anwar");
        participantDTO2.setLastName("Yfter");

        ParticipantDTO participantDTO8 = new ParticipantDTO();
        participantDTO2.setFirstName("Hasan");
        participantDTO2.setLastName("Omer");

        ParticipantDTO participantDTO9 = new ParticipantDTO();
        participantDTO2.setFirstName("Ilas");
        participantDTO2.setLastName("Sova");

        List<ParticipantDTO> request = Arrays.asList(participantDTO1, participantDTO2, participantDTO3,
                participantDTO4,participantDTO5,participantDTO6,participantDTO7,participantDTO8,participantDTO9);

        RestAssured.baseURI = "http://localhost:8080";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .contentType("application/json")
                .body(request)
                .post("/api/v1/conference/"+ roomId +"/register");

        response.then()
                .statusCode(400)
                .body("message", equalTo("No available seats! left:8"));
    }

    @Test
    public void checkAvailability_WhenWeHaveConflictsOnTime_Test() {
        var roomId = getConfereceId();
        int registeredParticipants = 50;
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 11, 0);

        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .queryParam("registeredParticipants", registeredParticipants)
                .queryParam("startTime", startTime.toString())
                .queryParam("endTime", endTime.toString())
                .get("/api/v1/conference/"+ roomId +"/availability");

        response.then()
                .statusCode(200)
                .body("message", equalTo("Not available"));
    }
    @Test
    public void checkAvailability_WhenNoConflictsOnTime_Test() {
        var roomId = getConfereceId();
        int registeredParticipants = 50;
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 7, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 8, 0);

        RestAssured.baseURI = baseURI;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .queryParam("registeredParticipants", registeredParticipants)
                .queryParam("startTime", startTime.toString())
                .queryParam("endTime", endTime.toString())
                .get("/api/v1/conference/"+ roomId +"/availability");

        response.then()
                .statusCode(200)
                .body("message", equalTo("Available"));
    }


    /// Using default added ConferenceRoom provided on the bootrstap of application
    @Test
    public void deleteParticipantTest() {
        var roomId = 7L;
        Long participantId = 8L;

        RestAssured.baseURI = "http://localhost:8080";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + getJWTToken())
                .queryParam("participantId", participantId)
                .delete("/api/v1/conference/"+ roomId +"/delete");

        response.then()
                .statusCode(201)
                .body("message", equalTo("user successfully deleted!"));
    }

}
