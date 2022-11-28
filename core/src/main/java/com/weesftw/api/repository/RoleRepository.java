package com.weesftw.api.repository;

import com.weesftw.api.model.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleRepository {

    List<Role> get(int id) throws SQLException;
}
