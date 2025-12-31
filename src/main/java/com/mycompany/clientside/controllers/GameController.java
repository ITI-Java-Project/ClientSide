/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author sian
 */
public class GameController implements Initializable {

    @FXML
    private ImageView player1Avatar;
    @FXML
    private Label player1Name;
    @FXML
    private Label player1Score;
    @FXML
    private Label drawScore;
    @FXML
    private Label player2Name;
    @FXML
    private ImageView player2Avatar;
    @FXML
    private Button settingsButton;
    @FXML
    private GridPane gameBoard;
    @FXML
    private Button cell00;
    @FXML
    private Button cell01;
    @FXML
    private Button cell02;
    @FXML
    private Button cell10;
    @FXML
    private Button cell11;
    @FXML
    private Button cell12;
    @FXML
    private Button cell20;
    @FXML
    private Button cell21;
    @FXML
    private Button cell22;

    private boolean xTurn; 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GameController INITIALIZED");
        xTurn = true;
    }

    @FXML
    private void handleCellClick(ActionEvent event) {
        Button clicked = (Button) event.getSource();

        
        if (!clicked.getText().isEmpty()) return;

        clicked.setText(xTurn ? "X" : "O");
        xTurn = !xTurn;

       
        Integer row = GridPane.getRowIndex(clicked);
        Integer col = GridPane.getColumnIndex(clicked);
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;

    }

    @FXML
    private void handleSettingClick(ActionEvent event) {
        Parent root = ((Node) event.getSource()).getScene().getRoot();
        DialogUtil.openSettingsDialog(root);
    }

    
}
