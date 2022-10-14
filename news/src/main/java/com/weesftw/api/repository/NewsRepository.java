package com.weesftw.api.repository;

import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;

import java.sql.SQLException;
import java.util.List;

public interface NewsRepository {

    List<News> getNews();
    List<News> getNews(String category);
    News saveNews(CreateNewsRequest news);

    interface OutboxNewsRepository {

        boolean isCommited(String newsId) throws SQLException;
        boolean commit(News news) throws SQLException;
    }
}
