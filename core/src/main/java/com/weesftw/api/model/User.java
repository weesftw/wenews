package com.weesftw.api.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Role role;
    private LocalDateTime lastActivy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
