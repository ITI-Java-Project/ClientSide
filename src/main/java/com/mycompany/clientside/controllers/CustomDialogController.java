package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    @FXML
    private void goBack() {
        SceneNavigator.back(rootPane);
    }

    private void closeDialog() {
        rootPane.getChildren().forEach(node -> node.setEffect(null));
        rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
    }
}