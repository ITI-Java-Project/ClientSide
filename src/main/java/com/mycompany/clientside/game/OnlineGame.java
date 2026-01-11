/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.game;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.connection.LocalConnection;

/**
 *
 * @author ezemo
 */
public class OnlineGame implements GameStartegy {

    private final ConnectionStrategy conn;

    public OnlineGame() {
        this.conn = ConnectionManager.getConnection();
    }

    @Override
    public void play(int x, int y, char move) {
        conn.getWriter().println("MOVE " + x + " " + y);
    }

    @Override
    public int[][] winner() {
        return null;
    }
}
