package com.weesftw.api.service.impl;

import com.weesftw.api.exception.PersistenceException;
import com.weesftw.api.model.Category;
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
            return repository.get(name);
        } catch(SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<Category> getAll() {
        try {
            return repository.getAll();
        } catch(SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
