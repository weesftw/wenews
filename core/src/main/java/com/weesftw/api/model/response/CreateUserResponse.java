package com.weesftw.api.model.response;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class CreateUserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
