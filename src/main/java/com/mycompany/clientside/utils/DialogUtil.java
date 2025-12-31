package com.mycompany.clientside.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;

public class DialogUtil {

    private DialogUtil() {}

    public static void openSettingsDialog(Parent root) {
        StackPane overlayRoot = ensureOverlayRoot(root);
        showSettingsDialog(overlayRoot);
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

    private static void showSettingsDialog(StackPane overlayRoot) {
        try {
            if (!overlayRoot.getChildren().isEmpty()) {
                overlayRoot.getChildren().get(0).setEffect(new GaussianBlur(20));
            }

            FXMLLoader loader = new FXMLLoader(DialogUtil.class.getResource("/fxml/CustomDialog.fxml"));
            Parent dialog = loader.load();

            com.mycompany.clientside.controllers.CustomDialogController controller =
                    loader.getController();
            controller.setRootPane(overlayRoot);

            overlayRoot.getChildren().add(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
