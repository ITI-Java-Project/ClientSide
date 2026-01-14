package com.mycompany.clientside.controllers;

import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.InvitationCallback;
import com.mycompany.clientside.callbacks.PlayersCallback;
import com.mycompany.clientside.callbacks.SessionCallback;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.dto.Session;
import com.mycompany.clientside.dto.UserSession;
import com.mycompany.clientside.game.OnlineGame;
import com.mycompany.clientside.service.ClientMessageCallback;
import com.mycompany.clientside.service.ClientMessageListener;
import com.mycompany.clientside.service.PlayerService;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
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

public class AvaliablePlayersController implements Initializable, InvitationCallback, GameLifecycleCallback, PlayersCallback, SessionCallback {

    @FXML
    private ImageView backIcon;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private VBox playersListContainer;
    @FXML
    private ImageView refreshIcon;

    private ConnectionStrategy conn;
    private Window owner;
    private PlayerService playerService;
    private ObservableList<Player> avaliablesPlayers;
    private Thread th;
    private Session currentSession;

    private InvitationDialogController currentInviteDialog;
    private ClientMessageListener listener;
    private boolean myTurn = false;
    private char myChar = 'O';
    private ReceiveInvitationDialogController receiveInvitationDialogController;
    private boolean isSender=true;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            owner = backIcon.getScene().getWindow();
            conn = ConnectionManager.getConnection();
            playerService = new PlayerService(conn);
            avaliablesPlayers = observableArrayList();

            listener = conn.getClientMessageListener();
            if (listener != null) {
                listener.setCallback(this);
            }

            requestPlayersList();
        });
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        SceneNavigator.back((Node) event.getSource());

    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        SceneNavigator.navigateTo(leaderBoardText, "/fxml/LeaderBoard.fxml");
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        Parent root = settingsIcon.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }

    @FXML
    private void handleRefresh(MouseEvent event) {
        SoundManager.getInstance().playClickSound();
        avaliablesPlayers.clear();
        playersListContainer.getChildren().clear();

        requestPlayersList();
    }

    private void requestPlayersList() {
        conn.getWriter().println("GET_AVAILABLE_PLAYERS");
    }

    private void challengeAction(Player player) {
        Parent root = settingsIcon.getScene().getRoot();
        System.out.println("Player Details : " + player.getName() + "- ID : " + player.getId());
        currentInviteDialog = GameDialogs.openInvitationDialog(root, player.getName());
        conn.getWriter().println("INVITE " + player.getId()); // send server INVITE 5
    }

    @Override
    public void loadPlayers(List<Player> players) {
        Platform.runLater(() -> {
            avaliablesPlayers.clear();
            avaliablesPlayers.addAll(players);
            showPlayers(avaliablesPlayers);
        });
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
        boolean isRecord = isSender ? currentInviteDialog.isRecording() 
                                    : receiveInvitationDialogController.isRecording();
        
        Parent root = settingsIcon.getScene().getRoot();
        
        // Get current user to determine which player name to show where
        int currentUserId = UserSession.getInstance().getCurrentUser().getId();
        
        Platform.runLater(() -> {
            SceneNavigator.navigateTo(root, "/fxml/Game.fxml", (GameController c) -> {
                c.setGameStrategy(new OnlineGame());
                c.setMyTurn(myTurn);
                c.setMyChar(myChar);
                c.setRecording(isRecord);
                
                // Set player names from session data
                if (currentSession != null) {
                    if (currentUserId == currentSession.getPlayer1Id()) {
                        // I am player 1
                        c.setPlayerNames(currentSession.getPlayer1Name(), 
                                        currentSession.getPlayer2Name());
                    } else {
                        // I am player 2
                        c.setPlayerNames(currentSession.getPlayer2Name(), 
                                        currentSession.getPlayer1Name());
                    }
                }
            });
        });
    }

    @Override
    public void onInviteReceived(String[] parts) {
        myTurn = true;
        myChar = 'X';
        isSender=false;
        Parent root = leaderBoardText.getScene().getRoot();
        Platform.runLater(() -> {
            receiveInvitationDialogController=GameDialogs.openReceiveInvitationDialog(root, parts[2],
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
    public void endGame() {
    }

    @Override
    public void loadLeaderBoard(List<Player> players) {
    }

    @Override
    public void onSessionDataReceived(Session session) {
        // Just log it - GameController will handle displaying names
        System.out.println("AvailablePlayersController received session: " + session);
    }

}
