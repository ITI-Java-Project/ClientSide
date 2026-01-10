package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RejectInvitationDialogController implements Initializable {

    @FXML
    private Label messageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setPlayerName(String playerName) {
        messageLabel.setText(playerName + " rejected your invitation");
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}
