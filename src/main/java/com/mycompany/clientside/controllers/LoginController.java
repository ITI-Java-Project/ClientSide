package com.mycompany.clientside.controllers;

import com.mycompany.clientside.auth.Authentication;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.connection.LocalConnection;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.dto.UserSession;
import com.mycompany.clientside.utils.AlertUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

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
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        SoundManager.getInstance().playClickSound();

        new Thread(() -> {

            if (!conn.connectToServer()) {
                Platform.runLater(() -> AlertUtil.showAlert(
                        "Error", "Cannot connect to server", Alert.AlertType.ERROR, owner
                ));
                return;
            }

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                Platform.runLater(() -> AlertUtil.showAlert(
                        "Warning",
                        "Please fill username and password fields",
                        Alert.AlertType.WARNING, owner
                ));
                return;
            }
            Player player = auth.login(username, password);

            Platform.runLater(() -> {
                if (player != null) {
                    UserSession.getInstance().setCurrentUser(player);
                    conn.startListener(null);
                    SceneNavigator.navigateTo(loginButton, "/fxml/Start.fxml");
                    System.out.println(UserSession.getInstance().getCurrentUser().toString());;
                } else {
                    AlertUtil.showAlert(
                            "Login failed",
                            "Please ensure you entered correct username and password",
                            Alert.AlertType.ERROR, owner
                    );
                }
            });

        }).start();
    }

    @FXML
    private void handleSettings(MouseEvent event) {
    }
}
