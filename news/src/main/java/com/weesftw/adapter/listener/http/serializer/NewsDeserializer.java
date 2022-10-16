package com.weesftw.adapter.listener.http.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.weesftw.api.model.request.CreateNewsRequest;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NewsDeserializer extends StdDeserializer<CreateNewsRequest> {

    public NewsDeserializer() {
        this(CreateNewsRequest.class);
    }

    public NewsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CreateNewsRequest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        ArrayNode articles = (ArrayNode) node.path("articles");
        for (JsonNode article : articles) {
            var author = article.path("author").asText();
            var title = article.path("title").asText();
            var description = article.path("description").asText();
            var url = article.path("url").asText();
            var urlToImage = article.path("urlToImage").asText();
            var publishedAt = article.path("publishedAt").asText();

            return CreateNewsRequest.builder()
                    .author(author)
                    .title(title)
                    .description(description)
                    .url(url)
                    .urlToImage(urlToImage)
                    .publishedAt(ZonedDateTime.parse(publishedAt)
                            .withZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                            .toLocalDateTime())
                    .build();
        }

        return null;
    }
}
