package com.weesftw.api.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.weesftw.api.exception.NewsAlreadyExistsException;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;
import org.bson.Document;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static java.lang.String.format;

@Singleton
public class DefaultNewsRepository implements NewsRepository {

    private final ObjectMapper mapper;
    private final MongoCollection<Document> collection;

    public DefaultNewsRepository(MongoDatabase database, ObjectMapper mapper) {
        this.mapper = mapper;
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
    public News get(String title) {
        var document = collection.find(and(eq("title", title))).first();
        try {
            if(document != null) {
                var map = mapper.readValue(document.toJson(), CreateNewsRequest.class);
                return News.builder()
                        .id(document.getObjectId("_id").toString())
                        .title(map.getTitle())
                        .description(map.getDescription())
                        .url(map.getUrl())
                        .author(map.getAuthor())
                        .category(map.getCategory())
                        .publishedAt(map.getPublishedAt())
                        .build();
            }

            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<News> getNews(String category) {
        var news = new ArrayList<com.weesftw.api.model.News>();
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
    public News saveNews(CreateNewsRequest createNewsRequest) {
        try {
            if(!exists(createNewsRequest.getTitle())) {
                var document = Document.parse(mapper.writeValueAsString(createNewsRequest));
                collection.insertOne(document);
                var objectId = document.getObjectId("_id");

                return News.builder()
                        .id(objectId.toHexString())
                        .title(createNewsRequest.getTitle())
                        .description(createNewsRequest.getDescription())
                        .publishedAt(createNewsRequest.getPublishedAt())
                        .url(createNewsRequest.getUrl())
                        .imageToUrl(createNewsRequest.getUrlToImage())
                        .category(createNewsRequest.getCategory())
                        .build();
            }

            throw new NewsAlreadyExistsException(format("News %s already exists", createNewsRequest.getTitle()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean exists(String title) {
        return collection.countDocuments(and(eq("title", title))) > 0;
    }

    @Singleton
    public static final class DefaultOutboxNewsRepository implements NewsRepository.OutboxNewsRepository {

        private final DataSource dataSource;

        private final String checkNews;
        private final String insertNews;

        public DefaultOutboxNewsRepository(DataSource dataSource, Config config) {
            this.dataSource = dataSource;
            this.checkNews = config.get("/sql/check_news.sql");
            this.insertNews = config.get("/sql/insert_news.sql");
        }

        @Override
        public boolean isCommited(String newsId) throws SQLException {
            try (var connection = dataSource.getConnection();
                 var stmt = connection.prepareStatement(checkNews)) {
                stmt.setString(1, newsId);

                var rs = stmt.executeQuery();
                while(rs.next()) {
                    var hasId = rs.getString("news_id");
                    if(hasId != null)
                        return true;
                }
                return false;
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }

        @Override
        public boolean commit(News news) throws SQLException {
            try (var connection = dataSource.getConnection();
                 var stmt = connection.prepareStatement(insertNews)) {
                stmt.setString(1, news.getId());

                stmt.execute();
                return true;
            } catch(SQLException e) {
                throw new SQLException(e);
            }
        }
    }
}
