/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.MatchResultGame;
/**
 * FXML Controller class
 *
 * @author 20000
 */
public class RecordingCardController implements Initializable {


    @FXML
    private HBox cardRoot;
    @FXML
    private ImageView playerImage;
    @FXML
    private Label vsLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setData(String playerName, String date, String time, MatchResultGame result) {
        vsLabel.setText("VS " + playerName);
        dateLabel.setText(date);
        timeLabel.setText(time);

        applyResultStyle(result);
    }

    private void applyResultStyle(MatchResultGame result) {
    switch (result) {
        case WIN:
            cardRoot.setStyle(cardRoot.getStyle()
                + "-fx-background-color: rgba(144,238,144,0.85);");
            break;

        case LOSE:
            cardRoot.setStyle(cardRoot.getStyle()
                + "-fx-background-color: rgba(255,99,71,0.85);");
            break;

        case DRAW:
            cardRoot.setStyle(cardRoot.getStyle()
                + "-fx-background-color: rgba(200,200,200,0.85);");
            break;

        default:
            // optional safety fallback
            break;
    }
}

    
}
