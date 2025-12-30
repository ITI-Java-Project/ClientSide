/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;

/**
 *
 * @author 20000
 */
public class DialogUtil {

    private DialogUtil() {
    }

    public static void openSettingsDialog(StackPane rootPane) {
        try {
            rootPane.getChildren().forEach(node ->
                    node.setEffect(new GaussianBlur(20))
            );

            FXMLLoader loader = new FXMLLoader(
                    DialogUtil.class.getResource("/fxml/CustomDialog.fxml")
            );

            Parent dialog = loader.load();

            var controller = loader.<com.mycompany.clientside.controllers.CustomDialogController>getController();
            controller.setRootPane(rootPane);

            rootPane.getChildren().add(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
