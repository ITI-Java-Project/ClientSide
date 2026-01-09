package com.mycompany.clientside.connection;


public class ConnectionManager {

    private static ConnectionStrategy connection;

    private ConnectionManager() {}

    public static void init(ConnectionStrategy connection) {
        if (ConnectionManager.connection == null) {
            ConnectionManager.connection = connection;
        }
    }

    public static ConnectionStrategy getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Connection not initialized");
        }
        return connection;
    }
}