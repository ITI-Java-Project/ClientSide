package com.mycompany.clientside.connection;

import com.mycompany.clientside.utils.AlertUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.BiConsumer;
import javafx.scene.control.Alert;

public class LocalConnection implements ConnectionStrategy {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private InetAddress ip;
    private boolean isConnected;
    private int portNumber;
    private Thread listenerThread;

    public LocalConnection(InetAddress ip) {
        this.ip = ip;
        this.portNumber = 5000;
        this.isConnected = false;
        listenerThread = null;
    }

    public LocalConnection() {
        try {
            this.ip = InetAddress.getByName("127.0.0.1");
            this.portNumber = 5000;
            this.isConnected = false;
            listenerThread = null;
        } catch (UnknownHostException ex) {
            AlertUtil.showAlert("Server Ip Error", "Invalid Ip Address",Alert.AlertType.ERROR);
        }
    }

    @Override
    public boolean connectToServer() {
        try {
            socket = new Socket(ip, portNumber);
            reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()),
                true 
            );
            isConnected = true;

        } catch (IOException ex) {
            isConnected = false;
            AlertUtil.showAlert("Connection Failed","Unable to connect to server.\nPlease check server status.", Alert.AlertType.ERROR);
        }
        return isConnected;
    }


    public void sendMove(int x, int y) {
        if (!isConnected) return;
        writer.println("MOVE " + x + " " + y);
    }
 
    public boolean signup(String username, String email, String password) {
        if (!isConnected) {
            connectToServer();   
        }

        if (!isConnected) return false;
        
        String command = "REGISTER " + username + " " + email + " " + password;
        writer.println(command);
        try {
            if(reader.readLine().startsWith("REGISTERED")){
                return true;
            }
        } catch (IOException ex) {
            AlertUtil.showAlert("Sginup Failed","Unable to sign up.\nPlease retry again", Alert.AlertType.ERROR);
        }
        return false;
    }

    public void recieveMove(BiConsumer<Integer, Integer> onMoveReceived) {
        listenerThread = new Thread(() -> {
            try {
                String message;
                while (isConnected && (message = reader.readLine()) != null) {

                    if (message.startsWith("MOVE")) {
                        String[] parts = message.split(" ");
                        int row = Integer.parseInt(parts[1]);
                        int col = Integer.parseInt(parts[2]);
                        onMoveReceived.accept(row, col);
                    }
                }
            } catch (IOException ex) {
                AlertUtil.showAlert("Receive Error","Failed to receive data from server.",Alert.AlertType.ERROR);
            }
        });
        listenerThread.start();
    }

    public boolean reconnect() {
        closeConnection();
        return connectToServer();
    }

    public void closeConnection() {
        try {
            if (listenerThread != null && listenerThread.isAlive()) {
                listenerThread.interrupt();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
             isConnected = false;

        } catch (IOException e) {
            AlertUtil.showAlert("Close Error", "Error while closing connection.", Alert.AlertType.ERROR);
        }
    }
}