package com.mycompany.clientside.controllers;

import com.mycompany.clientside.auth.Authentication;
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

    private LocalConnection con;
    private Authentication auth;
    private Window owner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            owner = signupButton.getScene().getWindow();
            con = new LocalConnection(owner);
            auth = new Authentication(con);
        });
    }

    @FXML
    private void handleSettings(MouseEvent event) {
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        System.out.println("userName : " + usernameField.getText());
        System.out.println("Email : " + emailField.getText());
        System.out.println("Password : " + passwordField.getText());
        boolean isRegistered = auth.signup(usernameField.getText(), emailField.getText(), passwordField.getText());
        if (isRegistered) {
            SceneNavigator.navigateTo(signupButton, "/fxml/Start.fxml");
        } else {
            AlertUtil.showAlert("Signup Failed", "This Email is used before please enter another email", Alert.AlertType.WARNING, owner);
        }
    }
}
