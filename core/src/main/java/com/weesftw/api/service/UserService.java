package com.weesftw.api.service;

import com.weesftw.api.model.User;
import com.weesftw.api.model.request.CreateUserRequest;

import java.util.List;

public interface UserService {

    User credentialsIsValid(String username, String password);

    User getUser(String username);

    User createUser(CreateUserRequest user);

    List<CreateUserRequest> getUsers();
}
