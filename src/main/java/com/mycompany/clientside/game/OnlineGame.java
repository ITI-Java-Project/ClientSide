/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.game;

import com.mycompany.clientside.callbacks.GameActionCallback;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.connection.LocalConnection;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.matchRecording.Move;
import com.mycompany.clientside.service.ClientMessageCallback;
import java.util.List;

/**
 *
 * @author ezemo
 */
public class OnlineGame implements GameStartegy,GameActionCallback {

    private final ConnectionStrategy conn;

    public OnlineGame() {
        this.conn = ConnectionManager.getConnection();
        conn.getClientMessageListener().setCallback(this);
    }

    @Override
    public void play(int x, int y, char move) {
        conn.getWriter().println("MOVE " + x + " " + y);
    }

    @Override
    public int[][] winner() {
        return null;
    }

    @Override
    public void onMoveReceived(Move move) {
        System.out.println("CALLBACK MOVE " + move.getX() + "," + move.getY() + " " + move.getMove());
        Board.getInstance().setCell(move.getX(), move.getY(), move.getMove());
    }

    @Override
    public void onNotYourTurn() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onCellOccupied() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
