package com.weesftw.adapter.client;

import com.weesftw.api.model.News;

import java.util.List;

public interface HttpNewsClient {

    List<News> getNews(String category);
}
