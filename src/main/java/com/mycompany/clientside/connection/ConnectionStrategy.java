package com.mycompany.clientside.connection;

import com.mycompany.clientside.utils.AlertUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javafx.scene.control.Alert;
import javafx.stage.Window;

public abstract class ConnectionStrategy {

    protected Socket socket;
    protected BufferedReader reader;
    protected PrintWriter writer;
    protected InetAddress ip;
    protected boolean isConnected;
    protected int portNumber;
    protected Thread listenerThread;
    protected Window owner;

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public InetAddress getIp() {
        return ip;
    }

    public boolean isIsConnected() {
        return isConnected;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public Thread getListenerThread() {
        return listenerThread;
    }

    public abstract boolean connectToServer();

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
            AlertUtil.showAlert("Close Error", "Error while closing connection.", Alert.AlertType.ERROR,owner);
        }
    }
}