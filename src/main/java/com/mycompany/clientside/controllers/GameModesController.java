/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author 20000
 */
public class GameModesController implements Initializable {

    @FXML
    private ImageView bgImage;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private Button onlineBtn;
    @FXML
    private Button localMultiplayerBtn;
    @FXML
    private Button cpuBtn;
    @FXML
    private Button localNetworkBtn;
    @FXML
    private ImageView backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bgImage.setImage(
            new Image(getClass().getResource("/images/GameModeMenuBG.png").toExternalForm())
        );

        bgImage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bgImage.fitWidthProperty().bind(newScene.widthProperty());
                bgImage.fitHeightProperty().bind(newScene.heightProperty());
            }
        });
        // TODO
    }    

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
    }

    @FXML
    private void goToSettings(MouseEvent event) {
    }

    @FXML
    private void handleOnline(ActionEvent event) {
    }

    @FXML
    private void handleLocalMultiplayer(ActionEvent event) {
    }

    @FXML
    private void handleCpu(ActionEvent event) {
    }

    @FXML
    private void handleLocalNetwork(ActionEvent event) {
    }

    @FXML
    private void handleBack(MouseEvent event) {
    }
    
}
