package com.weesftw.api.service;

import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;

import java.util.List;

public interface NewsService {

    List<News> getNews();
    List<News> getNews(String category);
    News saveNews(CreateNewsRequest createNewsRequest);
}
