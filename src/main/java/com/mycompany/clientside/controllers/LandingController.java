package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class LandingController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;


    @FXML
    private void handleLogin() { 
//        SceneNavigator.navigateTo(loginButton, "/fxml/Login.fxml");
                Parent root = loginButton.getScene().getRoot();
                GameDialogs.openReceiveInvitationDialog(
    root,
    "Ahmed",
    () -> {
        // Handle accept action
        System.out.println("Invitation accepted!");
        // Start the game, send response to server, etc.
    },
    () -> {
        // Handle deny action
        System.out.println("Invitation denied!");
        // Send rejection to server, etc.
    }
);

    }

    @FXML
    private void handleSignup() {
        SceneNavigator.navigateTo(signupButton, "/fxml/Signup.fxml");
    }
}