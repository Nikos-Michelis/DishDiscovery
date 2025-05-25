package com.reciperestapi.reciperestapi.user.repository.impl_service;

import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.user.model.User;
import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import com.reciperestapi.reciperestapi.user.repository.UserDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
public class UserDAOImpl implements UserDAO {

    @Inject
    DatabaseService databaseService;

    @Override
    public Optional<User> findByUsername(String username) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT user_id, user_uuid, userName, email, password, enable FROM user WHERE userName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setUserId(resultSet.getInt("user_id"));
                        user.setUserUUId(resultSet.getString("user_uuid"));
                        user.setUsername(resultSet.getString("userName"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setEnable(resultSet.getBoolean("enable"));
                        user.setRole(findRoleByUserName(resultSet.getString("userName")));
                        return Optional.of(user);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user with username: " + username, e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public Optional<User> findUserById(User user) {
        return findUserById(user.getUserId());
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT user_id, userName, email, password, enable FROM user WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setUserId(resultSet.getInt("user_id"));
                        user.setUsername(resultSet.getString("userName"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setEnable(resultSet.getBoolean("enable"));
                        user.setRole(findRoleByUserName(resultSet.getString("userName")));
                        return Optional.of(user);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user with username: " + email, e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    private LocalDateTime getLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
    public Optional<User> findUserById(Integer userId) {
        Connection connection = null;
        User user = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT user_id, user_uuid, userName, email, password, enable, accountLocked, total_blocks, total_attempts, lockedAt, lock_expiresAt " +
                           " FROM user WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User();
                        user.setUserId(resultSet.getInt("user_id"));
                        user.setUserUUId(resultSet.getString("user_uuid"));
                        user.setUsername(resultSet.getString("userName"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setEnable(resultSet.getBoolean("enable"));
                        user.setAccountLocked(resultSet.getBoolean("accountLocked"));
                        user.setTotalBlocks(resultSet.getInt("total_blocks"));
                        user.setTotalAttempts(resultSet.getInt("total_attempts"));
                        user.setLockedAt(getLocalDateTime(resultSet.getTimestamp("lockedAt")));
                        user.setLockExpiresAt(getLocalDateTime(resultSet.getTimestamp("lock_expiresAt")));
                        user.setRole(findRoleByUserName(resultSet.getString("userName")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user with user_id: " + userId, e);
        } finally {
            databaseService.closeConnection(connection);
        }
        return Optional.ofNullable(user);
    }
    @Override
    public Set<Roles> findRoleByUserName(String userName) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = " SELECT role.description AS role FROM user  " +
                    " JOIN user_role ON user.user_id = user_role.user_id" +
                    " JOIN role ON user_role.role_id = role.role_id" +
                    " WHERE userName = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();

            Set<Roles> roleList = new HashSet<>() {
            };
            while (resultSet.next()) {
                String role = resultSet.getString("role");
                roleList.add(Roles.valueOf(role));
            }
            //System.out.println(role);
            return roleList;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user role: " + e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public Optional<User> save(User user) {
        Connection connection = null;
        Integer userId = null;
        try {
            connection = databaseService.getConnection();
            String query = "INSERT INTO user (user_uuid, userName, email, password, accountLocked, enable) VALUES (?, ?, ?, ?, ?, ? )";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUserUUId());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setBoolean(5, user.isAccountLocked());
                preparedStatement.setBoolean(6, user.isEnable());
                preparedStatement.executeUpdate();
                ResultSet genKeys = preparedStatement.getGeneratedKeys();
                genKeys.next();
                userId = (int) genKeys.getLong(1);
                saveRole(userId);
            }
            return Optional.of(User.builder()
                    .userId(userId)
                    .userUUId(user.getUserUUId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .enable(user.isEnable())
                    .roleId(user.getRoleId())
                    .role(new HashSet<>(findRoleByUserName(user.getUsername())) {
                    }).build());
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public Optional<User> update(User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE user SET password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1,user.getPassword()); // No hashing
                preparedStatement.executeUpdate();
            }
            return Optional.of(User.builder().userId(user.getUserId())
                    .username(user.getUsername())
                    .roleId(user.getRoleId())
                    .role(new HashSet<>(findRoleByUserName(user.getUsername())) {
                    }).build());
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public void updateStatus(User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE user SET enable = ? WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1,user.isEnable());
                preparedStatement.setInt(2,user.getUserId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public void updateAttempts(User user){
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE user SET total_attempts = ? WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getTotalAttempts());
                preparedStatement.setInt(2,user.getUserId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void updateLock(User user){
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE user SET accountLocked = ?, lockedAt = ?, lock_expiresAt = ?, total_blocks = ? , total_attempts = ? WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, user.isAccountLocked());
                preparedStatement.setObject(2, user.getLockedAt() != null ? Timestamp.valueOf(user.getLockedAt()) : null, java.sql.Types.TIMESTAMP);
                preparedStatement.setObject(3, user.getLockExpiresAt() != null ? Timestamp.valueOf(user.getLockExpiresAt()) : null, java.sql.Types.TIMESTAMP);
                preparedStatement.setInt(4, user.getTotalBlocks());
                preparedStatement.setInt(5, user.getTotalAttempts());
                preparedStatement.setInt(6,user.getUserId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public void saveRole(int userId) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, 2); // No hashing
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
}
