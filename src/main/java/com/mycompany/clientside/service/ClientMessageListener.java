package com.mycompany.clientside.service;

import com.mycompany.clientside.callbacks.GameActionCallback;
import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.GameResultCallback;
import com.mycompany.clientside.callbacks.InvitationCallback;
import com.mycompany.clientside.callbacks.PlayersCallback;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.utils.JsonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;

public class ClientMessageListener implements Runnable {

    private final ConnectionStrategy conn;
    private volatile boolean running = true;
    private Object callback; // Can be any of the segregated callback interfaces

    public ClientMessageListener(ConnectionStrategy conn, Object callback) {
        this.conn = conn;
        this.callback = callback;
    }

   
    public void setCallback(Object callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            String line;
            while (running && (line = conn.getReader().readLine()) != null) {
                final String msg = line.trim();
                Platform.runLater(() -> onMessage(msg));
            }
        } catch (IOException e) {
            System.out.println("Connection closed: " + e.getMessage());
        }
    }

    
    private void onMessage(String msg) {
        System.out.println("Received from Server: " + msg);
        String command = extractCommand(msg);

        switch (command) {

            case "MOVE":
                handleMove(msg);
                break;

            case "NOT_YOUR_TURN":
                if (callback instanceof GameActionCallback) {
                    ((GameActionCallback) callback).onNotYourTurn();
                }
                break;

            case "CELL_OCCUPIED":
                if (callback instanceof GameActionCallback) {
                    ((GameActionCallback) callback).onCellOccupied();
                }
                break;

            //  GAME LIFECYCLE 
            case "START":
                if (callback instanceof GameLifecycleCallback) {
                    ((GameLifecycleCallback) callback).startGame();
                }
                break;

            case "END":
                if (callback instanceof GameLifecycleCallback) {
                    ((GameLifecycleCallback) callback).endGame();
                }
                break;

            // GAME RESULT 
            case "WIN":
                if (callback instanceof GameResultCallback) {
                    char winner = msg.split(" ")[1].charAt(0);
                    ((GameResultCallback) callback).onWin(winner);
                }
                break;

            case "LOSE":
                if (callback instanceof GameResultCallback) {
                    char loser = msg.split(" ")[1].charAt(0);
                    ((GameResultCallback) callback).onLose(loser);
                }
                break;

            case "DRAW":
                if (callback instanceof GameResultCallback) {
                    ((GameResultCallback) callback).onDraw();
                }
                break;

            //  INVITATIONS 
            case "INVITE_FROM":
                if (callback instanceof InvitationCallback) {
                    String[] parts = msg.split(" ", 3);
                    ((InvitationCallback) callback).onInviteReceived(parts);
                }
                break;

            case "INVITE_REJECT":
                if (callback instanceof InvitationCallback) {
                    String[] parts = msg.split(" ", 3);
                    ((InvitationCallback) callback).onInviteRejected(parts);
                }
                break;

            //  PLAYERS / LEADERBOARD 
            case "AVAILABLE_PLAYERS":
                if (callback instanceof PlayersCallback) {
                    ((PlayersCallback) callback)
                        .loadPlayers(getAllPlayers(msg));
                }
                break;

           

            default:
                System.out.println("Unknown command: " + msg);
        }
    }

    //  Helper Methods 

    private void handleMove(String msg) {
        String[] parts = msg.split(" ");
        if (parts.length < 4) return;

        int row = Integer.parseInt(parts[1]);
        int col = Integer.parseInt(parts[2]);
        char player = parts[3].charAt(0);

        System.out.println("Move received: " + row + "," + col + " Player: " + player);

        if (callback instanceof GameActionCallback) {
            ((GameActionCallback) callback).onMoveReceived(row, col, player);
        }
    }

    private List<Player> getAllPlayers(String response) {
        if (response.contains(":")) {
            response = response.substring(response.indexOf(":") + 1).trim();
        }

        if ("NONE".equals(response)) {
            return new ArrayList<>();
        }

        return JsonUtil.fromJsonList(response);
    }

    private String extractCommand(String msg) {
        if (msg.contains(":")) {
            return msg.substring(0, msg.indexOf(":"));
        }
        return msg.split(" ")[0];
    }

    
    public void stopListener() {
        running = false;
    }
}