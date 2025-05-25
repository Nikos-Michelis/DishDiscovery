package com.reciperestapi.reciperestapi.security.repository.impl_service;

import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import com.reciperestapi.reciperestapi.security.dto.OtpValidationRequest;
import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpType;
import com.reciperestapi.reciperestapi.security.repository.OtpDAO;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.service.otp.OtpResendService;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class OtpDAOImpl implements OtpDAO {
    @Inject
    private DatabaseService databaseService;
    @Inject
    private UserDAOImpl userDAO;
    private static final Logger LOGGER = Logger.getLogger(OtpDAOImpl.class.getName());

    @Override
    public void saveOtp(OtpToken otpToken) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "INSERT INTO otp (user_id, otp_uuid, otp_code, otp_type, createdAt, expiresAt) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otpToken.getUser().getUserId());
                preparedStatement.setString(2, otpToken.getOtpUUId());
                preparedStatement.setString(3, otpToken.getOtpCode());
                preparedStatement.setString(4, otpToken.getOtpType().name());
                preparedStatement.setTimestamp(5, java.sql.Timestamp.valueOf(otpToken.getCreatedAt()));
                preparedStatement.setTimestamp(6, java.sql.Timestamp.valueOf(otpToken.getExpiresAt()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error saving otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    private LocalDateTime getLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
    @Override
    public Optional<OtpToken> findByOtp(OtpValidationRequest otpToken) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM otp WHERE otp_code = ? AND otp_uuid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, otpToken.getOtp());
                preparedStatement.setString(2, otpToken.getUuid());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        OtpToken retrievedOtp = OtpToken.builder()
                                .otpId(resultSet.getInt("otp_id"))
                                .userId(resultSet.getInt("user_id"))
                                .otpUUId(resultSet.getString("otp_uuid"))
                                .otpCode(resultSet.getString("otp_code"))
                                .otpType(OtpType.valueOf(resultSet.getString("otp_type")))
                                .createdAt(resultSet.getTimestamp("createdAt").toLocalDateTime())
                                .expiresAt(resultSet.getTimestamp("expiresAt").toLocalDateTime())
                                .validatedAt(getLocalDateTime(resultSet.getTimestamp("validatedAt")))
                                .isRedeemed(resultSet.getBoolean("redeemed"))
                                .user(userDAO.findUserById(resultSet.getInt("user_id")).orElseThrow())
                                .build();
                        LOGGER.info("retrieved-Otp: " + retrievedOtp);
                        return Optional.of(retrievedOtp);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
     @Override
    public Optional<OtpToken> findByOtpType(OtpToken otp) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM otp WHERE user_id = ? AND otp_type = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otp.getUser().getUserId());
                preparedStatement.setString(2, otp.getOtpType().name());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        OtpToken retrievedOtp = OtpToken.builder()
                                .otpId(resultSet.getInt("otp_id"))
                                .userId(resultSet.getInt("user_id"))
                                .otpUUId(resultSet.getString("otp_uuid"))
                                .otpCode(resultSet.getString("otp_code"))
                                .otpType(OtpType.valueOf(resultSet.getString("otp_type")))
                                .createdAt(resultSet.getTimestamp("createdAt").toLocalDateTime())
                                .expiresAt(resultSet.getTimestamp("expiresAt").toLocalDateTime())
                                .validatedAt(getLocalDateTime(resultSet.getTimestamp("validatedAt")))
                                .otpResendCount(resultSet.getInt("otp_resend_count"))
                                .isRedeemed(resultSet.getBoolean("redeemed"))
                                .user(userDAO.findUserById(resultSet.getInt("user_id")).orElseThrow())
                                .build();
                        LOGGER.info("retrieved-Otp: " + retrievedOtp);
                        return Optional.of(retrievedOtp);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

     @Override
        public Optional<OtpToken> findByRequestId(String requestId) {
            Connection connection = null;
            try {
                connection = databaseService.getConnection();
                String query = "SELECT * FROM otp WHERE otp_uuid = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, requestId);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            OtpToken retrievedOtp = OtpToken.builder()
                                    .otpId(resultSet.getInt("otp_id"))
                                    .userId(resultSet.getInt("user_id"))
                                    .otpUUId(resultSet.getString("otp_uuid"))
                                    .otpCode(resultSet.getString("otp_code"))
                                    .otpType(OtpType.valueOf(resultSet.getString("otp_type")))
                                    .createdAt(resultSet.getTimestamp("createdAt").toLocalDateTime())
                                    .expiresAt(resultSet.getTimestamp("expiresAt").toLocalDateTime())
                                    .validatedAt(getLocalDateTime(resultSet.getTimestamp("validatedAt")))
                                    .isRedeemed(resultSet.getBoolean("redeemed"))
                                    .user(userDAO.findUserById(resultSet.getInt("user_id")).orElseThrow())
                                    .build();
                            LOGGER.info("retrieved-Otp: " + retrievedOtp);
                            return Optional.of(retrievedOtp);
                        }
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error retrieving otp", e);
            } finally {
                databaseService.closeConnection(connection);
            }
        }

    @Override
    public void update(OtpToken otpToken){
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE otp SET validatedAt = ?, redeemed = ? WHERE otp_id = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setTimestamp(1, Timestamp.valueOf(otpToken.getValidatedAt()));
                preparedStatement.setBoolean(2, otpToken.isRedeemed());
                preparedStatement.setInt(3, otpToken.getOtpId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
     @Override
    public void updateRedeemOtp(OtpToken otpToken){
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE otp SET redeemed = ? WHERE otp_id = ? AND validatedAt IS NOT NULL ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, otpToken.isRedeemed());
                preparedStatement.setInt(2, otpToken.getOtpId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void delete(OtpToken otpToken){
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "DELETE FROM otp WHERE otp_id = ? AND user_id  = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otpToken.getOtpId());
                preparedStatement.setInt(2, otpToken.getUser().getUserId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
}
