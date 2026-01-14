package com.mycompany.clientside.dto;

public class Session {

    private int id;
    private int player1Id;
    private int player2Id;
    private int player1Score;
    private int player2Score;
    private String player1Name;
    private String player2Name;

    public Session(int id, int player1Id, int player2Id, int player1Score, int player2Score, 
                   String player1Name, String player2Name) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    // Existing getters...
    public int getId() { return id; }
    public int getPlayer1Id() { return player1Id; }
    public int getPlayer2Id() { return player2Id; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }

    // New getters for names
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }

    // Setters
    public void setPlayer1Score(int player1Score) { this.player1Score = player1Score; }
    public void setPlayer2Score(int player2Score) { this.player2Score = player2Score; }

    @Override
    public String toString() {
        return "Session{" + "id=" + id + ", player1Id=" + player1Id + ", player2Id=" + player2Id 
               + ", player1Score=" + player1Score + ", player2Score=" + player2Score 
               + ", player1Name=" + player1Name + ", player2Name=" + player2Name + '}';
    }
}