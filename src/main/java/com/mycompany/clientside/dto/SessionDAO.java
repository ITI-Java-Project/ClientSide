/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.dto;



public class SessionDAO {

    private static SessionDAO instance;
    private Session currentSession;

    private SessionDAO() {
    }

    public static synchronized SessionDAO getInstance() {
        if (instance == null) {
            instance = new SessionDAO();
        }
        return instance;
    }

    // Create / Set session
    public void setSession(Session session) {
        this.currentSession = session;
    }

    // Read
    public Session getSession() {
        return currentSession;
    }

    // Update scores
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

    // Clear session (end game)
    public void clearSession() {
        currentSession = null;
    }

    // Helper
    public boolean hasActiveSession() {
        return currentSession != null;
    }
}
