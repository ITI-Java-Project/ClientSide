/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author 20000
 */
public class CustomDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;
@FXML
private VBox dialogCard;
    private StackPane rootPane;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @Override
public void initialize(URL url, ResourceBundle rb) {

    backgroundPane.setOnMouseClicked(e -> closeDialog());

    dialogCard.setOnMouseClicked(e -> e.consume());
}

    @FXML
    private void close() {
        Platform.exit();
    }

    private void closeDialog() {
        rootPane.getChildren().forEach(node -> node.setEffect(null));

        rootPane.getChildren().remove(
                rootPane.getChildren().size() - 1
        );
    }
}

