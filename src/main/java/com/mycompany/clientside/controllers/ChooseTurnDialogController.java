package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Turn;

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

    private Consumer<Turn> onTurnSelected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupDialogBehavior();
    }

    private void setupDialogBehavior() {
        backgroundPane.setOnMouseClicked(e -> cancel());
        dialogCard.setOnMouseClicked(e -> e.consume());
    }

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setOnTurnSelected(Consumer<Turn> onTurnSelected) {
        this.onTurnSelected = onTurnSelected;
    }

    @FXML
    private void chooseX() {
        selectTurn(Turn.X);
    }

    @FXML
    private void chooseO() {
        selectTurn(Turn.O);
    }

    private void selectTurn(Turn turn) {
        if (onTurnSelected != null) {
            onTurnSelected.accept(turn);
        }
        closeDialog();
    }

    @FXML
    private void cancel() {

        closeDialog();
    }

    private void closeDialog() {
        if (rootPane != null && !rootPane.getChildren().isEmpty()) {
            rootPane.getChildren().forEach(node -> node.setEffect(null));
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }
}
