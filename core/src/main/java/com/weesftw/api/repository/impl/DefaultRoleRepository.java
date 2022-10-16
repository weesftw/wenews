package com.weesftw.api.repository.impl;

import com.weesftw.api.model.Role;
import com.weesftw.api.repository.RoleRepository;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;

@Singleton
public class DefaultRoleRepository implements RoleRepository {

    private final DataSource dataSource;

    private final String getRole;

    public DefaultRoleRepository(DataSource dataSource, Config envs) {
        this.dataSource = dataSource;
        this.getRole = envs.get("/SQL/get_role.sql");
    }

    @Override
    public Role get(int id) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getRole)) {
            stmt.setInt(1, id);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return Role.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return null;
    }
}
