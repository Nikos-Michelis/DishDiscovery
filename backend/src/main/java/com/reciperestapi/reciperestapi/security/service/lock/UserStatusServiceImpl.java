package com.reciperestapi.reciperestapi.security.service.lock;

import com.reciperestapi.reciperestapi.user.model.User;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class UserStatusServiceImpl implements UserStatusService {
    @Inject
    private UserDAOImpl userDAO;

    private static final long LOCK_EXPIRATION_MIN = 5 * 60 * 1000; // 5 minutes
    private static final long LOCK_EXPIRATION_MAX = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    private static final Logger LOGGER = Logger.getLogger(UserStatusServiceImpl.class.getName());

    public boolean isAccountCurrentlyLocked(User savedUser) {
        return savedUser.isAccountLocked() && LocalDateTime.now().isBefore(savedUser.getLockExpiresAt());

    }

    public boolean isAccountUnlocked(User user) {
        return user.isAccountLocked() && LocalDateTime.now().isAfter(user.getLockExpiresAt());
    }

    @Override
    public void lockAccount(User user) {
        user.setLockedAt(LocalDateTime.now());
        user.setLockExpiresAt(calculateLockAccountExpiration(user));
        user.setAccountLocked(true);
        user.setTotalBlocks(user.getTotalBlocks() + 1);
        updateLock(user);
    }

    @Override
    public void resetAccount(User user) {
        user.setAccountLocked(false);
        user.setTotalAttempts(0);
        updateLock(user);
    }

    @Override
    public void unlockAccount(User user) {
        user.setAccountLocked(false);
        user.setTotalAttempts(0);
        user.setTotalBlocks(0);
        LOGGER.info(String.format("user after lock change --> " + user));
        updateLock(user);
    }

    private LocalDateTime calculateLockAccountExpiration(User savedUser) {
        return savedUser.getTotalBlocks() >= 1
                ? LocalDateTime.now().plusSeconds(LOCK_EXPIRATION_MAX / 1000)
                : LocalDateTime.now().plusSeconds(LOCK_EXPIRATION_MIN / 1000);
    }

    private void updateLock(User user) {
        userDAO.updateLock(user);
    }

}
