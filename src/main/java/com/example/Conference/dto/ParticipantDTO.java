package com.example.Conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {

    private Long Id;
    private String firstName;

    private String lastName;

}
