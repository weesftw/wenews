package com.weesftw.api.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Token {

    private long id;
    private User user;
    private String refreshToken;
    private boolean revoked;
    private LocalDateTime createAt;
}
