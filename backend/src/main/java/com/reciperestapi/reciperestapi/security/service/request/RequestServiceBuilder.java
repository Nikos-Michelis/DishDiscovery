package com.reciperestapi.reciperestapi.security.service.request;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.UUID;

public class RequestServiceBuilder {
    public String generateRequestId() {
        String timestamp = Instant.now().toString();
        String uuid = UUID.randomUUID().toString();
        String combined = timestamp + "-" + uuid;
        return hashString(combined);
    }

    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing the request ID", e);
        }
    }
}
