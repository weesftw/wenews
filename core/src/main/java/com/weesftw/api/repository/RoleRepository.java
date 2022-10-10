package com.weesftw.api.repository;

import com.weesftw.api.model.Role;

import java.sql.SQLException;

public interface RoleRepository {

    Role get(int id) throws SQLException;
}
