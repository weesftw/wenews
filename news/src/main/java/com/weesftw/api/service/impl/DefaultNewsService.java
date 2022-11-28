package com.weesftw.api.service.impl;

import com.weesftw.api.exception.CategoryNotFoundException;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.api.service.CategoryService;
import com.weesftw.api.service.NewsService;
import jakarta.inject.Singleton;

import java.util.List;

import static java.lang.String.format;

@Singleton
public class DefaultNewsService implements NewsService {

    private final NewsRepository repository;
    private final CategoryService categoryService;

    public DefaultNewsService(NewsRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    @Override
    public List<News> getNews() {
        return repository.getNews();
    }

    @Override
    public News get(String title) {
        return repository.get(title);
    }

    @Override
    public List<News> getNews(String category, int size) {
        return repository.getNews(category, size);
    }

    @Override
    public News saveNews(CreateNewsRequest createNewsRequest) {
        if(categoryService.getCategory(createNewsRequest.getCategory()) == null)
            throw new CategoryNotFoundException(format("Category %s not found", createNewsRequest.getCategory()));

        return repository.saveNews(createNewsRequest);
    }
}
