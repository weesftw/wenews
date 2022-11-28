package com.weesftw.adapter.listener;

import com.weesftw.api.Observer;
import com.weesftw.api.model.News;

public interface Newsletter {

    void notifyObservers(News news);
    void register(Observer observer);
}
