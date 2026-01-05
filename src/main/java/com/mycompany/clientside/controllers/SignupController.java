package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.LocalConnection;
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
    private LocalConnection con;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = new LocalConnection();
    }    
    
    @FXML
    private void handleSettings(MouseEvent event) {}

    @FXML
    private void handleSignup(ActionEvent event) {
        System.out.println("userName : " + usernameField.getText());
        System.out.println("Email : " + emailField.getText());
        System.out.println("Password : " + passwordField.getText());
        boolean isRegistered = con.signup(usernameField.getText(), emailField.getText(), passwordField.getText());
        if(isRegistered) SceneNavigator.navigateTo(signupButton, "/fxml/Start.fxml");
    }
}
