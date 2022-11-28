package com.weesftw.api.service;

import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.model.response.UserResponse;

import java.util.List;

public interface UserService {

    boolean credentialsIsValid(String username, String password);

    boolean userAlreadyExists(String username);

    List<String> getRoles(String username);

    UserResponse getUser(String username);

    UserResponse createUser(CreateUserRequest user);

    boolean banUser(String username);

    List<UserResponse> getUsers();
}
