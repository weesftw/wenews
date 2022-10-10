package com.weesftw.api.service;

import com.weesftw.api.model.Category;

import java.util.List;

public interface CategoryService {

    Category getCategory(String name);
    List<Category> getAll();
}
