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
import javafx.scene.control.Alert;

public class LocalConnection extends ConnectionStrategy {

    public LocalConnection(InetAddress ip) {
        this.ip = ip;
        this.portNumber = 5000;
        this.isConnected = false;
        this.listenerThread = null;
    }

    public LocalConnection() {
        try {
            this.ip = InetAddress.getByName("127.0.0.1");
            this.portNumber = 5000;
            this.isConnected = false;
            this.listenerThread = null;
        } catch (UnknownHostException ex) {
            AlertUtil.showAlert("Server Ip Error", "Invalid Ip Address", Alert.AlertType.ERROR);
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
            AlertUtil.showAlert("Connection Failed", "Unable to connect to server.\nPlease check server status.", Alert.AlertType.ERROR);
        }
        return isConnected;
    }
}