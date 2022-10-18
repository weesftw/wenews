package com.weesftw.api.repository.impl;

import com.weesftw.api.model.Category;
import com.weesftw.api.repository.CategoryRepository;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DefaultCategoryRepository implements CategoryRepository {

    private final DataSource dataSource;

    private final String getCategory;
    private final String getCategories;

    public DefaultCategoryRepository(DataSource dataSource, Config envs) {
        this.dataSource = dataSource;
        this.getCategory = envs.get("/sql/get_category.sql");
        this.getCategories = envs.get("/sql/get_categories.sql");
    }

    @Override
    public Category get(String name) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getCategory)) {
            stmt.setString(1, name);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return Category.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .isPublic(rs.getBoolean("public"))
                        .enabled(rs.getBoolean("enabled"))
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return null;
    }

    @Override
    public List<Category> getAll() throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getCategories)) {

            var categories = new ArrayList<Category>();
            var rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(Category.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .isPublic(rs.getBoolean("public"))
                        .enabled(rs.getBoolean("enabled"))
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }

            return categories;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
