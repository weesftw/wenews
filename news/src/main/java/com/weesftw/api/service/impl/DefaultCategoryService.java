package com.weesftw.api.service.impl;

import com.weesftw.api.exception.CategoryAlreadyExistsException;
import com.weesftw.api.model.Category;
import com.weesftw.api.model.request.CreateCategoryRequest;
import com.weesftw.api.repository.CategoryRepository;
import com.weesftw.api.service.CategoryService;
import jakarta.inject.Singleton;

import java.sql.SQLException;
import java.util.List;

@Singleton
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository repository;

    public DefaultCategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category getCategory(String name) {
        try {
            var category = repository.get(name);
            return category == null ? repository.getExternal(name) : category;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> getAll() {
        try {
            return repository.getAll();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> getAllExternal() {
        try {
            return repository.getAllExternal();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCategory(CreateCategoryRequest category) {
        try {
            if(getCategory(category.getName()) != null)
                throw new CategoryAlreadyExistsException("Category already exists");

            repository.create(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
