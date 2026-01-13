package com.mycompany.clientside.controllers;

import com.mycompany.clientside.auth.Authentication;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.connection.LocalConnection;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.dto.UserSession;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField serverIpField;

    private ConnectionStrategy conn;
    private Authentication auth;
    private Window owner;
    private InetAddress serverIp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverIpField.setText("127.0.0.1");

        Platform.runLater(() -> {
            owner = loginButton.getScene().getWindow();
        });

        usernameField.textProperty().addListener((obs, old, newVal) -> hideError());
        passwordField.textProperty().addListener((obs, old, newVal) -> hideError());
        serverIpField.textProperty().addListener((obs, old, newVal) -> hideError());

    }

    @FXML
    private void handleLogin(ActionEvent event) {
        SoundManager.getInstance().playClickSound();
        hideError();

        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String serverIpText = serverIpField.getText().trim();

        if (username.isEmpty() && password.isEmpty()) {
            showError("Please enter your username and password.");
            return;
        }

        if (username.isEmpty()) {
            showError("Please enter your username.");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        try {
            serverIp = InetAddress.getByName(serverIpText);
            ConnectionManager.init(new LocalConnection(serverIp, owner));
            conn = ConnectionManager.getConnection();
            auth = new Authentication(conn);
        } catch (UnknownHostException e) {
            showError("Invalid server IP address!");
            serverIpField.requestFocus();
            return;
        }

        loginButton.setDisable(true);

        new Thread(() -> {
            if (!conn.connectToServer()) {
                Platform.runLater(() -> {
                    showError("Cannot connect to server. Please try again later.");
                    loginButton.setDisable(false);
                });
                return;
            }

            Player player = auth.login(username, password);

            Platform.runLater(() -> {
                loginButton.setDisable(false);

                if (player != null) {
                    UserSession.getInstance().setCurrentUser(player);
                    conn.startListener(null);
                    SceneNavigator.navigateTo(loginButton, "/fxml/Start.fxml");
                    System.out.println(UserSession.getInstance().getCurrentUser().toString());
                } else {
                    showError("Invalid username or password. Please try again.");
                }
            });

        }).start();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}