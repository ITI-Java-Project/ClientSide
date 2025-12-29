package com.mycompany.clientside.utils;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author hends
 */

public class SceneNavigator {
    
   public static void navigateTo(Node currentNode, String fxmlPath) {
        try {
            Stage stage = (Stage) currentNode.getScene().getWindow();
            Scene scene = stage.getScene();

            Parent oldRoot = scene.getRoot();
            Parent newRoot = FXMLLoader.load(
                    SceneNavigator.class.getResource(fxmlPath)
            );

            StackPane stack;
            
            if (stage.isFullScreen()) {
                 stage.setFullScreenExitHint("");
            }

            if (oldRoot instanceof StackPane) {
                stack = (StackPane) oldRoot;
            } else {
                stack = new StackPane(oldRoot);
                scene.setRoot(stack);
            }

            newRoot.setOpacity(0);
            stack.getChildren().add(newRoot);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(100), newRoot);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), oldRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeIn.play();
            fadeOut.play();

            fadeIn.setOnFinished(e -> {
                stack.getChildren().remove(oldRoot);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}