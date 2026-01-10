package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
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
            System.out.println("initialize");

            new Thread(() -> {
                try {
                    String line;
                    while ((line = conn.getReader().readLine()) != null) {
                        String msg;
                        if (line.contains(":")) msg = line.substring(0,line.indexOf(":")+1).trim();
                        else msg = line.split(" ")[0];

                        switch (msg) {
                            case "INVITE_FROM":
                                String fromPlayer = line.substring("INVITE_FROM".length()).trim();
                                Platform.runLater(() -> {
                                    GameDialogs.openReceiveInvitationDialog(
                                            playersListContainer.getScene().getRoot(),
                                            fromPlayer,
                                            () -> conn.getWriter().println("INVITE_ACCEPT " + fromPlayer),
                                            () -> conn.getWriter().println("INVITE_REJECT " + fromPlayer)
                                    );
                                });
                                break;
                            case "AVAILABLE_PLAYERS:":
                                System.out.println("AVAILABLE_PLAYERS Block.");
                                loadPlayers();
                                break;
                            case "START":
                                Platform.runLater(()
                                        -> SceneNavigator.navigateTo(playersListContainer.getScene().getRoot(), "/fxml/Game.fxml")
                                );
                                break;

                            case "INVITE_REJECT":
                                System.out.println("Invitation rejected");
                                break;

                            default:
                                System.out.println("Unknown message: " + msg + "\nLine:" + line);
                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
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

    // Challenge action
    @FXML
    private void challengeAction(Player player) {
        Parent root = settingsIcon.getScene().getRoot();
        System.out.println("Player Details : " + player.getName() + "- ID : " + player.getId());
        GameDialogs.openInvitationDialog(root, player.getName());
        conn.getWriter().println("INVITE " + player.getId());

        new Thread(() -> {
            try {
                String msgFromServer = conn.getReader().readLine();
                System.out.println("Server Response: " + msgFromServer);

                String[] parts = msgFromServer.split(" ");
                String command = parts[0];

                Platform.runLater(() -> {
                    switch (command) {
                        case "START":
                            SceneNavigator.navigateTo(root, "/fxml/Game.fxml");
                            break;

                        case "INVITE_REJECT":
//                        DialogUtil.showInfo(root, "Invitation Rejected");
                            break;
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
}
