/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.InvitationCallback;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.service.ClientMessageListener;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author 20000
 */
public class StartController implements Initializable, InvitationCallback, GameLifecycleCallback {

    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private Button startBtn;
    @FXML
    private Button recordingBtn;
    @FXML
    private Button exitBtn;
    private ConnectionStrategy conn;
    private ClientMessageListener listener;
    private InvitationDialogController currentInviteDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = ConnectionManager.getConnection();
        listener = conn.getClientMessageListener();
        if (listener != null) {
            listener.setCallback(this);
        }
    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        SceneNavigator.navigateTo(leaderBoardText, "/fxml/LeaderBoard.fxml");
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        Parent root = settingsIcon.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }

    @FXML
    private void handleStart(ActionEvent event) {
        SceneNavigator.navigateTo(startBtn, "/fxml/GameModes.fxml");
    }

    @FXML
    private void handleRecording(ActionEvent event) {
        SceneNavigator.navigateTo(recordingBtn, "/fxml/Recording.fxml");

    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void onInviteReceived(String[] parts) {
        Parent root = leaderBoardText.getScene().getRoot();
        Platform.runLater(() -> {
            GameDialogs.openReceiveInvitationDialog(root, parts[2],
                    () -> {
                        conn.getWriter().println("INVITE_ACCEPT " + parts[1]);
                    },
                    () -> {
                        conn.getWriter().println("INVITE_REJECT " + parts[1]);
                    });
        });
    }

    @Override
    public void onInviteRejected(String[] parts) {
        Parent root = leaderBoardText.getScene().getRoot();
        Platform.runLater(() -> {
            currentInviteDialog.closeDialog();
            GameDialogs.openRejectInvitationDialog(root, parts[2]);
        });
    }

    public void startGame() {
        System.out.println("=================================");
        Parent root = settingsIcon.getScene().getRoot();
        Platform.runLater(() -> {
            SceneNavigator.navigateTo(root, "/fxml/Game.fxml");
        });
    }

    @Override
    public void endGame() {
    }
}
