package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class InvitationDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;

    @FXML
    private VBox dialogCard;

    @FXML
    private Label titleLabel;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private ToggleButton recordToggle;

    private StackPane rootPane;

    private boolean isRecording = false;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.setOnMouseClicked(e -> closeDialog());
        dialogCard.setOnMouseClicked(e -> e.consume());
        updateToggleStyle();
    }

    public void setPlayerName(String playerName) {
        titleLabel.setText("Invitation sent to " + playerName);
    }

    public void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
    }

    @FXML
    private void onRecordToggle() {
        SoundManager.getInstance().playClickSound();
        isRecording = recordToggle.isSelected();
        updateToggleStyle();
    }

    private void updateToggleStyle() {
        if (isRecording) {
            recordToggle.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 20;");
            recordToggle.setText("ON");
        } else {
            recordToggle.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 20;");
            recordToggle.setText("OFF");
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    @FXML
    private void cancel() {
        SoundManager.getInstance().playClickSound();
        closeDialog();
    }

    public void closeDialog() {
        rootPane.getChildren().forEach(node -> node.setEffect(null));
        rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
    }

}
