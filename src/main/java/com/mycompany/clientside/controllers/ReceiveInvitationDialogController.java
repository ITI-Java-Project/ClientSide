package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ReceiveInvitationDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;

    @FXML
    private VBox dialogCard;

    @FXML
    private Label titleLabel;

    @FXML
    private ToggleButton recordToggle;

    @FXML
    private Button acceptButton;

    @FXML
    private Button denyButton;

    private StackPane rootPane;
    private boolean isRecording = false;

    private Runnable onAccept;
    private Runnable onDeny;

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
        titleLabel.setText(playerName + " wants to play with you!");
    }

    public void setOnAccept(Runnable onAccept) {
        this.onAccept = onAccept;
    }

    public void setOnDeny(Runnable onDeny) {
        this.onDeny = onDeny;
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
    private void accept() {
        SoundManager.getInstance().playClickSound();
        if (onAccept != null) {
            onAccept.run();
        }
        closeDialog();
    }

    @FXML
    private void deny() {
        SoundManager.getInstance().playClickSound();
        if (onDeny != null) {
            onDeny.run();
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
