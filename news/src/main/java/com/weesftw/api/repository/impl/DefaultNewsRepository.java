package com.weesftw.api.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.repository.NewsRepository;
import jakarta.inject.Singleton;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Singleton
public class DefaultNewsRepository implements NewsRepository {

    private final ObjectMapper mapper = new ObjectMapper();
    private final MongoCollection<Document> collection;

    public DefaultNewsRepository(MongoDatabase database) {
        this.collection = database.getCollection("news");
    }

    @Override
    public List<News> getNews() {
        var news = new ArrayList<News>();
        collection.find().forEach((Consumer<? super Document>) document -> {
            try {
                mapper.readValue(document.toJson(), News.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        return news;
    }

    @Override
    public List<News> getNews(String category) {
        var news = new ArrayList<News>();
        collection.find(and(eq("category", category))).forEach((Consumer<? super Document>) doc -> {
            try {
                news.add(mapper.readValue(doc.toJson(), News.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        return news;
    }

    @Override
    public News saveNews(CreateNewsRequest news) {
        try {
            var document = Document.parse(mapper.writeValueAsString(news));
            var objectId = document.getObjectId("_id");
            collection.insertOne(document);

            return News.builder()
                    .id(objectId.toHexString())
                    .title(news.getTitle())
                    .description(news.getDescription())
                    .pubDate(news.getPubDate())
                    .link(news.getLink())
                    .image(news.getImage())
                    .category(news.getCategory())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
