package com.weesftw.api.service.impl;

import com.password4j.Password;
import com.weesftw.api.exception.PersistenceException;
import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.model.response.UserResponse;
import com.weesftw.api.repository.UserRepository;
import com.weesftw.api.service.UserService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class DefaultUserService implements UserService {

    private final UserRepository repository;

    public DefaultUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean credentialsIsValid(String username, String password) {
        try {
            var credentials = repository.getCredentials(username);
            if (credentials != null)
                return decrypt(password, credentials);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return false;
    }

    @Override
    public boolean userAlreadyExists(String username) {
        return false;
    }

    @Override
    public List<String> getRoles(String username) {
        try {
            return repository.getRoles(username);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public UserResponse getUser(String username) {
        try {
            var user = repository.get(username);
            return UserResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .banned(user.isBanned())
                    .username(user.getUsername())
                    .imageUrl(user.getImageUrl())
                    .roles(user.getRoles())
                    .build();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public UserResponse createUser(CreateUserRequest user) {
        try {
            if (repository.userAlreadyExists(user.getUsername()))
                throw new PersistenceException("User already exists, please choose another username");

            if (user.getImageUrl() == null)
                user.setImageUrl("https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg");

            user.setPassword(encrypt(user.getPassword()));
            if (repository.create(user))
                return UserResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .imageUrl(user.getImageUrl())
                        .build();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return null;
    }

    @Override
    public boolean banUser(String username) {
        try {
            log.warn("Banning user {}", username);
            return repository.banUser(username);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<UserResponse> getUsers() {  // TODO: make pagination when returns all users
        try {
            var response = new ArrayList<UserResponse>();
            repository.getUsers().forEach(user -> response.add(UserResponse.builder()
                    .id(user.getId())
                    .banned(user.isBanned())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .imageUrl(user.getImageUrl())
                    .roles(user.getRoles())
                    .build()));

            return response;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private String encrypt(String password) {
        return Password.hash(password).withBcrypt().getResult();
    }

    private boolean decrypt(String plain, String encrypted) {
        return Password.check(plain, encrypted).withBcrypt();
    }
}
