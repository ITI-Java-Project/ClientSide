package com.mycompany.clientside.controllers;

import com.mycompany.clientside.auth.Authentication;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.connection.LocalConnection;
import com.mycompany.clientside.utils.AlertUtil;
import com.mycompany.clientside.utils.SceneNavigator;
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
            conn = new LocalConnection(owner);
            auth = new Authentication(conn);
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (conn.connectToServer()) {
            if (username.isEmpty() || password.isEmpty()) {
                AlertUtil.showAlert("Warning", "PLease Fill username and password fields", Alert.AlertType.WARNING, owner);
            } else {
                boolean isSuccess = auth.login(usernameField.getText(), passwordField.getText());
                if (isSuccess) {
                    SceneNavigator.navigateTo(loginButton, "/fxml/Start.fxml");
                } else {
                    AlertUtil.showAlert("Login failed", "PLease Ensure you entered corrcrt username and password ", Alert.AlertType.ERROR, owner);
                }
            }
        }
    }

    @FXML
    private void handleSettings(MouseEvent event) {
    }
}
