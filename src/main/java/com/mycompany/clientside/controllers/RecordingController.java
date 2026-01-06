package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.MatchResultGame;

public class RecordingController implements Initializable {

    @FXML
    private VBox recordsContainer;
    @FXML
    private ImageView backBtn;
    @FXML
    private Label leaderBoardText;
    @FXML
    private ImageView settingsIcon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRecord("Ahmed Salah", "2025-01-01", "08:30 PM", MatchResultGame.WIN);
        loadRecord("Omar Hassan", "2025-01-02", "10:15 PM", MatchResultGame.LOSE);
        loadRecord("Mohamed Ali", "2025-01-03", "09:00 PM", MatchResultGame.DRAW);
    }

    private void loadRecord(String playerName, String date, String time, MatchResultGame result) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/RecordingCard.fxml")
            );

            HBox card = loader.load();
            RecordingCardController controller = loader.getController();

            controller.setData(playerName, date, time, result);

            recordsContainer.getChildren().add(card);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SceneNavigator.back(backBtn);
    }

    @FXML
    private void goToLeaderBoard(MouseEvent event) {
               SceneNavigator.navigateTo(leaderBoardText, "/fxml/LeaderBoard.fxml");

    }

    @FXML
    private void goToSettings(MouseEvent event) {
        StackPane root = (StackPane) settingsIcon.getScene().getRoot();
DialogUtil.openDialog(root, "/fxml/CustomDialog.fxml");
    }
}
