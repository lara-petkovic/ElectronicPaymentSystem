package com.example.sep.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private final Map<String, LoginAttempt> loginAttemptsCache = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = loginAttemptsCache.getOrDefault(username, new LoginAttempt(0, System.currentTimeMillis(), false));

        if (attempt.isLocked() && System.currentTimeMillis() - attempt.getLastAttemptTime() < LOCK_TIME_DURATION) {
            return;
        }

        attempt.setLastAttemptTime(System.currentTimeMillis());
        attempt.setFailedAttempts(attempt.getFailedAttempts() + 1);

        if (attempt.getFailedAttempts() >= MAX_ATTEMPTS) {
            attempt.setLocked(true);
        }

        loginAttemptsCache.put(username, attempt);
    }

    public boolean isLocked(String username) {
        LoginAttempt attempt = loginAttemptsCache.get(username);
        if (attempt == null || !attempt.isLocked()) {
            return false;
        }

        if (System.currentTimeMillis() - attempt.getLastAttemptTime() > LOCK_TIME_DURATION) {
            attempt.setFailedAttempts(0);
            attempt.setLocked(false);
            loginAttemptsCache.put(username, attempt);
            return false;
        }

        return true;
    }

    public void resetAttempts(String username) {
        loginAttemptsCache.remove(username);
    }

    public void loginSucceeded(String username) {
        loginAttemptsCache.remove(username);
    }

    private static class LoginAttempt {
        private int failedAttempts;
        private long lastAttemptTime;
        private boolean isLocked;

        public LoginAttempt(int failedAttempts, long lastAttemptTime, boolean isLocked) {
            this.failedAttempts = failedAttempts;
            this.lastAttemptTime = lastAttemptTime;
            this.isLocked = isLocked;
        }

        public int getFailedAttempts() {
            return failedAttempts;
        }

        public void setFailedAttempts(int failedAttempts) {
            this.failedAttempts = failedAttempts;
        }

        public long getLastAttemptTime() {
            return lastAttemptTime;
        }

        public void setLastAttemptTime(long lastAttemptTime) {
            this.lastAttemptTime = lastAttemptTime;
        }

        public boolean isLocked() {
            return isLocked;
        }

        public void setLocked(boolean locked) {
            isLocked = locked;
        }
    }
}
