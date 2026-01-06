package com.mycompany.clientside.game;

import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.utils.AlertUtil;
import java.io.IOException;
import java.util.function.BiConsumer;
import javafx.scene.control.Alert;

public class Transmitter {

    ConnectionStrategy conn;
    Thread listenerThread;

    public Transmitter(ConnectionStrategy conn, Thread listenerThread) {
        this.conn = conn;
        this.listenerThread = listenerThread;
    }

    public void sendMove(int x, int y) {
        if (!conn.isIsConnected()) {
            return;
        }
        conn.getWriter().println("MOVE " + x + " " + y);
    }

    public void recieveMove(BiConsumer<Integer, Integer> onMoveReceived) {
        listenerThread = new Thread(() -> {
            try {
                String message;
                while (conn.isIsConnected() && (message = conn.getReader().readLine()) != null) {

                    if (message.startsWith("MOVE")) {
                        String[] parts = message.split(" ");
                        int row = Integer.parseInt(parts[1]);
                        int col = Integer.parseInt(parts[2]);
                        onMoveReceived.accept(row, col);
                    }
                }
            } catch (IOException ex) {
                AlertUtil.showAlert("Receive Error", "Failed to receive data from server.", Alert.AlertType.ERROR);
            }
        });
        listenerThread.start();
    }
}