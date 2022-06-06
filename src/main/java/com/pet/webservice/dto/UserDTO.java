package com.pet.webservice.dto;

/* DTO - Data Transfer Object. This is an object that stores data in itself
* and transmit data to the client */

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {

    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String username;
    private String bio;
}
