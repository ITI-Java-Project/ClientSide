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
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class SignupController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField emailField;
    @FXML
    private Button signupButton;
    @FXML
    private TextField serverIpField;

    private ConnectionStrategy con;
    private Authentication auth;
    private Window owner;
    private InetAddress serverIp;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverIpField.setText("127.0.0.1");
        Platform.runLater(() -> {
            owner = signupButton.getScene().getWindow();
        });

        usernameField.textProperty().addListener((obs, old, newVal) -> hideError());
        emailField.textProperty().addListener((obs, old, newVal) -> hideError());
        passwordField.textProperty().addListener((obs, old, newVal) -> hideError());
        serverIpField.textProperty().addListener((obs, old, newVal) -> hideError());
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        SoundManager.getInstance().playClickSound();
        hideError();

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String serverIpText = serverIpField.getText().trim();

        if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {
            showError("Please fill in all fields.");
            usernameField.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            showError("Please enter your username.");
            usernameField.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            showError("Please enter your email.");
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters long.");
            usernameField.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address.");
            emailField.requestFocus();
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            passwordField.requestFocus();
            return;
        }

        try {
            serverIp = InetAddress.getByName(serverIpText);
            ConnectionManager.init(new LocalConnection(serverIp, owner));
            con = ConnectionManager.getConnection();
            auth = new Authentication(con);
        } catch (UnknownHostException e) {
            showError("Invalid server IP address!");
            serverIpField.requestFocus();
            return;
        }

        signupButton.setDisable(true);

        new Thread(() -> {
            if (!con.connectToServer()) {
                Platform.runLater(() -> {
                    showError("Cannot connect to server. Please try again later.");
                    signupButton.setDisable(false);
                });
                return;
            }

            Player player = auth.signup(username, email, password);

            Platform.runLater(() -> {
                signupButton.setDisable(false);

                if (player != null) {
                    UserSession.getInstance().setCurrentUser(player);
                    con.startListener(null);
                    System.out.println(UserSession.getInstance().getCurrentUser().toString());
                    SceneNavigator.navigateTo(signupButton, "/fxml/Start.fxml");
                } else {
                    showError("This email is already in use. Please use a different email.");
                }
            });

        }).start();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
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
