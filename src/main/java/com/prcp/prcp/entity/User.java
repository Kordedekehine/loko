package com.prcp.prcp.entity;

import com.prcp.prcp.enums.Roles;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String state;

    private String email;

    private String password;
    @Transient
    private String confirmPassword;

    private String countryCode;

    @Enumerated(EnumType.STRING)
    private Roles roles;

}