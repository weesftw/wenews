package com.weesftw.api.repository.impl;

import com.weesftw.api.model.User;
import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.repository.RoleRepository;
import com.weesftw.api.repository.UserRepository;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.apache.commons.lang3.math.NumberUtils.isDigits;

@Singleton
public class DefaultUserRepository implements UserRepository {

    private final DataSource dataSource;
    private final RoleRepository roleRepository;

    private final String getUser;
    private final String createUser;
    private final String getUserId;

    public DefaultUserRepository(Config envs, DataSource dataSource, RoleRepository roleRepository) {
        this.dataSource = dataSource;
        this.roleRepository = roleRepository;
        this.getUser = envs.get("/SQL/get_user.sql");
        this.createUser = envs.get("/SQL/create_user.sql");
        this.getUserId = envs.get("/SQL/get_user_id.sql");
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
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .role(roleRepository.get(rs.getInt("role_id")))
                        .build();
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
            stmt.setString(4, request.getUsername());
            stmt.setString(5, request.getPassword());
            stmt.setInt(6, 1);

            stmt.execute();
            return true;
        } catch(SQLException e) {
            throw new SQLException(e);
        }
    }
}
