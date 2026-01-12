package com.mycompany.clientside.service;

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
    private ClientMessageCallback callback;

    public ClientMessageListener(ConnectionStrategy conn, ClientMessageCallback callback) {
        this.conn = conn;
        this.callback = callback;
    }

    public void setCallback(ClientMessageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            String line;
            while (running && (line = conn.getReader().readLine()) != null) {
                String msg = line.trim();
                Platform.runLater(() -> onMessage(msg));
            }
        } catch (IOException e) {
            System.out.println("Connection closed.");
        }
    }

    private void onMessage(String msg) {
        System.out.println("Received from Server: " + msg);
        String command = extractCommand(msg);

        switch (command) {
            case "MOVE":
                if (callback != null) {
                    handleMove(msg);
                }
                break;
            case "START":
                if (callback != null) {
                    callback.startGame();
                }
                break;
            case "INVITE_REJECT":
                if (callback != null) {
                    String[] parts = msg.split(" ", 3);
                    callback.onInviteRejected(parts);
                }
                break;
            case "AVAILABLE_PLAYERS":
                if (callback != null) {
                    callback.loadPlayers(getAllAvailablePlayers(msg));
                }
                break;

            case "END":
                if (callback != null) {
                    callback.endGame();
                }
                break;
            case "ALL_PLAYERS":
                if (callback != null) {
                    callback.loadLeaderBoard(getAllPlayers(msg));
                }
                break;

            case "INVITE_FROM": // INVITE_FROM 5 MOAZ
                if (callback != null) {
                    String[] parts = msg.split(" ", 3);
                    callback.onInviteReceived(parts);
                }
                break;
            case "WIN":
                if (callback != null) {
                    char winner = msg.split(" ")[1].charAt(0);
                    callback.onWin(winner);
                }
                break;
            case "Lose":
                if (callback != null) {
                    char loser = msg.split(" ")[1].charAt(0);
                    callback.onLose(loser);
                }
                break;

            case "DRAW":
                if (callback != null) {
                    callback.onDraw();
                }
                break;

            case "NOT_YOUR_TURN":
                if (callback != null) {
                    callback.onNotYourTurn();
                }
                break;
            case "CELL_OCCUPIED":
                if (callback != null) {
                    callback.onCellOccupied();
                }
                break;
            default:
                System.out.println("Unknown command: " + msg);
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

    private void handleMove(String msg) {
        String[] parts = msg.split(" ");
        if (parts.length < 4) {
            return;
        }
        int row = Integer.parseInt(parts[1]);
        int col = Integer.parseInt(parts[2]);
        char player = parts[3].charAt(0);

        System.out.println("Move received: " + row + "," + col + " Player: " + player);

        if (callback != null) {
            callback.onMoveReceived(row, col, player);
        }
    }

    private List<Player> getAllAvailablePlayers(String response) {
        if (response.contains(":")) {
            response = response.substring(response.indexOf(":") + 1).trim();
        }
        if ("NONE".equals(response)) {
            return new ArrayList<>();
        }
        return JsonUtil.fromJsonList(response);
    }

    public void stopListener() {
        running = false;
    }
}
