package com.weesftw.api.repository.impl;

import com.weesftw.api.model.Category;
import com.weesftw.api.model.request.CreateCategoryRequest;
import com.weesftw.api.repository.CategoryRepository;
import com.weesftw.common.Config;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DefaultCategoryRepository implements CategoryRepository {

    private final DataSource dataSource;

    private final String getCategory;
    private final String getExternalCategory;
    private final String getExternalCategories;
    private final String getCategories;
    private final String createCategory;

    public DefaultCategoryRepository(DataSource dataSource, Config envs) {
        this.dataSource = dataSource;
        this.getCategory = envs.get("/sql/get_category.sql");
        this.getExternalCategory = envs.get("/sql/get_external_category.sql");
        this.getExternalCategories = envs.get("/sql/get_external_categories.sql");
        this.getCategories = envs.get("/sql/get_categories.sql");
        this.createCategory = envs.get("/sql/create_category.sql");

    }

    @Override
    public Category get(String name) throws SQLException {
        return getCategory(name, getCategory);
    }

    @Override
    public Category getExternal(String name) throws SQLException {
        return getCategory(name, getExternalCategory);
    }

    @Nullable
    private Category getCategory(String name, String getExternalCategory) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getExternalCategory)) {
            stmt.setString(1, name);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return Category.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .isPublic(rs.getBoolean("is_public"))
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
        return getCategories(getCategories);
    }

    @Override
    public List<Category> getAllExternal() throws SQLException {
        return getCategories(getExternalCategories);
    }

    private List<Category> getCategories(String query) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(query)) {

            var categories = new ArrayList<Category>();
            var rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(Category.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .isPublic(rs.getBoolean("is_public"))
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

    @Override
    public boolean create(CreateCategoryRequest category) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(createCategory)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getPassword());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
