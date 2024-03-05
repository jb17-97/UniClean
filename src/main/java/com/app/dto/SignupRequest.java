package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String gender;

    private String address;
    private String postCode;
    private String city;
    private String telephone;

    private String email;
    private String password;
    private String registrationType;
}
