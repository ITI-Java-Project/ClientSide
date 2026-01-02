package com.mycompany.clientside.connection;

import com.mycompany.clientside.utils.AlertUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import javafx.scene.control.Alert;

public class LocalConnection implements ConnectionStrategy {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private InetAddress ip;
    private boolean isConnected;
    private int portNumber;
    private Thread listenerThread;

    public LocalConnection(DataInputStream dataInputStream, DataOutputStream dataOutputStream, InetAddress ip) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.ip = ip;
        portNumber = 5005;
        isConnected = false;
        listenerThread = null;
    }

    public LocalConnection(DataInputStream dis, DataOutputStream dos) {
        try {
            this.dataInputStream = dis;
            this.dataOutputStream = dos;
            this.ip = InetAddress.getByName("127.0.0.1");
            isConnected = false;
            portNumber = 5005;
            listenerThread = null;
        } catch (UnknownHostException ex) {
            AlertUtil.showAlert("Server Ip Error", "Invalid Ip Address", Alert.AlertType.ERROR);
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
            AlertUtil.showAlert("Connection Failed", "Unable to connect to server.\nPlease check server status.", Alert.AlertType.ERROR);
        }
        return isConnected;
    }

    public void sendMove(int position) {
        try {
            dataOutputStream.writeInt(position);
        } catch (IOException ex) {
            AlertUtil.showAlert("Send Error", "Failed to send move to server.", Alert.AlertType.ERROR);
        }
    }

    public void recieveMove(Consumer<Integer> onMoveReceived) {
        listenerThread = new Thread(() -> {
            while (isConnected) {
                try {
                    int receviablePosition = dataInputStream.readInt();
                    onMoveReceived.accept(receviablePosition);
                } catch (IOException ex) {
                    AlertUtil.showAlert("Recieve Error", "Failed to receive the next move from server.", Alert.AlertType.ERROR);
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
            AlertUtil.showAlert("Close Error", "Error while closing connection.", Alert.AlertType.ERROR);
        }
    }
}