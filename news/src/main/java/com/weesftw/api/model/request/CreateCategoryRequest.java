package com.weesftw.api.model.request;

import com.weesftw.common.context.Introspected;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Categories cannot contain words with spaces.")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;
}
