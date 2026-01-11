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
        SceneNavigator.navigateTo(loginButton, "/fxml/Login.fxml");
    }

    @FXML
    private void handleSignup() {
        SceneNavigator.navigateTo(signupButton, "/fxml/Signup.fxml");
    }
}
