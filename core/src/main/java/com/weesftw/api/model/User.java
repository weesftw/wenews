package com.weesftw.api.model;

import com.weesftw.common.context.Introspected;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private String username;
    private String password;
    private List<Role> roles;
    private boolean banned;
    private LocalDateTime lastActivity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
