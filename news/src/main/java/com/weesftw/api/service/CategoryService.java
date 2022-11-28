package com.weesftw.api.service;

import com.weesftw.api.model.Category;
import com.weesftw.api.model.request.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    Category getCategory(String name);
    List<Category> getAll();
    List<Category> getAllExternal();
    void createCategory(CreateCategoryRequest category);
}
