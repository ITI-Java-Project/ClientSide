package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;

    @FXML
    private VBox dialogCard;

    @FXML
    private Label titleLabel;

    @FXML
    private MediaView mediaView;

    private StackPane rootPane;
    private MediaPlayer mediaPlayer;

    private Runnable onGoBack;
    private Runnable onPlayAgain;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No click handlers
    }

    public void setWon(boolean won) {
        if (won) {
            titleLabel.setText("You Won!");
            titleLabel.setStyle("-fx-text-fill: #4CAF50;");
        } else {
            titleLabel.setText("You Lost!");
            titleLabel.setStyle("-fx-text-fill: #F34B4E;");
        }
    }

    public void setOnGoBack(Runnable onGoBack) {
        this.onGoBack = onGoBack;
    }

    public void setOnPlayAgain(Runnable onPlayAgain) {
        this.onPlayAgain = onPlayAgain;
    }

    public void playVideo(String videoPath) {
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();

    }

    @FXML
    private void goBack() {
        SoundManager.getInstance().playClickSound();

        stopVideo();
        closeDialog();

        if (onGoBack != null) {
            onGoBack.run();
        }
    }

    @FXML
    private void playAgain() {
        SoundManager.getInstance().playClickSound();

        stopVideo();
        closeDialog();

        if (onPlayAgain != null) {
            onPlayAgain.run();
        }
    }

    private void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    private void closeDialog() {
        if (rootPane != null) {
            rootPane.getChildren().forEach(node -> node.setEffect(null));
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }
}
