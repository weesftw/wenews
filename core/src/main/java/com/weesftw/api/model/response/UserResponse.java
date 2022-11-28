package com.weesftw.api.model.response;

import com.weesftw.api.model.Role;
import com.weesftw.common.context.Introspected;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Introspected
public class UserResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String imageUrl;
    private boolean banned;
    private List<Role> roles;
}
