package com.weesftw.api.service.impl;

import com.password4j.Password;
import com.weesftw.api.exception.PersistenceException;
import com.weesftw.api.model.User;
import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.repository.UserRepository;
import com.weesftw.api.service.UserService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Singleton
public class DefaultUserService implements UserService {

    private final UserRepository repository;

    public DefaultUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User credentialsIsValid(String username, String password) {
        var user = getUser(username);
        if(user != null) {
            var result = decrypt(password, user.getPassword());
            return result ? user : null;
        }

        return null;
    }

    @Override
    public User getUser(String username) {
        try {
            return repository.get(username);
        } catch(SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public User createUser(CreateUserRequest user) {
        try {
            user.setPassword(encrypt(user.getPassword()));
            repository.create(user);

            return User.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();
        } catch(SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<CreateUserRequest> getUsers() {
        // TODO: make pagination when returns all users
        return Collections.emptyList();
    }

    private String encrypt(String password) {
        return Password.hash(password).withBcrypt().getResult();
    }

    private boolean decrypt(String plain, String encrypted) {
        return Password.check(plain, encrypted).withBcrypt();
    }
}
