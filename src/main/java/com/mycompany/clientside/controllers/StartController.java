/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author 20000
 */
public class StartController implements Initializable {

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

    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        SceneNavigator.navigateTo(leaderBoardText, "/fxml/LeaderBoard.fxml");
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        Parent root = settingsIcon.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }

    @FXML
    private void handleStart(ActionEvent event) {
        SoundManager.getInstance().playClickSound();

        SceneNavigator.navigateTo(startBtn, "/fxml/GameModes.fxml");
    }

    @FXML
    private void handleRecording(ActionEvent event) {
        SoundManager.getInstance().playClickSound();

        SceneNavigator.navigateTo(recordingBtn, "/fxml/Recording.fxml");

    }

    @FXML
    private void handleExit(ActionEvent event) {
        SoundManager.getInstance().playClickSound();

        Platform.exit();
    }
}
