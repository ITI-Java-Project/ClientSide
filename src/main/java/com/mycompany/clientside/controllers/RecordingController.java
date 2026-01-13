package com.mycompany.clientside.controllers;

import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.InvitationCallback;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.matchRecording.Match;
import com.mycompany.clientside.matchRecording.MatchRecorder;
import com.mycompany.clientside.matchRecording.Result;
import com.mycompany.clientside.service.ClientMessageListener;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.MatchResultGame;

public class RecordingController implements Initializable, InvitationCallback, GameLifecycleCallback {

    @FXML
    private VBox recordsContainer;
    @FXML
    private ImageView backBtn;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;

    private MatchRecorder matchRecorder;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    private ConnectionStrategy conn;
    private ClientMessageListener listener;
    private InvitationDialogController currentInviteDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matchRecorder = new MatchRecorder("");
        loadAllMatches();
         conn = ConnectionManager.getConnection();
        listener = conn.getClientMessageListener();
        if (listener != null) {
            listener.setCallback(this);
        }
    }

    private void loadAllMatches() {
        List<Match> matches = matchRecorder.loadAllMatches();

        if (matches.isEmpty()) {
            Label noRecordsLabel = new Label("No match recordings found");
            noRecordsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            recordsContainer.getChildren().add(noRecordsLabel);
            return;
        }

        for (Match match : matches) {
            String playerName = match.getOponentName();
            String date = dateFormat.format(match.getDate());
            String time = timeFormat.format(match.getDate());
            MatchResultGame result = convertResultToMatchResultGame(match.getResult());

            loadRecord(playerName, date, time, result, match);
        }
    }

    private MatchResultGame convertResultToMatchResultGame(Result result) {
        switch (result) {
            case WIN:
                return MatchResultGame.WIN;
            case LOSE:
                return MatchResultGame.LOSE;
            default:
                return MatchResultGame.DRAW;
        }
    }

    private void loadRecord(String playerName, String date, String time, MatchResultGame result, Match match) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/RecordingCard.fxml")
            );

            HBox card = loader.load();
            RecordingCardController controller = loader.getController();

            controller.setData(playerName, date, time, result);

            // Make card clickable to view match replay
            card.setOnMouseClicked(event -> openMatchReplay(match));
            card.setStyle(card.getStyle() + "-fx-cursor: hand;");

            recordsContainer.getChildren().add(card);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMatchReplay(Match match) {
        SceneNavigator.navigateTo(recordsContainer, "/fxml/RecordedMatch.fxml",
                (RecordedMatchController controller) -> {
                    controller.setMatch(match);
                });
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        SceneNavigator.back(backBtn);
    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        SceneNavigator.navigateTo(leaderBoardText, "/fxml/LeaderBoard.fxml");
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        StackPane root = (StackPane) settingsIcon.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
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
