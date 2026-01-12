package com.mycompany.clientside.controllers;

import com.mycompany.clientside.matchRecording.Match;
import com.mycompany.clientside.matchRecording.MatchRecorder;
import com.mycompany.clientside.matchRecording.Result;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.MatchResultGame;

public class RecordingController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matchRecorder = new MatchRecorder("");
        loadAllMatches();
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
        SceneNavigator.back(backBtn);
    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
        SceneNavigator.navigateTo(leaderBoardText, "/fxml/LeaderBoard.fxml");
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        StackPane root = (StackPane) settingsIcon.getScene().getRoot();
DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }
}