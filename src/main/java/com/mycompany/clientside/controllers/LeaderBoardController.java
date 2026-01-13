package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.service.ClientMessageCallback;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class LeaderBoardController implements Initializable, ClientMessageCallback {

    @FXML
    private ImageView backBtn;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private VBox playersListContainer;

    private ConnectionStrategy conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            conn = ConnectionManager.getConnection();
            conn.startListener(this);
            conn.getWriter().println("GET_ALL_PLAYERS");
        });
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        SceneNavigator.back((Node) event.getSource());
    }

    @FXML
    private void goToSettings(MouseEvent event) {
        SoundManager.getInstance().playClickSound();

        Parent root = settingsIcon.getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }

    @Override
    public void loadLeaderBoard(List<Player> players) {
        Platform.runLater(() -> renderPlayers(players));
    }

    private void renderPlayers(List<Player> players) {
        playersListContainer.getChildren().clear();

        if (players == null || players.isEmpty()) {
            showEmptyState();
            return;
        }

        int rank = 1;
        for (Player player : players) {
            playersListContainer.getChildren()
                    .add(createPlayerCard(player, rank++));
        }
    }

    private void showEmptyState() {
        Label label = new Label("No players found");
        label.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 24;");
        playersListContainer.getChildren().add(label);
    }

    private Node createPlayerCard(Player player, int rank) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/LeaderBoardCard.fxml")
            );

            Node card = loader.load();
            LeaderBoardCardController controller = loader.getController();
            controller.setData(player, rank);

            return card;

        } catch (Exception e) {
            e.printStackTrace();
            return new Label("Failed to load player card");
        }
    }

    @Override
    public void loadPlayers(List<Player> players) {
    }

    @Override
    public void startGame() {
    }

    @Override
    public void onInviteReceived(String[] parts) {
    }

    @Override
    public void onInviteRejected(String[] parts) {
    }

    @Override
    public void onMoveReceived(int r, int c, char p) {
    }

    @Override
    public void onWin(char winner) {
    }

    @Override
    public void onLose(char loser) {
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
    public void endGame() {
    }
}
