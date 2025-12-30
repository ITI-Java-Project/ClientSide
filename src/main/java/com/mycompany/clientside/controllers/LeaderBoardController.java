/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class LeaderBoardController implements Initializable {

    @FXML
    private ImageView bgImage;
    @FXML
    private ImageView backBtn;
    @FXML
    private ImageView settingsIcon;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    // Back arrow action
    @FXML
    private void handleBack(MouseEvent event) {
        SceneNavigator.navigateTo(backBtn, "/fxml/Start.fxml");
    }


    // Open Settings alert
    @FXML
    private void goToSettings(MouseEvent event) {
        StackPane root = (StackPane) settingsIcon.getScene().getRoot();
        DialogUtil.openSettingsDialog(root);
    }  
    
}
