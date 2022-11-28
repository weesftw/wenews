package com.weesftw.api.repository;

import com.weesftw.api.model.User;
import com.weesftw.api.model.request.CreateUserRequest;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    boolean userAlreadyExists(String username) throws SQLException;

    boolean banUser(String username) throws SQLException;

    List<User> getUsers() throws SQLException;

    List<String> getRoles(String username) throws SQLException;

    User get(String username) throws SQLException;

    String getCredentials(String username) throws SQLException;

    boolean create(CreateUserRequest request) throws SQLException;
}
