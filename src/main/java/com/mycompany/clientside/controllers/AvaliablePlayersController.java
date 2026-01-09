package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.service.PlayerService;
import com.mycompany.clientside.utils.DialogUtil;
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

public class AvaliablePlayersController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            owner = backIcon.getScene().getWindow();
            conn = ConnectionManager.getConnection();
            playerService = new PlayerService(conn);
            avaliablesPlayers = new ArrayList<>();
            loadPlayers();
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
        DialogUtil.openSettingsDialog(root);
    }

    // Challenge action
    @FXML
    private void challengeAction(MouseEvent event) {

    }

    private void loadPlayers() {
        new Thread(() -> {
            List<Player> players = playerService.getAllavaliablesPlayers();

            Platform.runLater(() -> showPlayers(players));
        }).start();
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

                nameLabel.setText(player.getName());
                scoreLabel.setText("Score: " + player.getScore());
                // challengeLabel.setOnMouseClicked(e -> handleChallenge(player)); TODO

                playersListContainer.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
