package com.weesftw.adapter.client.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.weesftw.api.model.News;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NewsDeserializer extends StdDeserializer<List<News>> {

    public NewsDeserializer() {
        this(List.class);
    }

    public NewsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<News> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var arr = new ArrayList<News>();

        ArrayNode node = p.getCodec().readTree(p);

        for (JsonNode article : node) {
            var id = article.path("id").asText();
            var category = article.path("category").asText();
            var author = article.path("author").asText();
            var title = article.path("title").asText();
            var description = article.path("description").asText();
            var url = article.path("url").asText();
            var urlToImage = article.path("urlToImage").asText();
            var publishedAt = article.path("publishedAt").asText();

            arr.add(News.builder()
                    .id(id)
                    .category(category)
                    .author(author)
                    .title(title)
                    .description(description)
                    .url(url)
                    .urlToImage(urlToImage)
                    .publishedAt(ZonedDateTime.parse(publishedAt.concat("Z"))
                            .withZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                            .toLocalDateTime().toString())
                    .build());
        }

        log.info("news has been deserialized! size: {}", arr.size());
        return arr;
    }
}
