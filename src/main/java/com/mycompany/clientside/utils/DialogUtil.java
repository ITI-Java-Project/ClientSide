package com.mycompany.clientside.utils;

import static com.mycompany.clientside.utils.AlertUtil.showAlert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;

public class DialogUtil {

    private DialogUtil() {
    }

    public static Object openDialog(Parent root, String fxmlPath) {
        try {
            StackPane overlayRoot = ensureOverlayRoot(root);

            if (!overlayRoot.getChildren().isEmpty()) {
                overlayRoot.getChildren().get(0).setEffect(new GaussianBlur(20));
            }

            FXMLLoader loader = new FXMLLoader(DialogUtil.class.getResource(fxmlPath));
            Parent dialog = loader.load();

            Object controller = loader.getController();
            controller.getClass()
                    .getMethod("setRootPane", StackPane.class)
                    .invoke(controller, overlayRoot);

            overlayRoot.getChildren().add(dialog);

            return controller;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static StackPane ensureOverlayRoot(Parent root) {
        if (root instanceof StackPane) {
            return (StackPane) root;
        }

        Scene scene = root.getScene();
        StackPane wrapper = new StackPane(root);
        scene.setRoot(wrapper);
        return wrapper;
    }

    public static void showInfo(String title, String message) {
        showAlert(
                title,
                message,
                Alert.AlertType.INFORMATION,
                null
        );
    }

    public static void showError(String title, String message) {
        showAlert(
                title,
                message,
                Alert.AlertType.ERROR,
                null
        );
    }
}
