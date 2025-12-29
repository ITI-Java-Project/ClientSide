package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handleSettings(MouseEvent event) {}

    @FXML
    private void handleSignup(ActionEvent event) {
        SceneNavigator.navigateTo(signupButton, "/fxml/Start.fxml");
    }
}
