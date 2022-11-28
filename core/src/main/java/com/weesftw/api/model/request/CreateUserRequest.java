package com.weesftw.api.model.request;

import io.micronaut.core.annotation.Introspected;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Introspected
public class CreateUserRequest {

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @NotNull(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one number and one special character")
    private String password;

    @Pattern(regexp = "(https?:\\/\\/.*\\.(?:png|jpg))", message = "Image URL must be valid")
    private String imageUrl;
}
