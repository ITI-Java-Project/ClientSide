package com.mycompany.clientside.controllers;

import com.mycompany.clientside.matchRecording.Match;
import com.mycompany.clientside.matchRecording.Move;
import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class RecordedMatchController implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button settingsButton;
    @FXML
    private Button backButton;
    @FXML
    private Label matchInfoLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private Button cell00, cell01, cell02;
    @FXML
    private Button cell10, cell11, cell12;
    @FXML
    private Button cell20, cell21, cell22;
    
    // Playback controls
    @FXML
    private Button playPauseButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button restartButton;
    @FXML
    private Slider moveSlider;
    @FXML
    private Label moveCountLabel;

    private Button[][] cells;
    private Match match;
    private List<Move> movesList;
    private int currentMoveIndex = 0;
    private Timeline autoPlayTimeline;
    private boolean isPlaying = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCellsArray();
        disableAllCells();
        setupPlaybackControls();
    }

    private void initializeCellsArray() {
        cells = new Button[][]{
            {cell00, cell01, cell02},
            {cell10, cell11, cell12},
            {cell20, cell21, cell22}
        };
    }

    private void disableAllCells() {
        for (Button[] row : cells) {
            for (Button cell : row) {
                cell.setDisable(true);
                cell.setStyle(cell.getStyle() + "-fx-opacity: 1.0;");
            }
        }
    }

    private void setupPlaybackControls() {
        playPauseButton.setText("▶");
        
        // Slider listener to manually navigate through moves
        moveSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!isPlaying && movesList != null) {
                int targetMove = newVal.intValue();
                if (targetMove != currentMoveIndex) {
                    goToMove(targetMove);
                }
            }
        });
    }

    public void setMatch(Match match) {
        this.match = match;
        this.movesList = new ArrayList<>(match.getMoves());
        displayMatchInfo();
        
        // Setup slider
        if (movesList != null && !movesList.isEmpty()) {
            moveSlider.setMin(0);
            moveSlider.setMax(movesList.size());
            moveSlider.setValue(0);
            moveSlider.setBlockIncrement(1);
            moveSlider.setMajorTickUnit(1);
            moveSlider.setMinorTickCount(0);
            moveSlider.setSnapToTicks(true);
        }
        
        clearBoard();
        updateMoveCounter();
    }

    private void displayMatchInfo() {
        if (match != null) {
            String info = String.format("VS %s - %s", 
                match.getOponentName(), 
                dateFormat.format(match.getDate()));
            matchInfoLabel.setText(info);
            
            String result = match.getResult().toString();
            resultLabel.setText("Result: " + result);
        }
    }

    private void clearBoard() {
        for (Button[] row : cells) {
            for (Button cell : row) {
                cell.setText("");
            }
        }
        currentMoveIndex = 0;
        updateMoveCounter();
    }

    private void goToMove(int targetMoveIndex) {
        if (movesList == null || targetMoveIndex < 0 || targetMoveIndex > movesList.size()) {
            return;
        }

        // Clear board and replay up to target move
        clearBoardWithoutReset();
        
        for (int i = 0; i < targetMoveIndex && i < movesList.size(); i++) {
            Move move = movesList.get(i);
            applyMoveToBoard(move);
        }
        
        currentMoveIndex = targetMoveIndex;
        updateMoveCounter();
        
        if (!isPlaying) {
            moveSlider.setValue(currentMoveIndex);
        }
    }

    private void clearBoardWithoutReset() {
        for (Button[] row : cells) {
            for (Button cell : row) {
                cell.setText("");
            }
        }
    }

    private void applyMoveToBoard(Move move) {
        int row = move.getY();
        int col = move.getX();
        char symbol = move.getMove();

        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            Button cell = cells[row][col];
            cell.setText(String.valueOf(symbol));
        }
    }

    private void updateMoveCounter() {
        if (movesList != null) {
            moveCountLabel.setText(currentMoveIndex + " / " + movesList.size());
        }
    }

    @FXML
    private void handlePlayPause(MouseEvent event) {
        if (movesList == null || movesList.isEmpty()) {
            return;
        }

        if (isPlaying) {
            pausePlayback();
        } else {
            startPlayback();
        }
    }

    private void startPlayback() {
        if (currentMoveIndex >= movesList.size()) {
            currentMoveIndex = 0;
            clearBoard();
        }

        isPlaying = true;
        playPauseButton.setText("||");

        autoPlayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (currentMoveIndex < movesList.size()) {
                Move move = movesList.get(currentMoveIndex);
                applyMoveToBoard(move);
                currentMoveIndex++;
                updateMoveCounter();
                moveSlider.setValue(currentMoveIndex);
            } else {
                pausePlayback();
            }
        }));
        autoPlayTimeline.setCycleCount(Timeline.INDEFINITE);
        autoPlayTimeline.play();
    }

    private void pausePlayback() {
        isPlaying = false;
        playPauseButton.setText("▶");
        
        if (autoPlayTimeline != null) {
            autoPlayTimeline.stop();
        }
    }

    @FXML
    private void handlePrevious(MouseEvent event) {
        pausePlayback();
        if (currentMoveIndex > 0) {
            goToMove(currentMoveIndex - 1);
        }
    }

    @FXML
    private void handleNext(MouseEvent event) {
        pausePlayback();
        if (currentMoveIndex < movesList.size()) {
            goToMove(currentMoveIndex + 1);
        }
    }

    @FXML
    private void handleRestart(MouseEvent event) {
        pausePlayback();
        goToMove(0);
    }

    @FXML
    private void handleBack(MouseEvent event) {
        pausePlayback();
        SceneNavigator.back(rootPane);
    }

}