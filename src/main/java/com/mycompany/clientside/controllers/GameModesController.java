package com.mycompany.clientside.controllers;

import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.InvitationCallback;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.game.AiGame;
import com.mycompany.clientside.game.GameStartegy;
import com.mycompany.clientside.game.LocalGame;
import com.mycompany.clientside.game.OnlineGame;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Difficulty;

public class GameModesController implements Initializable, GameLifecycleCallback, InvitationCallback {

    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private Button onlineBtn;
    @FXML
    private Button localMultiplayerBtn;
    @FXML
    private Button cpuBtn;
    @FXML
    private Button localNetworkBtn;
    @FXML
    private ImageView backBtn;
    private ReceiveInvitationDialogController receiveInvitationDialogController;
    private ConnectionStrategy conn;
    private InvitationDialogController currentInviteDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        Parent root = leaderBoardText.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/Leaderboard.fxml");
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        Parent root = settingsIcon.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }

    @FXML
    private void handleOnline(ActionEvent event) {
        gotToGame(onlineBtn, new LocalGame());
    }

    @FXML
    private void handleLocalMultiplayer(ActionEvent event) {
        gotToGame(localMultiplayerBtn, new LocalGame());
    }

    @FXML
    private void handleCpu(ActionEvent event) {
        Parent root = cpuBtn.getScene().getRoot();

        GameDialogs.openChooseDifficultyDialog(root, (Difficulty difficulty) -> {
            gotToGame(cpuBtn, new AiGame(difficulty));
        });
    }

    @FXML
    private void handleLocalNetwork(ActionEvent event) {
        SoundManager.getInstance().playClickSound();
        SceneNavigator.navigateTo(localMultiplayerBtn, "/fxml/AvaliablePlayers.fxml");
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        SceneNavigator.back(backBtn);
    }

    private void gotToGame(Node currentNode, GameStartegy gameStartegy) {
        SceneNavigator.navigateTo(currentNode, "/fxml/Game.fxml", (GameController c) -> {
            c.setGameStrategy(gameStartegy);
        });
    }

    @Override
    public void onInviteReceived(String[] parts) {
        Parent root = leaderBoardText.getScene().getRoot();
        Platform.runLater(() -> {
            receiveInvitationDialogController = GameDialogs.openReceiveInvitationDialog(root, parts[2],
                    () -> {
                        conn.getWriter().println("INVITE_ACCEPT " + parts[1]);
                    },
                    () -> {
                        conn.getWriter().println("INVITE_REJECT " + parts[1]);
                    });
        });
    }

    @Override
    public void startGame() {
        System.out.println("=================================");
        Parent root = settingsIcon.getScene().getRoot();
        Platform.runLater(() -> {
            SceneNavigator.navigateTo(root, "/fxml/Game.fxml", (GameController c) -> {
                c.setGameStrategy(new OnlineGame());
                c.setMyTurn(true);
                c.setMyChar('X');
                c.setRecording(receiveInvitationDialogController.isRecording());
            });
        });
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onInviteRejected(String[] parts) {
        Parent root = leaderBoardText.getScene().getRoot();
        Platform.runLater(() -> {
            currentInviteDialog.closeDialog();
            GameDialogs.openRejectInvitationDialog(root, parts[2]);
        });
    }

}
