package com.reciperestapi.reciperestapi.security.service.lock;

import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.user.model.User;

public interface UserStatusService {
    boolean isAccountCurrentlyLocked(User savedUser);
    void lockAccount(User user);
    void resetAccount(User user);
    void unlockAccount(User user);
}
