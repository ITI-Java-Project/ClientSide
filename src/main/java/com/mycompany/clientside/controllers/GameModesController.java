package com.mycompany.clientside.controllers;

import com.mycompany.clientside.game.AiGame;
import com.mycompany.clientside.game.GameStartegy;
import com.mycompany.clientside.game.LocalGame;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Difficulty;


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
        gotToGame(onlineBtn, new LocalGame());
    }

    @FXML
    private void handleLocalMultiplayer(ActionEvent event) {
        gotToGame(localMultiplayerBtn, new LocalGame());
    }

    @FXML
    private void handleCpu(ActionEvent event) {
        gotToGame(cpuBtn, new AiGame(Difficulty.HARD));
    }

    @FXML
    private void handleLocalNetwork(ActionEvent event) {
        SceneNavigator.navigateTo(localMultiplayerBtn, "/fxml/AvaliablePlayers.fxml");
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SceneNavigator.back(backBtn);
    }

    private void gotToGame(Node currentNode, GameStartegy gameStartegy){
        SceneNavigator.navigateTo(currentNode,"/fxml/Game.fxml",(GameController c)->{
            c.setGameStrategy(gameStartegy);
        });
    }



}