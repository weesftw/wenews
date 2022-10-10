package com.weesftw.api.repository;

import com.weesftw.api.model.Token;

import java.sql.SQLException;

public interface TokenRepository {

    Token get(String refreshToken) throws SQLException;
    boolean save(Token token) throws SQLException;
    boolean update(boolean revoked, String token) throws SQLException;
}
