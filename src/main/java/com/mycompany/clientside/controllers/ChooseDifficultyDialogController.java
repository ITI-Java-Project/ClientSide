package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Difficulty;

public class ChooseDifficultyDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;
    @FXML
    private VBox dialogCard;
    @FXML
    private Label titleLabel;
    @FXML
    private Button easyButton;
    @FXML
    private Button mediumButton;
    @FXML
    private Button hardButton;
    @FXML
    private Button cancelButton;

    private StackPane rootPane;
    private Consumer<Difficulty> onDifficultySelected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Optional: Add hover effects
        setupButtonHoverEffect(easyButton, "#2ecc71", "#27ae60");
        setupButtonHoverEffect(mediumButton, "#f39c12", "#e67e22");
        setupButtonHoverEffect(hardButton, "#e74c3c", "#c0392b");
    }

    private void setupButtonHoverEffect(Button button, String normalColor, String hoverColor) {
        String baseStyle = "-fx-border-color: black; -fx-border-width: 3; -fx-background-radius: 15; -fx-border-radius: 15;";
        
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: " + hoverColor + "; " + baseStyle)
        );
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: " + normalColor + "; " + baseStyle)
        );
    }

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setOnDifficultySelected(Consumer<Difficulty> callback) {
        this.onDifficultySelected = callback;
    }

    @FXML
    private void chooseEasy(ActionEvent event) {
        selectDifficulty(Difficulty.EASY);
    }

    @FXML
    private void chooseMedium(ActionEvent event) {
        selectDifficulty(Difficulty.MEDIUM);
    }

    @FXML
    private void chooseHard(ActionEvent event) {
        selectDifficulty(Difficulty.HARD);
    }

    private void selectDifficulty(Difficulty difficulty) {
        closeDialog();
        if (onDifficultySelected != null) {
            onDifficultySelected.accept(difficulty);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        if (rootPane != null && rootPane.getChildren().size() > 1) {
            // Remove blur effect from main content
            rootPane.getChildren().get(0).setEffect(null);
            // Remove this dialog
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }
}