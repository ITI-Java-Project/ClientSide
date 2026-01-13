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
import javafx.stage.Window;

public class LocalConnection extends ConnectionStrategy {

    public LocalConnection(InetAddress ip,Window owner) {
        this.ip = ip;
        this.portNumber = 5000;
        this.isConnected = false;
        this.listenerThread = null;
        this.owner = owner;
    }

    public LocalConnection(Window owner) {
       this.owner = owner;
        try {
            this.ip = InetAddress.getByName("127.0.0.1");
            this.portNumber = 5000;
            this.isConnected = false;
            this.listenerThread = null;

        } catch (UnknownHostException ex) {
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
        }
        return isConnected;
    }
}