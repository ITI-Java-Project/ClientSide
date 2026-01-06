package com.mycompany.clientside.auth;

import com.mycompany.clientside.connection.ConnectionStrategy;
import java.io.IOException;

public class Authentication {

    private ConnectionStrategy conn;

    public Authentication(ConnectionStrategy conn) {
        this.conn = conn;
    }

    public boolean login(String username, String password) {
        if (!conn.isIsConnected()) {
            conn.connectToServer();
        }

        if (!conn.isIsConnected()) {
            return false;
        }

        String command = "LOGIN " + username + " " + password;
        conn.getWriter().println(command);

        try {
            if (conn.getReader().readLine().startsWith("LOGIN_SUCCESS")) {
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean signup(String username, String email, String password) {
        if (!conn.isIsConnected()) {
            conn.connectToServer();
        }

        if (!conn.isIsConnected()) {
            return false;
        }

        String command = "REGISTER " + username + " " + email + " " + password;
        conn.getWriter().println(command);
        try {
            if (conn.getReader().readLine().startsWith("REGISTERED")) {
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}