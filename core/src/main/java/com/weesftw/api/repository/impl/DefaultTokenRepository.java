package com.weesftw.api.repository.impl;

import com.weesftw.api.model.Token;
import com.weesftw.api.repository.TokenRepository;
import com.weesftw.api.repository.UserRepository;
import com.weesftw.common.Config;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;

@Singleton
public class DefaultTokenRepository implements TokenRepository {

    private final DataSource dataSource;
    private final UserRepository userRepository;

    private final String getToken;
    private final String createToken;
    private final String updateToken;

    public DefaultTokenRepository(Config envs, DataSource dataSource, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.getToken = envs.get("/SQL/get_token.sql");
        this.createToken = envs.get("/SQL/create_token.sql");
        this.updateToken = envs.get("/SQL/update_token.sql");
        this.userRepository = userRepository;
    }

    @Override
    public Token get(String refreshToken) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(getToken)) {
            stmt.setString(1, refreshToken);

            var rs = stmt.executeQuery();
            while(rs.next()) {
                return Token.builder()
                        .id(rs.getLong("id"))
                        .user(userRepository.get(rs.getString("user_id")))
                        .refreshToken(rs.getString("refresh_token"))
                        .revoked(rs.getBoolean("revoked"))
                        .createAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
        } catch(SQLException e) {
            throw new SQLException(e);
        }

        return null;
    }

    @Override
    public boolean save(Token token) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(createToken)) {
            stmt.setLong(1, token.getUser().getId());
            stmt.setString(2, token.getRefreshToken());
            stmt.setBoolean(3, token.isRevoked());

            stmt.execute();
            return true;
        } catch(SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(boolean revoked, String token) throws SQLException {
        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(updateToken)) {
            stmt.setBoolean(1, revoked);
            stmt.setString(2, token);

            stmt.execute();
            return true;
        } catch(SQLException e) {
            throw new SQLException(e);
        }
    }
}
