package com.weesftw.api.repository;

import com.weesftw.api.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryRepository {

    Category get(String name) throws SQLException;
    List<Category> getAll() throws SQLException;
}
