/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.game.GameStartegy;
import com.mycompany.clientside.game.LocalGame;
import com.mycompany.clientside.matchRecording.MatchRecorder;
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
    private GameStartegy gameStrategy;
    private MatchRecorder matchRecorder;
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GameController INITIALIZED");
        xTurn = true;
    }
    
    public void setGameStrategy(GameStartegy gameStartegy){
        this.gameStrategy=gameStrategy;
    }

    @FXML
    private void handleCellClick(ActionEvent event) {
        Button clicked = (Button) event.getSource();

        
        if (!clicked.getText().isEmpty()) return;
        char move = xTurn ? 'X' : 'O';
        clicked.setText(Character.toString(move));
        xTurn = !xTurn;

       
        Integer row = GridPane.getRowIndex(clicked);
        Integer col = GridPane.getColumnIndex(clicked);
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;
        gameStrategy.play(row, col, move);
        int[][] winningCells = gameStrategy.winner();
        if (winningCells != null) {
            highlightWinningCells(winningCells);
            disableBoard();
        }
        
    }

    @FXML
    private void handleSettingClick(ActionEvent event) {
        Parent root = ((Node) event.getSource()).getScene().getRoot();
        DialogUtil.openSettingsDialog(root);
    }
    
    private void disableBoard() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setDisable(true);
            }
        }
    }
    
    private void highlightWinningCells(int[][] cells) {
        for (int[] pos : cells) {
            int row = pos[0];
            int col = pos[1];

            for (Node node : gameBoard.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                r = (r == null) ? 0 : r;
                c = (c == null) ? 0 : c;

                if (node instanceof Button && r == row && c == col) {
                    ((Button) node).setTextFill(javafx.scene.paint.Color.RED);
                }
            }
        }
    }
    
}
