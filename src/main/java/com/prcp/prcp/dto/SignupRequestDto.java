package com.prcp.prcp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

    private String username;

    private String firstName;

    private String lastName;

    private String state;

    private String email;

    private String password;

    private String confirmPassword;

    private String countryCode;

    private String role;
}
