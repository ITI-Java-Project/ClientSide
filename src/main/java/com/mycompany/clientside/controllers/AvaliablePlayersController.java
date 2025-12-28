package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class AvaliablePlayersController implements Initializable {

    @FXML
    private ImageView bgImage;
    @FXML
    private ImageView backIcon;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private VBox playersListContainer;
    @FXML
    private Label challengeLabel;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    // Back arrow action
    @FXML
    private void handleBack(MouseEvent event) {
        System.out.println("Back Arrow Clicked.");
        // TODO : Add back Arrow Action 
    }

    // Navigate to leaderboard 
    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        System.out.println("go to Leaderboard.");
        // TODO : Add Navigation Logic to Leaderboard.
    }

    // Open Settings alert
    @FXML
    private void goToSettings(MouseEvent event) {
        System.out.println("open Settings Alert.");
        // TODO : Add Open Settings Alert logic.
    }

    // Challenge action
    @FXML
    private void challengeAction(MouseEvent event) {
        System.out.println("challenge others :)");
        // TODO : Add Challenge Action
    }
}
