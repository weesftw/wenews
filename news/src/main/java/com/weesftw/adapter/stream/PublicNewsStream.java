package com.weesftw.adapter.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.weesftw.adapter.stream.serializer.NewsDeserializer;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.CategoryService;
import com.weesftw.common.Config;
import com.weesftw.common.event.EventHandler;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@Singleton
public class PublicNewsStream {

    private final ObjectMapper mapper;
    private final Map<String, String> urls = new HashMap<>();
    private final EventHandler<CreateNewsRequest> e;

    public PublicNewsStream(Config config, CategoryService service, EventHandler<CreateNewsRequest> e, ObjectMapper mapper) {
        this.e = e;
        this.mapper = mapper.registerModule(new SimpleModule().addDeserializer(List.class, new NewsDeserializer()));

        var url = config.get("newsapi.url");
        service.getAll().forEach(category -> {
            var name = category.getName();
            if (name != null)
                urls.put(name, url.concat("&category=" + name));
        });
    }

    @Scheduled(fixedDelay = "5400s", initialDelay = "15s")
    public void getNews() {
        urls.forEach((category, url) -> {
            try {
                var client = HttpClient.newHttpClient();
                var request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .timeout(Duration.of(10, SECONDS))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    var news = mapper.readValue(response.body(), List.class);
                    if (news != null)
                        ((List<CreateNewsRequest>) news).forEach(n -> {
                            n.setCategory(category);
                            log.debug("News: {}", news);
                            e.handle(n);
                        });
                    else
                        log.warn(format("newsapi.org returned %s status code, requests has been refused.", response.statusCode()));
                }
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
