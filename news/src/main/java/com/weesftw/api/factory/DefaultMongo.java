package com.weesftw.api.factory;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.weesftw.Config;
import com.weesftw.context.Factory;
import jakarta.inject.Singleton;

@Factory
public class DefaultMongo {

    private final Config config;

    public DefaultMongo(Config config) {
        this.config = config;
    }

    @Singleton
    public MongoDatabase mongoClient() {
        var connection = MongoClients.create(config.get("MONGO_URI"));
        return connection.getDatabase(config.get("mongo.database"));
    }
}
