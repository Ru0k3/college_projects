package online_examination_system.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import online_examination_system.Data.DataStore;
import online_examination_system.users.Users;

/**
 * Central authentication manager for validating users and maintaining
 * in-memory sessions during the current application run.
 */
public class Authentication {

    private final Set<String> loggedInUserIds;
    private final Map<String, String> sessionToUserId;
    private Users currentUser;
    private String currentSessionToken;

    public Authentication() {
        this.loggedInUserIds = new HashSet<>();
        this.sessionToUserId = new HashMap<>();
    }

    public boolean validateUser(String email, String password, String role) {
        currentUser = DataStore.findUserByCredentials(email, password, role);
        if (currentUser == null) {
            currentSessionToken = null;
            return false;
        }
        generateSession(currentUser.getUserId());
        return true;
    }

    public String generateSession(String userId) {
        String token = UUID.randomUUID().toString();
        loggedInUserIds.add(userId);
        sessionToUserId.put(token, userId);
        currentSessionToken = token;
        return token;
    }

    public boolean isLoggedIn(String userId) {
        return userId != null && loggedInUserIds.contains(userId);
    }

    public boolean isSessionValid(String token) {
        return token != null && sessionToUserId.containsKey(token);
    }

    public void invalidateSession(String token) {
        String userId = sessionToUserId.remove(token);
        if (userId != null) {
            loggedInUserIds.remove(userId);
        }
        if (token != null && token.equals(currentSessionToken)) {
            currentSessionToken = null;
            currentUser = null;
        }
    }

    public void logoutCurrentUser() {
        invalidateSession(currentSessionToken);
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public String getCurrentSessionToken() {
        return currentSessionToken;
    }
}
