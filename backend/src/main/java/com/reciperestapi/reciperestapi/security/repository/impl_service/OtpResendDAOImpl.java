package com.reciperestapi.reciperestapi.security.repository.impl_service;

import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.repository.OtpResendDAO;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.Optional;

public class OtpResendDAOImpl implements OtpResendDAO {
    @Inject
    private DatabaseService databaseService;
    @Override
    public Optional<OtpResend> findByUserIdAndOtpType(OtpToken otpToken) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM otp_resend WHERE user_id = ? AND otp_type = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otpToken.getUserId());
                preparedStatement.setString(2, otpToken.getOtpType().name());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        OtpResend otpResend =
                                OtpResend.builder()
                                        .resendId(resultSet.getInt("resend_id"))
                                        .userId(resultSet.getInt("user_id"))
                                        .otpType(resultSet.getString("otp_type"))
                                        .otpResendCount(resultSet.getInt("otp_resend_count"))
                                        .lastOtpSentTime(resultSet.getTimestamp("last_otp_sent_time").toLocalDateTime())
                                        .build();
                        return Optional.of(otpResend);
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
    public void save(OtpResend otpResend) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "INSERT INTO otp_resend (user_id, otp_type, otp_resend_count) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otpResend.getUserId());
                preparedStatement.setString(2, otpResend.getOtpType());
                preparedStatement.setInt(3, otpResend.getOtpResendCount());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void update(OtpResend otpResend) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "UPDATE otp_resend SET otp_resend_count = ?, last_otp_sent_time = ? WHERE user_id = ? AND otp_type = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otpResend.getOtpResendCount());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(otpResend.getLastOtpSentTime()));
                preparedStatement.setInt(3, otpResend.getUserId());
                preparedStatement.setString(4, otpResend.getOtpType());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void delete(OtpResend otpResend) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "DELETE FROM otp_resend WHERE resend_id = ? AND user_id  = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, otpResend.getResendId());
                preparedStatement.setInt(2, otpResend.getUserId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating otp", e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

}
