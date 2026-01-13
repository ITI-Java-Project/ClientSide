package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
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
        SoundManager.getInstance().playClickSound();
        SceneNavigator.navigateTo(loginButton, "/fxml/Login.fxml");
    }

    @FXML
    private void handleSignup() {
        SoundManager.getInstance().playClickSound();
        SceneNavigator.navigateTo(signupButton, "/fxml/Signup.fxml");
    }
}
