package com.weesftw.api.repository;

import com.weesftw.api.model.Category;
import com.weesftw.api.model.request.CreateCategoryRequest;

import java.sql.SQLException;
import java.util.List;

public interface CategoryRepository {

    Category get(String name) throws SQLException;
    Category getExternal(String name) throws SQLException;
    List<Category> getAll() throws SQLException;
    List<Category> getAllExternal() throws SQLException;
    boolean create(CreateCategoryRequest category) throws SQLException;
}
