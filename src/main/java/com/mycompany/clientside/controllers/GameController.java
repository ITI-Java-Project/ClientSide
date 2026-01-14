package com.mycompany.clientside.controllers;

import com.mycompany.clientside.callbacks.GameActionCallback;
import com.mycompany.clientside.callbacks.GameLifecycleCallback;
import com.mycompany.clientside.callbacks.SessionCallback;
import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.dto.Session;
import com.mycompany.clientside.dto.SessionDAO;
import com.mycompany.clientside.dto.UserSession;
import com.mycompany.clientside.game.AiGame;
import com.mycompany.clientside.game.Board;
import com.mycompany.clientside.game.GameStartegy;
import com.mycompany.clientside.game.LocalGame;
import com.mycompany.clientside.game.OnlineGame;
import com.mycompany.clientside.matchRecording.MatchRecorder;
import com.mycompany.clientside.matchRecording.Move;
import com.mycompany.clientside.matchRecording.Result;
import com.mycompany.clientside.utils.AlertUtil;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.GameDialogs;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.Duration;
import models.Difficulty;

public class GameController implements Initializable, GameLifecycleCallback, GameActionCallback, SessionCallback {

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
    private Label player2Score;
    @FXML
    private ImageView player2Avatar;
    @FXML
    private Button settingsButton;
    @FXML
    private GridPane gameBoard;

    private GameStartegy gameStrategy;
    private boolean myTurn = true;
    private char myChar = 'X';
    private char oponentsChar = 'O';
    private boolean gameEnded = false;
    private MatchRecorder matchRecorder;
    private int player1Wins = 0;
    private int player2Wins = 0;
    private int draws = 0;
    private Window owner;
    private VideoDialogController videoDialogController;
    private String myPlayerName = "Player 1";
    private String opponentPlayerName = "Player 2";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateScoreDisplay();
        Platform.runLater(() -> {
            owner = settingsButton.getScene().getWindow();
        });
    }

    public void setGameStrategy(GameStartegy gameStrategy) {
        this.gameStrategy = gameStrategy;
        setupConnection();
        startGame();

    }
    

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setRecording(boolean record) {
        if (record) {
            matchRecorder = new MatchRecorder(player2Name.getText());
        }
    }

    public void setMyChar(char move) {
        myChar = move;
        oponentsChar = (myChar == 'X') ? 'O' : 'X';
    }

    private void setupConnection() {
        if (this.gameStrategy instanceof OnlineGame) {
            ConnectionManager.getConnection().getClientMessageListener().setCallback(this);
            Session session = SessionDAO.getInstance().getSession();
            if (session != null) {
                System.out.println("Session already exists, loading names...");
                updatePlayerNamesFromSession(session);
            }
        }
    }

    @Override
    public void onSessionDataReceived(Session session) {
        System.out.println("GameController received session: " + session);
        Platform.runLater(() -> {
            updatePlayerNamesFromSession(session);
        });
    }

    private void updatePlayerNamesFromSession(Session session) {
        if (session == null) {
            System.out.println("Session is null!");
            return;
        }

        int currentUserId = UserSession.getInstance().getCurrentUser().getId();
        System.out.println("Current user ID: " + currentUserId);
        System.out.println("Player1 ID: " + session.getPlayer1Id() + " Name: " + session.getPlayer1Name());
        System.out.println("Player2 ID: " + session.getPlayer2Id() + " Name: " + session.getPlayer2Name());

        Platform.runLater(() -> {
            player1Name.setText(session.getPlayer1Name());
            player2Name.setText(session.getPlayer2Name());
            player1Score.setText("" + session.getPlayer1Score());
            player2Score.setText("" + session.getPlayer2Score());
        });
    }

    @Override
    public void startGame() {
        Board.getInstance().resetBoard();
        gameEnded = false;
        clearBoard();

        if (myTurn) {
            enableBoard();
        } else {
            disableBoard();
        }

        updateTurnIndicator();
    }

    @FXML
    private void handleCellClick(ActionEvent event) {
        if (gameEnded) {
            return;
        }

        Button clicked = (Button) event.getSource();

        if (!(clicked.getText().isEmpty())) {
            return;
        }

        Integer row = GridPane.getRowIndex(clicked);
        Integer col = GridPane.getColumnIndex(clicked);
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;

        if (gameStrategy instanceof AiGame) {
            gameStrategy.play(row, col, myChar);
            updateUI();

            if (checkGameStateWithoutDialog()) {
                return;
            }

            disableBoard();

            PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
            pause.setOnFinished(e -> {
                ((AiGame) gameStrategy).playAi(myChar);
                updateUI();
                enableBoard();
                checkGameState();
                updateTurnIndicator();
            });

            pause.play();
        } else if (gameStrategy instanceof LocalGame) {
            if (myTurn) {
                gameStrategy.play(row, col, myChar);
                myTurn = false;
            } else {
                gameStrategy.play(row, col, oponentsChar);
                myTurn = true;
            }
            updateUI();
            updateTurnIndicator();
            checkGameState();
        }else if (gameStrategy instanceof OnlineGame) {
            if (myTurn) {
                gameStrategy.play(row, col, myChar);
                updateUI();

                recordMove(new Move(row, col, myChar));

                updateTurnIndicator();

                checkGameState();
            if (!gameEnded) {
                    myTurn = false;
                    disableBoard();
                }
            }
        }
    }

    private void updateTurnIndicator() {
        Platform.runLater(() -> {
            if (gameEnded) {
                player1Name.setTextFill(Color.BLACK);
                player2Name.setTextFill(Color.BLACK);
                return;
            }

            if (gameStrategy instanceof AiGame) {
                player1Name.setTextFill(Color.BLUE);
                player2Name.setTextFill(Color.BLACK);
                return;
            }

            if (gameStrategy instanceof OnlineGame) {
                char currentTurnChar = getCurrentTurnChar();
                int currentUserId = UserSession.getInstance().getCurrentUser().getId();

                Session session = SessionDAO.getInstance().getSession();
                if (session == null) {
                    if (myTurn) {
                        player1Name.setTextFill(Color.BLUE);
                        player2Name.setTextFill(Color.GRAY);
                    } else {
                        player1Name.setTextFill(Color.GRAY);
                        player2Name.setTextFill(Color.BLUE);
                    }
                    return;
                }
                boolean isPlayer1 = (currentUserId == session.getPlayer1Id());
                boolean isCurrentUserTurn;

                if (isPlayer1) {
                    isCurrentUserTurn = (currentTurnChar == myChar);
                } else {
                    isCurrentUserTurn = (currentTurnChar == myChar);
                }

                if (isCurrentUserTurn) {
                    if (isPlayer1) {
                        player1Name.setTextFill(Color.BLUE);
                        player2Name.setTextFill(Color.GRAY);
                    } else {
                        player1Name.setTextFill(Color.GRAY);
                        player2Name.setTextFill(Color.BLUE);
                    }
                } else {
                    if (isPlayer1) {
                        player1Name.setTextFill(Color.GRAY);
                        player2Name.setTextFill(Color.BLUE);
                    } else {
                        player1Name.setTextFill(Color.BLUE);
                        player2Name.setTextFill(Color.GRAY);
                    }
                }
            } else if (gameStrategy instanceof LocalGame) {
                if (myTurn) {
                    player1Name.setTextFill(Color.BLUE);
                    player2Name.setTextFill(Color.GRAY);
                } else {
                    player1Name.setTextFill(Color.GRAY);
                    player2Name.setTextFill(Color.BLUE);
                }
            }
        });
    }

    private void updateUI() {
        Platform.runLater(() -> {
            for (Node node : gameBoard.getChildren()) {
                if (node instanceof Button) {
                    Integer r = GridPane.getRowIndex(node);
                    Integer c = GridPane.getColumnIndex(node);
                    r = (r == null) ? 0 : r;
                    c = (c == null) ? 0 : c;
                    if (((Button) node).getText().isEmpty()
                            && Board.getInstance().getBoard()[r][c] != '\0') {

                        ((Button) node).setText(
                                String.valueOf(Board.getInstance().getBoard()[r][c])
                        );
                    }
                }
            }
        });
    }

    private boolean checkGameStateWithoutDialog() {
        int[][] winningCells = this.gameStrategy.winner();
        if (winningCells != null) {
            handleWin(winningCells);
            return true;
        }

        char[][] board = Board.getInstance().getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        handleDraw();
        return true;
    }

    private void checkGameState() {
        int[][] winningCells = this.gameStrategy.winner();
        if (winningCells == null) {
            checkForDraw();
        } else {
            handleWin(winningCells);
        }
    }

    private void checkForDraw() {
        char[][] board = Board.getInstance().getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return;
                }
            }
        }
        handleDraw();
    }

    private void handleWin(int[][] winningCells) {

        gameEnded = true;

        if (matchRecorder != null) {
            matchRecorder.endMatch(Result.WIN);
        }

        disableBoard();
        paintWinningCells(winningCells);

        char winningChar = Board.getInstance()
                .getBoard()[winningCells[0][0]][winningCells[0][1]];

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        pause.setOnFinished(e -> {

            if (winningChar == myChar) {
                player1Wins++;
                player1Name.setTextFill(Color.GREEN);
                player2Name.setTextFill(Color.RED);

                videoDialogController = GameDialogs.openVideoDialog(
                        gameBoard,
                        "You Won!",
                        "/videos/win.mp4",
                        () -> SceneNavigator.back(gameBoard),
                        this::restart
                );

            } else {
                player2Wins++;
                player1Name.setTextFill(Color.RED);
                player2Name.setTextFill(Color.GREEN);

                videoDialogController = GameDialogs.openVideoDialog(
                        gameBoard,
                        "You Lost!",
                        "/videos/Lose.mp4",
                        () -> SceneNavigator.back(gameBoard),
                        this::restart
                );
            }
            if (this.gameStrategy instanceof OnlineGame) {
                videoDialogController.disablePlayAgain(true);

            }
            updateScoreDisplay();
        });

        pause.play();
    }

    private void handleDraw() {
        gameEnded = true;
        draws++;
        disableBoard();
        if (matchRecorder != null) {
            matchRecorder.endMatch(Result.DRAW);
        }
        player1Name.setTextFill(Color.ORANGE);
        player2Name.setTextFill(Color.ORANGE);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        pause.setOnFinished(e -> {
            updateScoreDisplay();

            videoDialogController = GameDialogs.openVideoDialog(
                    gameBoard,
                    "Draw",
                    "/videos/Draw.mp4",
                    () -> SceneNavigator.back(gameBoard),
                    () -> {
                        restart();
                    }
            );
            if (this.gameStrategy instanceof OnlineGame) {
                videoDialogController.disablePlayAgain(true);
            }
        });

        pause.play();
    }

    private void updateScoreDisplay() {
        Platform.runLater(() -> {
            player1Score.setText(String.valueOf(player1Wins));
            drawScore.setText(String.valueOf(draws));
            if (player2Score != null) {
                player2Score.setText(String.valueOf(player2Wins));
            }
        });
    }

    private void paintWinningCells(int[][] winningCells) {
        char winningChar = Board.getInstance().getBoard()[winningCells[0][0]][winningCells[0][1]];

        for (int[] cell : winningCells) {
            int row = cell[0];
            int col = cell[1];

            for (Node node : gameBoard.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                Integer c2 = GridPane.getColumnIndex(node);
                r = (r == null) ? 0 : r;
                c = (c == null) ? 0 : c;

                if (node instanceof Button && r == row && c == col) {
                    Button btn = ((Button) node);
                    if (winningChar == myChar) {
                        btn.setTextFill(Color.GREEN);
                    } else {
                        btn.setTextFill(Color.RED);
                    }
                }
            }
        }
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

    private void clearBoard() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setText("");
                btn.setTextFill(Color.BLACK);
            }
        }
        Board.getInstance().resetBoard();
    }

    @FXML
    private void handleSettingClick(ActionEvent event) {
        Parent root = ((Node) event.getSource()).getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/GameDialog.fxml");
    }

    @FXML
    private void handlePlayAgain(ActionEvent event) {
        startGame();
    }

    @Override
    public void onNotYourTurn() {
        Platform.runLater(() -> {
            AlertUtil.showAlert("Not Your Turn", "Please wait for your turn!", Alert.AlertType.ERROR, owner);
        });
    }

    @Override
    public void onCellOccupied() {
        Platform.runLater(() -> {
            AlertUtil.showAlert("Cell Occupied", "This cell is already taken!!", Alert.AlertType.ERROR, owner);
        });
    }

    @Override
    public void endGame() {

    }

    @Override
    public void onMoveReceived(Move move) {
        Platform.runLater(() -> {
            Board.getInstance().setCell(move.getX(), move.getY(), move.getMove());
            updateUI();
            int[][] winningCells = this.gameStrategy.winner();
            boolean isDraw = false;
            
            if (winningCells == null) {
                char[][] board = Board.getInstance().getBoard();
                isDraw = true;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == '\0') {
                            isDraw = false;
                            break;
                        }
                    }
                    if (!isDraw) break;
                }
            }
           
            if (winningCells == null && !isDraw) {
                myTurn = true;
                updateTurnIndicator();
                enableBoard();
            } else {
                checkGameState();
            }
        });
        recordMove(move);
    }

    private void recordMove(Move move) {
        if (matchRecorder != null) {
            matchRecorder.addMove(move);
        }
    }

    private void restart() {
        GameStartegy newStrategy;

        if (gameStrategy instanceof LocalGame) {
            newStrategy = new LocalGame();
        } else if (gameStrategy instanceof AiGame) {
            newStrategy = new AiGame(Difficulty.HARD);
        } else if (gameStrategy instanceof OnlineGame) {
            newStrategy = new OnlineGame();
        } else {
            newStrategy = new LocalGame();
        }

        SceneNavigator.navigateWithoutHistory(gameBoard, "/fxml/game.fxml", (GameController c) -> {
            c.setGameStrategy(newStrategy);
            c.setMyChar(this.myChar);
            c.setMyTurn(myTurn);
            c.setRecording(matchRecorder != null);
        });
    }
    private char getCurrentTurnChar() {
        char[][] board = Board.getInstance().getBoard();
        int xCount = 0;
        int oCount = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X') xCount++;
                if (board[i][j] == 'O') oCount++;
            }
        }
        return xCount == oCount ? 'X' : 'O';
    }
}