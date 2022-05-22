package com.pet.webservice.payload.response;

import lombok.Getter;

/* when a 401 error occurs, this object will be created  */
@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }
}
