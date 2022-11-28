package com.weesftw.api.repository.impl;

import com.weesftw.api.model.User;
import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.repository.RoleRepository;
import com.weesftw.api.repository.UserRepository;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.isDigits;

@Singleton
public class DefaultUserRepository implements UserRepository {

    private final DataSource dataSource;
    private final RoleRepository roleRepository;

    private final String banUser;
    private final String getUser;
    private final String getUsers;
    private final String createUser;
    private final String getUserId;
    private final String getUserRoles;
    private final String getUserCredentials;
    private final String checkUsername;

    public DefaultUserRepository(Config envs, DataSource dataSource, RoleRepository roleRepository) {
        this.dataSource = dataSource;
        this.roleRepository = roleRepository;
        this.getUser = envs.get("/SQL/get_user.sql");
        this.banUser = envs.get("/SQL/update_user_ban.sql");
        this.getUsers = envs.get("/SQL/get_users.sql");
        this.createUser = envs.get("/SQL/create_user.sql");
        this.getUserId = envs.get("/SQL/get_user_id.sql");
        this.getUserRoles = envs.get("/SQL/get_user_roles.sql");
        this.getUserCredentials = envs.get("/SQL/get_user_credentials.sql");
        this.checkUsername = envs.get("/SQL/check_username.sql");
    }

    @Override
    public boolean userAlreadyExists(String username) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(checkUsername)) {
            stmt.setString(1, username);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return rs.getBoolean("has");
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return false;
    }

    @Override
    public boolean banUser(String username) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(banUser)) {
            stmt.setString(1, username);

            stmt.execute();
            return true;
        } catch(SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<User> getUsers() throws SQLException {
        var arr = new ArrayList<User>();

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getUsers)) {

            var rs = stmt.executeQuery();
            while(rs.next()) {
                arr.add(User.builder()
                        .id(rs.getInt("id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .email(rs.getString("email"))
                        .imageUrl(rs.getString("image_url"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .banned(rs.getBoolean("banned"))
                        .lastActivity(rs.getTimestamp("last_activity").toLocalDateTime())
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .roles(roleRepository.get(rs.getInt("role_id")))
                        .build());
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return arr;
    }

    @Override
    public List<String> getRoles(String username) throws SQLException {
        var arr = new ArrayList<String>();

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getUserRoles)) {
            stmt.setString(1, username);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                arr.add(rs.getString("name"));
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return arr;
    }

    @Override
    public User get(String username) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(isDigits(username) ? getUserId : getUser)) {
            stmt.setString(1, username);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return User.builder()
                        .id(rs.getInt("id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .email(rs.getString("email"))
                        .imageUrl(rs.getString("image_url"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .banned(rs.getBoolean("banned"))
                        .lastActivity(rs.getTimestamp("last_activity").toLocalDateTime())
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .roles(roleRepository.get(rs.getInt("role_id")))
                        .build();
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return null;
    }

    @Override
    public String getCredentials(String username) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getUserCredentials)) {
            stmt.setString(1, username);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return rs.getString("password");
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return null;
    }

    @Override
    public boolean create(CreateUserRequest request) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(createUser)) {
            stmt.setString(1, request.getFirstName());
            stmt.setString(2, request.getLastName());
            stmt.setString(3, request.getEmail());
            stmt.setString(4, request.getImageUrl());
            stmt.setString(5, request.getUsername());
            stmt.setString(6, request.getPassword());
            stmt.setInt(7, 1);

            stmt.execute();
            return true;
        } catch(SQLException e) {
            throw new SQLException(e);
        }
    }
}
