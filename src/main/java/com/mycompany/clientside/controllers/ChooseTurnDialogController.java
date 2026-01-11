package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ChooseTurnDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;
    
    @FXML
    private VBox dialogCard;
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Button xButton;
    
    @FXML
    private Button oButton;
    
    @FXML
    private Button cancelButton;
    
    private StackPane rootPane;
    
    private Runnable onChooseX;
    private Runnable onChooseO;
    private Runnable onCancel;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.setOnMouseClicked(e -> cancel());
        dialogCard.setOnMouseClicked(e -> e.consume());
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void setOnChooseX(Runnable onChooseX) {
        this.onChooseX = onChooseX;
    }
    
    public void setOnChooseO(Runnable onChooseO) {
        this.onChooseO = onChooseO;
    }
    
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    @FXML
    private void chooseX() {
        if (onChooseX != null) {
            onChooseX.run();
        }
        closeDialog();
    }
    
    @FXML
    private void chooseO() {
        if (onChooseO != null) {
            onChooseO.run();
        }
        closeDialog();
    }
    
    @FXML
    private void cancel() {
        if (onCancel != null) {
            onCancel.run();
        }
        closeDialog();
    }

    private void closeDialog() {
        if (rootPane != null) {
            rootPane.getChildren().forEach(node -> node.setEffect(null));
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }
}