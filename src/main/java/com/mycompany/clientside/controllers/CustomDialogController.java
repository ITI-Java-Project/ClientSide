package com.mycompany.clientside.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CustomDialogController implements Initializable {

    @FXML
    private Pane backgroundPane;
    
    @FXML
    private VBox dialogCard;
    
    private StackPane rootPane;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.setOnMouseClicked(e -> closeDialog());
        dialogCard.setOnMouseClicked(e -> e.consume());
    }

    @FXML
    private void openSoundSettings() {
        try {
            // Remove current dialog first
            if (rootPane != null && rootPane.getChildren().size() > 1) {
                rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
            }

            // Load sound settings dialog
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/SoundSettingsDialog.fxml")
            );
            Parent soundDialog = loader.load();

            SoundSettingsController controller = loader.getController();
            controller.setRootPane(rootPane);
            
            // Set callback to reopen settings dialog when going back
            controller.setOnBackCallback(() -> reopenSettingsDialog());

            rootPane.getChildren().add(soundDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reopenSettingsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/CustomDialog.fxml")
            );
            Parent dialog = loader.load();

            CustomDialogController controller = loader.getController();
            controller.setRootPane(rootPane);

            rootPane.getChildren().add(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    private void closeDialog() {
        rootPane.getChildren().forEach(node -> node.setEffect(null));
        rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
    }
}