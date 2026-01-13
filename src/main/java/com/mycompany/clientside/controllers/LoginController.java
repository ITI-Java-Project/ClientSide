package com.mycompany.clientside.controllers;

import com.mycompany.clientside.auth.Authentication;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.connection.LocalConnection;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.dto.UserSession;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

    private ConnectionStrategy conn;
    private Authentication auth;
    private Window owner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            owner = loginButton.getScene().getWindow();
            ConnectionManager.init((new LocalConnection(owner)));
            conn = ConnectionManager.getConnection();
            auth = new Authentication(conn);
        });
        
        // Clear error when user starts typing
        usernameField.textProperty().addListener((obs, old, newVal) -> hideError());
        passwordField.textProperty().addListener((obs, old, newVal) -> hideError());
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        SoundManager.getInstance().playClickSound();
        hideError();

        // Step 1: Get and validate input fields FIRST (on UI thread, no server call)
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

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

        // Step 2: Only if validation passes, disable button and make server call
        loginButton.setDisable(true);
        
        new Thread(() -> {
            // Step 3: Try to connect to server
            if (!conn.connectToServer()) {
                Platform.runLater(() -> {
                    showError("Cannot connect to server. Please try again later.");
                    loginButton.setDisable(false);
                });
                return;
            }

            // Step 4: Attempt login
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

    @FXML
    private void handleSettings(MouseEvent event) {
    }
}