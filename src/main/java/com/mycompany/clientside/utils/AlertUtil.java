package com.mycompany.clientside.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertUtil {
     public static void showAlert(String title , String message , Alert.AlertType type){
          Platform.runLater(() -> {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
           });   
    }   
}