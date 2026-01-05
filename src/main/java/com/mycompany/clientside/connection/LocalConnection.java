package com.mycompany.clientside.connection;

import com.mycompany.clientside.utils.AlertUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.BiConsumer;
import javafx.scene.control.Alert;
import javafx.stage.Window;

public class LocalConnection implements ConnectionStrategy {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private InetAddress ip;
    private boolean isConnected;
    private int portNumber;
    private Thread listenerThread;
    private Window owner;

    public LocalConnection(InetAddress ip,Window owner) {
        this.ip = ip;
        this.owner = owner;
        portNumber = 5000;
        isConnected = false;
        listenerThread = null;
    }

    public LocalConnection(Window owner) {
       this.owner = owner;
        try {
            this.ip = InetAddress.getByName("127.0.0.1");
            isConnected = false;
            portNumber = 5000;
            listenerThread = null;
            this.owner = owner;
        } catch (UnknownHostException ex) {
            AlertUtil.showAlert("Server Ip Error", "Invalid Ip Address", Alert.AlertType.ERROR,owner);
        }
    }

    @Override
    public boolean connectToServer() {
        try {
            socket = new Socket(ip, portNumber);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            isConnected = true;
        } catch (IOException ex) {
            isConnected = false;
            AlertUtil.showAlert("Connection Failed", "Unable to connect to server.\nPlease check server status.", Alert.AlertType.ERROR,owner);
        }
        return isConnected;
    }

    public void sendMove(int x , int y) {
        try {
            String command = "MOVE " + x + " " + y;
            dataOutputStream.writeUTF(command);
            dataOutputStream.flush();
        } catch (IOException ex) {
            AlertUtil.showAlert("Send Error", "Failed to send move to server.", Alert.AlertType.ERROR,owner);
        }
    }

    public void recieveMove(BiConsumer<Integer,Integer> onMoveReceived) {
        listenerThread = new Thread(() -> {
            while (isConnected) {
                try {
                    String receviablePosition = dataInputStream.readUTF();
                    String[] parts = receviablePosition.split(" ");
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);
                    onMoveReceived.accept(row,col);
                } catch (IOException ex) {
                    AlertUtil.showAlert("Recieve Error", "Failed to receive the next move from server.", Alert.AlertType.ERROR,owner);
                }
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
            if (socket != null) {
                socket.close();
            }
            isConnected = false;
        } catch (IOException e) {
            AlertUtil.showAlert("Close Error", "Error while closing connection.", Alert.AlertType.ERROR,owner);
        }
    }
}