package com.weesftw.api.factory;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.weesftw.common.Config;
import com.weesftw.common.context.Factory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Singleton;

import javax.sql.DataSource;

@Factory
public class DefaultDataSource {

    private final Config config;

    public DefaultDataSource(Config config) {
        this.config = config;
    }

    @Singleton
    public DataSource getDataSource() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("datasource");
        hikariConfig.setJdbcUrl(config.get("ds.url"));
        hikariConfig.setUsername(config.get("DS_USER"));
        hikariConfig.setPassword(config.get("DS_PASSWD"));

        return new HikariDataSource(hikariConfig);
    }

    @Singleton
    public MongoDatabase mongoClient() {
        var connection = MongoClients.create(config.get("MONGO_URI"));
        return connection.getDatabase(config.get("mongo.database"));
    }
}
