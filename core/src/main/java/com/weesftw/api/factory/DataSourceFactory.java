package com.weesftw.api.factory;

import com.weesftw.common.Config;
import com.weesftw.common.context.Factory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;

import javax.sql.DataSource;

@Factory
public class DataSourceFactory {

    private final Config config;

    public DataSourceFactory(Config config) {
        this.config = config;
    }

    @Singleton
    public DataSource getDataSource(@Nullable MeterRegistry registry) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("datasource");
        hikariConfig.setMetricRegistry(registry);
        hikariConfig.setJdbcUrl(config.get("ds.url"));
        hikariConfig.setUsername(config.get("DS_USER"));
        hikariConfig.setPassword(config.get("DS_PASSWD"));

        return new HikariDataSource(hikariConfig);
    }
}
