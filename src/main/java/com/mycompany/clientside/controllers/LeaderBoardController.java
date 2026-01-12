package com.mycompany.clientside.controllers;

import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.InvitationCallback;
import com.mycompany.clientside.callbacks.PlayersCallback;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.service.ClientMessageCallback;
import com.mycompany.clientside.service.ClientMessageListener;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
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

public class LeaderBoardController implements Initializable, InvitationCallback, PlayersCallback, GameLifecycleCallback {

    @FXML
    private ImageView backBtn;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private VBox playersListContainer;
    private ConnectionStrategy conn;
    private ClientMessageListener listener;
    private InvitationDialogController currentInviteDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            conn = ConnectionManager.getConnection();
            listener = conn.getClientMessageListener();
            if (listener != null) {
                listener.setCallback(this);
            }
            conn.getWriter().println("GET_ALL_PLAYERS");
        });
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SceneNavigator.back((Node) event.getSource());
    }

    @FXML
    private void goToSettings(MouseEvent event) {
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
    public void onInviteReceived(String[] parts) {
        Parent root = playersListContainer.getScene().getRoot();
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
        Parent root = playersListContainer.getScene().getRoot();
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
