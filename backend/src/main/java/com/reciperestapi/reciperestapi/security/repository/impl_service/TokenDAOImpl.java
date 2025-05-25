package com.reciperestapi.reciperestapi.security.repository.impl_service;

import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import com.reciperestapi.reciperestapi.security.repository.TokenDAO;
import com.reciperestapi.reciperestapi.security.model.Token;
import com.reciperestapi.reciperestapi.security.model.TokenType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TokenDAOImpl implements TokenDAO {

    @Inject
    private DatabaseService databaseService;
    @Override
    public List<Token> findAllValidTokenByUserId(Integer id) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM token WHERE user_id = ? AND expired = 0 AND revoked = 0 ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Token> tokens = new ArrayList<>();
                    while (resultSet.next()) {
                        Token token = Token.builder()
                                .userId(resultSet.getInt("user_id"))
                                .token(resultSet.getString("token"))
                                .tokenType(TokenType.valueOf(resultSet.getString("token_type")))
                                .expired(resultSet.getBoolean("expired"))
                                .revoked(resultSet.getBoolean("revoked"))
                                .ipAddress(resultSet.getString("ipAddress"))
                                .build();
                        tokens.add(token);
                    }
                    return tokens;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Token", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public Optional<Token> findByToken(String token) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM token WHERE token = ? AND expired = false AND revoked = false";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, token);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Token retrievedToken = Token.builder()
                                .userId(resultSet.getInt("user_id"))
                                .token(resultSet.getString("token"))
                                .tokenType(TokenType.valueOf(resultSet.getString("token_type")))
                                .expired(resultSet.getBoolean("expired"))
                                .revoked(resultSet.getBoolean("revoked"))
                                .build();
                        return Optional.of(retrievedToken);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving Token", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void saveToken(Token token) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "INSERT INTO token(user_id, jti, token, token_type, expired, revoked, token_expires) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, token.getUserId());
                preparedStatement.setString(2, token.getJti());
                preparedStatement.setString(3, token.getToken());
                preparedStatement.setString(4, (token.getTokenType().name()));
                preparedStatement.setBoolean(5, token.isExpired());
                preparedStatement.setBoolean(6, token.isRevoked());
                preparedStatement.setTimestamp(7, java.sql.Timestamp.valueOf(token.getExpiresAt().toLocalDateTime()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving Token", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void update(List<Token> validUserTokens) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE token SET token = ?, token_type = ?, expired = ?, revoked = ? WHERE user_id = ? AND token = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (var token : validUserTokens) {
                    preparedStatement.setString(1, token.getToken());
                    preparedStatement.setString(2, String.valueOf(token.getTokenType()));
                    preparedStatement.setBoolean(3, token.isExpired());
                    preparedStatement.setBoolean(4, token.isRevoked());
                    preparedStatement.setInt(5, token.getUserId());
                    preparedStatement.setString(6, token.getToken());
                    preparedStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving Token", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void revokeToken() {

    }

    @Override
    public void revokeAllToken() {

    }
}
