package com.mycompany.clientside.dto;

import com.mycompany.clientside.dto.Player;

public class UserSession {

    private static UserSession instance;

    private Player currentUser;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Player getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Player currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void clear() {
        currentUser = null;
    }
}
