package com.weesftw.adapter.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.weesftw.adapter.client.HttpNewsClient;
import com.weesftw.adapter.client.serializer.NewsDeserializer;
import com.weesftw.api.model.News;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static java.time.Duration.ofSeconds;

@Slf4j
@Singleton
public class DefaultHttpNewsClient implements HttpNewsClient {

    private final ObjectMapper mapper;
    private final String newsUrl;

    public DefaultHttpNewsClient(Config config, ObjectMapper mapper) {
        this.newsUrl = config.get("NEWS_API_URL");
        this.mapper = mapper.registerModule(new SimpleModule().addDeserializer(List.class, new NewsDeserializer()));
    }

    @Override
    public List<News> getNews(String category) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .timeout(ofSeconds(10))
                .uri(URI.create(format(newsUrl.concat(format("/v1/news/%s?size=%s", category, 6))))) // gRPC maybe? :D
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200)
                return mapper.readValue(response.body(), List.class);

            log.warn(format("microservice `news` returned %s status code, requests has been refused.", response.statusCode()));
        } catch (IOException e) {
            log.warn("`news` throws exception: ", e);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }

        return Collections.emptyList();
    }
}
