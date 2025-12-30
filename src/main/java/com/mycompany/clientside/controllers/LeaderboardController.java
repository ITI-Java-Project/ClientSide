package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class LeaderboardController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    // Back arrow action
    @FXML
    private void handleBack(MouseEvent event) {
        System.out.println("Back Arrow Clicked.");
        // TODO : Add back Arrow Action 
    }


    // Open Settings alert
    @FXML
    private void goToSettings(MouseEvent event) {
        System.out.println("open Settings Alert.");
        // TODO : Add Open Settings Alert logic.
    }
    
}
