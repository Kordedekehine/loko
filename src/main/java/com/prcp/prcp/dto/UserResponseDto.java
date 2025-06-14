package com.prcp.prcp.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long Id;

    private String username;

    private String firstName;

    private String lastName;

    private String state;

    private String email;

    private String password;

    private String countryCode;

    private String role;
}
