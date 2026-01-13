package com.mycompany.clientside.dto;

import com.google.gson.Gson;

public class SessionDAO {

    private static SessionDAO instance;
    private Session currentSession;
    private final Gson gson = new Gson();

    private SessionDAO() {}

    public static synchronized SessionDAO getInstance() {
        if (instance == null) {
            instance = new SessionDAO();
        }
        return instance;
    }

    public void setSessionFromJson(String json) {
        this.currentSession = gson.fromJson(json, Session.class);
    }

    public Session getSession() {
        return currentSession;
    }

    public boolean hasActiveSession() {
        return currentSession != null;
    }

    // ===== UPDATE (LOCAL) =====
    public void updatePlayer1Score(int score) {
        if (currentSession != null) {
            currentSession.setPlayer1Score(score);
        }
    }

    public void updatePlayer2Score(int score) {
        if (currentSession != null) {
            currentSession.setPlayer2Score(score);
        }
    }

    public void clearSession() {
        currentSession = null;
    }
}
