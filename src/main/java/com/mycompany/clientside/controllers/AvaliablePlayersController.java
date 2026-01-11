package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.service.ClientMessageCallback;
import com.mycompany.clientside.service.PlayerService;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Window;

public class AvaliablePlayersController implements Initializable, ClientMessageCallback {

    @FXML
    private ImageView bgImage;
    @FXML
    private ImageView backIcon;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private VBox playersListContainer;
    @FXML
    private Label challengeLabel;

    private ConnectionStrategy conn;
    private Window owner;
    private PlayerService playerService;
    private List<Player> avaliablesPlayers;
    private Thread th;

    private InvitationDialogController currentInviteDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            owner = backIcon.getScene().getWindow();
            conn = ConnectionManager.getConnection();
            playerService = new PlayerService(conn);
            avaliablesPlayers = new ArrayList<>();
            conn.startListener(this);

            conn.getWriter().println("GET_AVAILABLE_PLAYERS");

        });
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SceneNavigator.back((Node) event.getSource());
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
    private void challengeAction(Player player) {
        Parent root = settingsIcon.getScene().getRoot();
        System.out.println("Player Details : " + player.getName() + "- ID : " + player.getId());
        currentInviteDialog = GameDialogs.openInvitationDialog(root, player.getName());
        conn.getWriter().println("INVITE " + player.getId()); // send server INVITE 5
    }

    @Override
    public void loadPlayers(List<Player> players) {
        Platform.runLater(() -> showPlayers(players));
    }

    private void showPlayers(List<Player> players) {
        playersListContainer.getChildren().clear();

        if (players.isEmpty()) {
            Label emptyLabel = new Label("No players available");
            emptyLabel.setFont(Font.font("Comic Sans MS", 24));
            emptyLabel.setTextFill(Color.web("#333"));
            emptyLabel.setAlignment(Pos.CENTER);

            playersListContainer.setAlignment(Pos.CENTER);
            playersListContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Player player : players) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerCard.fxml"));
                HBox card = loader.load();

                Label nameLabel = (Label) card.lookup("#playerNameLabel");
                Label scoreLabel = (Label) card.lookup("#playerScoreLabel");
                Label challengeLabel = (Label) card.lookup("#challengeLabel");

                nameLabel.setText(player.getName());
                scoreLabel.setText("Score: " + player.getScore());
                challengeLabel.setOnMouseClicked(e -> challengeAction(player));

                playersListContainer.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startGame() {
        System.out.println("=================================");
        Parent root = settingsIcon.getScene().getRoot();
        Platform.runLater(() -> {
            SceneNavigator.navigateTo(root, "/fxml/Game.fxml");
        });
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

    @Override
    public void onMoveReceived(int row, int col, char player) {
    }

    @Override
    public void onWin(char winner) {
    }

    @Override
    public void onDraw() {
    }

    @Override
    public void onNotYourTurn() {
    }

    @Override
    public void onCellOccupied() {
    }

    @Override
    public void onLose(char loser) {
    }

    @Override
    public void endGame() {
    }
}
