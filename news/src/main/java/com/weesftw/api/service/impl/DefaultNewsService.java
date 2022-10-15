package com.weesftw.api.service.impl;

import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.api.service.NewsService;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class DefaultNewsService implements NewsService {

    private final NewsRepository repository;

    public DefaultNewsService(NewsRepository repository) {
        this.repository = repository;
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
    public List<News> getNews(String category) {
        return repository.getNews(category);
    }

    @Override
    public News saveNews(CreateNewsRequest createNewsRequest) {
        return repository.saveNews(createNewsRequest);
    }
}
