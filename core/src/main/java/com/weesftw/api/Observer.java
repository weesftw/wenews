package com.weesftw.api;

import com.weesftw.api.model.News;

public interface Observer {

    void update(News news);
}
