package com.weesftw.api.model.request;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class CreateUserRequest {

    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
    private String firstName;

    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one number and one special character")
    private String password;
}
