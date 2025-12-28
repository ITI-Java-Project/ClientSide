package com.mycompany.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class LandingController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    // Login button action
    @FXML
    private void handleLogin() {
        System.out.println("Login clicked");
        // Add your login logic here
    }

    // Sign up button action
    @FXML
    private void handleSignup() {
        System.out.println("Sign up clicked");
        // Add your sign up logic here
    }


}