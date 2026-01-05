package com.mycompany.clientside.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Window;

public class AlertUtil {

    public static void showAlert(String title, String message, Alert.AlertType type, Window owner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            if (owner != null) {
                alert.initOwner(owner);
                alert.initModality(Modality.WINDOW_MODAL);
            }
            
            alert.showAndWait();
        });
    }
}