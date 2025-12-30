/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
public class StartController implements Initializable {

    @FXML
    private ImageView bgImage;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private Button startBtn;
    @FXML
    private Button recordingBtn;
    @FXML
    private Button exitBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bgImage.setImage(
            new Image(getClass().getResource("/images/MainMenuBG.png").toExternalForm())
        );

        bgImage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bgImage.fitWidthProperty().bind(newScene.widthProperty());
                bgImage.fitHeightProperty().bind(newScene.heightProperty());
            }
        });
    }


    @FXML
    private void goToLeaderBoard(MouseEvent event) {
    }

    @FXML
    private void goToSettings(MouseEvent event) {
    }

    @FXML
    private void handleStart(ActionEvent event) {
        SceneNavigator.navigateTo(startBtn, "/fxml/GameModes.fxml");
    }

    @FXML
    private void handleRecording(ActionEvent event) {
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
}