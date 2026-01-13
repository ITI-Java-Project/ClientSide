package com.mycompany.clientside.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import java.io.IOException;

public class Authentication {

    private ConnectionStrategy conn;

    public Authentication(ConnectionStrategy conn) {
        this.conn = conn;
    }

    public Player login(String username, String password) {
        if (!conn.isIsConnected()) {
            conn.connectToServer();
        }

        if (!conn.isIsConnected()) {
            return null;
        }

        String command = "LOGIN " + username + " " + password;
        conn.getWriter().println(command);

        try {
            String response = conn.getReader().readLine();

            if (response.startsWith("LOGIN_SUCCESS")) {
                String json = response.substring("LOGIN_SUCCESS ".length());

                Gson gson = new GsonBuilder().create();
                return gson.fromJson(json, Player.class);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Player signup(String username, String email, String password) {
        if (!conn.isIsConnected()) {
            conn.connectToServer();
        }

        if (!conn.isIsConnected()) {
            return null;
        }

        conn.getWriter().println("REGISTER " + username + " " + email + " " + password);

        try {
            String response = conn.getReader().readLine();

            if (response.startsWith("REGISTER_SUCCESS")) {
                String json = response.substring("REGISTER_SUCCESS ".length());

                Gson gson = new GsonBuilder().create();
                return gson.fromJson(json, Player.class);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
