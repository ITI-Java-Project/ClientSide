package com.mycompany.clientside.controllers;

import com.mycompany.clientside.connection.ConnectionManager;
import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.game.AiGame;
import com.mycompany.clientside.game.Board;
import com.mycompany.clientside.game.GameStartegy;
import com.mycompany.clientside.game.LocalGame;
import com.mycompany.clientside.game.OnlineGame;
import com.mycompany.clientside.service.ClientMessageCallback;
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
    private boolean myTurn=true;
    private char myChar='X';
    private char oponentsChar='O';

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
    
    public void setGameStrategy(GameStartegy gameStrategy){
        this.gameStrategy=gameStrategy;
        setupConnection();
        startGame();
    }
    
    public void setMyTurn(boolean myTurn){
        this.myTurn=myTurn;
    }
    
    public void setMyChar(char move){
        myChar=move; 
        if(myChar=='X'){
            oponentsChar='O';
        }
        else{
            oponentsChar='X';
        }
    }

    private void setupConnection(){
        if(this.gameStrategy instanceof OnlineGame){
            conn = ConnectionManager.getConnection();
            conn.startListener(this);
            gameStrategy = new OnlineGame();
        }
    }
    
    @Override
    public void startGame() {
        Board.getInstance().resetBoard();
        enableBoard();
        clearBoard();
    }
    
    @FXML
    private void handleCellClick(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        System.out.println("Clicked event"+ clicked.getText());
        if (!(clicked.getText().isEmpty())) {
            return;
        }
        System.out.println("Passed empty check");

        Integer row = GridPane.getRowIndex(clicked);
        Integer col = GridPane.getColumnIndex(clicked);
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;
        if(myTurn){
            gameStrategy.play(row, col, myChar);
            System.out.println("My turn");
        }else{
            gameStrategy.play(row, col, oponentsChar);
            System.out.println("Oponent turn");
        }
        updateUI();
        checkGameState();
    } 
    
    
    private void updateUI(){
        Platform.runLater(() -> {
            for(Node node:gameBoard.getChildren()){
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
    
    private void checkGameState(){
        int[][] winningCells= this.gameStrategy.winner();
        if(winningCells==null){
            checkForDraw();
        }
        else{
           paintWinningCells(winningCells);
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
        disableBoard();
    }
    
    private void paintWinningCells(int[][] winningCells) {
        for (int[] cell : winningCells) {
            int row = cell[0];
            int col = cell[1];

            for (Node node : gameBoard.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                r = (r == null) ? 0 : r;
                c = (c == null) ? 0 : c;

                if (node instanceof Button && r == row && c == col) {
                    Button btn= ((Button) node);
                    if (btn.getText().equals("" + myChar))
                        btn.setTextFill(javafx.scene.paint.Color.GREEN);
                    else
                        btn.setTextFill(javafx.scene.paint.Color.RED);
                }
            }
        }
        disableBoard();
    }
    private void disableBoard(){
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
        
    }
    
    private void enableBoard(){
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }
    }
    
    private void clearBoard(){
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setText("");
            }
        }
        Board.getInstance().resetBoard();

    }
    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }
    @FXML
    private void handleSettingClick(ActionEvent event) {
        Parent root = ((Node) event.getSource()).getScene().getRoot();
        DialogUtil.openDialog(root, "/fxml/GameDialog.fxml");
    }

    @Override
    public void loadPlayers(List<Player> players) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public void onInviteReceived(String[] parts) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void onInviteRejected(String[] parts) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void onMoveReceived(int row, int col, char player) {
        System.out.println("CALLBACK MOVE " + row + "," + col + " " + player);

        Board.getInstance().setCell(row, col, player);
        updateUI();
    }

    @Override
    public void onWin(char winner) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void onLose(char loser) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void onDraw() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void onNotYourTurn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onCellOccupied() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void endGame() {
        Parent root = player2Name.getScene().getRoot();
        SceneNavigator.navigateTo(root, "/fxml/GameModes.fxml"); 
    }

    @Override
    public void loadLeaderBoard(List<Player> players) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }



}