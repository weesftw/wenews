package com.weesftw.adapter.stream.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.weesftw.api.model.request.CreateNewsRequest;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsDeserializer extends StdDeserializer<List<CreateNewsRequest>> {

    public NewsDeserializer() {
        this(CreateNewsRequest.class);
    }

    public NewsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<CreateNewsRequest> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var arr = new ArrayList<CreateNewsRequest>();

        JsonNode node = p.getCodec().readTree(p);
        var articles = (ArrayNode) node.path("articles");
        for (JsonNode article : articles) {
            var author = article.path("author").textValue();
            var title = article.path("title").textValue();
            var description = article.path("description").textValue();
            var url = article.path("url").textValue();
            var urlToImage = article.path("urlToImage").textValue();
            var publishedAt = article.path("publishedAt").textValue();

            arr.add(CreateNewsRequest.builder()
                    .author(author)
                    .title(title)
                    .description(description)
                    .url(url)
                    .urlToImage(urlToImage)
                    .publishedAt(ZonedDateTime.parse(publishedAt)
                            .withZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                            .toLocalDateTime())
                    .build());
        }

        return arr;
    }
}
