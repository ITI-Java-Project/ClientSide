/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
        
    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        Parent root = settingsIcon.getScene().getRoot();
DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }

    @FXML
    private void handleOnline(ActionEvent event) {
        gotToGame(onlineBtn);
    }

    @FXML
    private void handleLocalMultiplayer(ActionEvent event) {
        gotToGame(localMultiplayerBtn);
    }

    @FXML
    private void handleCpu(ActionEvent event) {
        gotToGame(cpuBtn);
    }

    @FXML
    private void handleLocalNetwork(ActionEvent event) {
        gotToGame(localNetworkBtn);
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SceneNavigator.back(backBtn);
    }
    private void gotToGame(Node currentNode){
        SceneNavigator.navigateTo(currentNode,"/fxml/Game.fxml");
    }

}
