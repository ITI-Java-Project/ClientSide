package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.game.Board;
import com.mycompany.clientside.game.GameStartegy;
import com.mycompany.clientside.game.OnlineGame;
import com.mycompany.clientside.service.ClientMessageCallback;
import com.mycompany.clientside.service.ClientMessageListener;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.GridPane;

public class GameController implements Initializable, ClientMessageCallback {

    @FXML
    private ImageView player1Avatar;
    @FXML
    private Label player1Name;
    @FXML
    private Label player1Score;
    @FXML
    private Label drawScore;
    @FXML
    private Label player2Name;
    @FXML
    private ImageView player2Avatar;
    @FXML
    private Button settingsButton;
    @FXML
    private GridPane gameBoard;

    private GameStartegy gameStrategy;
    private ConnectionStrategy conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GameController INITIALIZED");
        conn = ConnectionManager.getConnection();
        conn.startListener(this);
        gameStrategy = new OnlineGame();
    }

    // USER ACTIONS
    @FXML
    private void handleCellClick(ActionEvent event) {
        Button clicked = (Button) event.getSource();

        if (!clicked.getText().isEmpty()) {
            return;
        }

        Integer row = GridPane.getRowIndex(clicked);
        Integer col = GridPane.getColumnIndex(clicked);
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;

        gameStrategy.play(row, col, ' ');

    }

    @FXML
    private void handleSettingClick(ActionEvent event) {
        Parent root = ((Node) event.getSource()).getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/GameDialog.fxml");
    }

    // CALLBACKS
    @Override
    public void onMoveReceived(int row, int col, char player) {
        System.out.println("CALLBACK MOVE " + row + "," + col + " " + player);

        Board.getInstance().setCell(row, col, player);
        updateCell(row, col, player);
    }

    @Override
    public void onWin(char winner) {
        disableBoard();
        DialogUtil.showInfo("Game Over", "Winner is: " + winner);
    }

    @Override
    public void onLose(char loser) {
        disableBoard();
        DialogUtil.showInfo("Game Over", "Loser is: " + loser);
    }

    @Override
    public void onDraw() {
        disableBoard();
        DialogUtil.showInfo("Game Over", "Draw Game");
    }

    @Override
    public void onNotYourTurn() {
        DialogUtil.showError("Invalid Move", "It is not your turn");
    }

    @Override
    public void startGame() {
        Board.getInstance().resetBoard();
        enableBoard();
        clearBoardUI();
    }

    @Override
    public void loadPlayers(List<Player> players) {
    }

    @Override
    public void onInviteReceived(String[] parts) {
    }

    @Override
    public void onInviteRejected(String[] parts) {
    }

    @Override
    public void onCellOccupied() {
    }

    // UI HELPER
    private void updateCell(int row, int col, char player) {
        Platform.runLater(() -> {
            for (Node node : gameBoard.getChildren()) {
                if (node instanceof Button) {
                    Integer r = GridPane.getRowIndex(node);
                    Integer c = GridPane.getColumnIndex(node);
                    r = (r == null) ? 0 : r;
                    c = (c == null) ? 0 : c;

                    if (r == row && c == col) {
                        ((Button) node).setText(String.valueOf(player));
                        break;
                    }
                }
            }
        });
    }

    private void disableBoard() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
    }

    private void enableBoard() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }
    }

    private void clearBoardUI() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setText("");
            }
        }
    }

    @Override
    public void endGame() {
        Parent root = player2Name.getScene().getRoot();
        SceneNavigator.navigateTo(root, "/fxml/GameModes.fxml");
    }

    @Override
    public void loadLeaderBoard(List<Player> players) {
    }
}