package com.weesftw.api.repository;

import com.weesftw.api.model.User;
import com.weesftw.api.model.request.CreateUserRequest;

import java.sql.SQLException;

public interface UserRepository {

    User get(String username) throws SQLException;
    boolean create(CreateUserRequest request) throws SQLException;
}
