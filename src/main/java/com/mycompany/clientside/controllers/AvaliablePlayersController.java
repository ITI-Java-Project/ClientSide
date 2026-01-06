package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.DialogUtil;
import com.mycompany.clientside.utils.SceneNavigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void challengeAction(MouseEvent event) {

    }
}
