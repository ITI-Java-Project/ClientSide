package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class RejectInvitationDialogController implements Initializable {

    @FXML
    private Label messageLabel;
    private StackPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setPlayerName(String playerName) {
        messageLabel.setText(playerName + " rejected your invitation");
    }

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @FXML
    public void closeDialog() {
        SoundManager.getInstance().playClickSound();

        rootPane.getChildren().forEach(node -> node.setEffect(null));
        rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
    }
}
