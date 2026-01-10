package com.mycompany.clientside.controllers;

import com.mycompany.clientside.game.*;
import com.mycompany.clientside.matchRecording.*;
import com.mycompany.clientside.utils.DialogUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class GameController implements Initializable {

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
    private Label player2Score;
    @FXML
    private Button settingsButton;
    @FXML
    private GridPane gameBoard;

    private GameStartegy gameStrategy;
    private char currentPlayer;
    private boolean waitingForMove;
    private boolean gameOver;
    private MatchRecorder matchRecorder;
    private String opponentName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GameController INITIALIZED");
        currentPlayer = 'X';
        waitingForMove = false;
        gameOver = false;
    }

    public void setGameStrategy(GameStartegy strategy) {
        this.gameStrategy = strategy;
        this.currentPlayer = 'X';

        if (strategy instanceof AiGame) {
            this.opponentName = "AI";
        } else if (strategy instanceof LocalGame) {
            this.opponentName = player2Name != null ? player2Name.getText() : "Player 2";
        } else {
            this.opponentName = "Opponent";
        }

        matchRecorder = new MatchRecorder(opponentName);

        resetGame();
    }

    @FXML
    private void handleCellClick(ActionEvent event) {
        if (gameOver || waitingForMove) {
            return;
        }

        Button clicked = (Button) event.getSource();
        if (!clicked.getText().isEmpty()) {
            return;
        }

        Integer row = GridPane.getRowIndex(clicked);
        Integer col = GridPane.getColumnIndex(clicked);
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;

        makeMove(row, col, clicked);
    }

    private void makeMove(int row, int col, Button button) {
        button.setText(String.valueOf(currentPlayer));
        gameStrategy.play(row, col, currentPlayer);
        matchRecorder.addMove(new Move(row, col, currentPlayer));

        if (checkGameOver()) {
            return;
        }
        handleNextTurn();
    }

    private void handleNextTurn() {
        if (gameStrategy instanceof AiGame) {
            waitForAiMove();
        } else if (gameStrategy instanceof LocalGame) {
            switchPlayer();
        } else {
            switchPlayer();
        }
    }

    private void waitForAiMove() {
        waitingForMove = true;
        setBoardEnabled(false);

        PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
        pause.setOnFinished(e -> {
            recordAiMove();
            syncBoardWithUI();

            if (!checkGameOver()) {
                waitingForMove = false;
                setBoardEnabled(true);
            }
        });
        pause.play();
    }

    private void recordAiMove() {
        char[][] board = Board.getInstance().getBoard();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = getCellButton(i, j);
                if (btn != null && btn.getText().isEmpty() && board[i][j] != '\0') {
                    matchRecorder.addMove(new Move(i, j, board[i][j]));
                    return;
                }
            }
        }
    }

    private Button getCellButton(int row, int col) {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                r = (r == null) ? 0 : r;
                c = (c == null) ? 0 : c;

                if (r == row && c == col) {
                    return (Button) node;
                }
            }
        }
        return null;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkGameOver() {
        int[][] winningCells = gameStrategy.winner();

        if (winningCells != null) {
            handleWin(winningCells);
            return true;
        }

        if (isBoardFull()) {
            handleDraw();
            return true;
        }

        return false;
    }

    private void syncBoardWithUI() {
        char[][] board = Board.getInstance().getBoard();

        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;

                Integer r = GridPane.getRowIndex(btn);
                Integer c = GridPane.getColumnIndex(btn);
                r = (r == null) ? 0 : r;
                c = (c == null) ? 0 : c;

                if (btn.getText().isEmpty() && board[r][c] != '\0') {
                    btn.setText(String.valueOf(board[r][c]));
                }
            }
        }
    }

    private boolean isBoardFull() {
        char[][] board = Board.getInstance().getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private void handleWin(int[][] winningCells) {
        gameOver = true;
        highlightWinningCells(winningCells);
        setBoardEnabled(false);

        char winner = Board.getInstance().getBoard()[winningCells[0][0]][winningCells[0][1]];
        updateScore(winner);

        Result result;
        if (winner == 'X') {
            result = Result.WIN;
        } else {
            result = Result.LOSE;
        }

        matchRecorder.endMatch(result);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            showGameOverDialog(winner + " wins!");
            resetGame();
        });
        pause.play();
    }

    private void handleDraw() {
        gameOver = true;
        setBoardEnabled(false);

        int draws = Integer.parseInt(drawScore.getText());
        drawScore.setText(String.valueOf(draws + 1));

        matchRecorder.endMatch(Result.DRAW);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            showGameOverDialog("It's a draw!");
            resetGame();
        });
        pause.play();
    }

    private void updateScore(char winner) {
        if (winner == 'X') {
            if (player1Score != null) {
                int score = Integer.parseInt(player1Score.getText());
                player1Score.setText(String.valueOf(score + 1));
            }
        } else {
            if (player2Score != null) {
                int score = Integer.parseInt(player2Score.getText());
                player2Score.setText(String.valueOf(score + 1));
            }
        }
    }

    private void highlightWinningCells(int[][] cells) {
        for (int[] pos : cells) {
            for (Node node : gameBoard.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                r = (r == null) ? 0 : r;
                c = (c == null) ? 0 : c;

                if (node instanceof Button && r == pos[0] && c == pos[1]) {
                    ((Button) node).setTextFill(javafx.scene.paint.Color.RED);
                }
            }
        }
    }

    private void setBoardEnabled(boolean enabled) {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(!enabled);
            }
        }
    }

    private void resetGame() {
        gameOver = false;
        waitingForMove = false;
        currentPlayer = 'X';
        Board.getInstance().resetBoard();

        if (opponentName != null) {
            matchRecorder = new MatchRecorder(opponentName);
        }

        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setText("");
                btn.setDisable(false);
                btn.setTextFill(javafx.scene.paint.Color.BLACK);
            }
        }
    }

    private void showGameOverDialog(String message) {
        System.out.println("Game Over: " + message);
    }

    @FXML
    private void handleSettingClick(ActionEvent event) {
        Parent root = ((Node) event.getSource()).getScene().getRoot();
        DialogUtil.openDialog(root,"/fxml/CustomDialog.fxml");
    }
}
