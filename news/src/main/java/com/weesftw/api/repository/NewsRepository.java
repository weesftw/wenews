package com.weesftw.api.repository;

import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;

import java.util.List;

public interface NewsRepository {

    List<News> getNews();
    List<News> getNews(String category);
    News saveNews(CreateNewsRequest news);
}
