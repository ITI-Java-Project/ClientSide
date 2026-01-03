package com.mycompany.clientside.utils;

import java.util.Stack;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneNavigator {

    private static final Stack<String> pages = new Stack<>();
    private static final Duration FADE_DURATION = Duration.millis(500);
    private static boolean isNavigating = false;

    public static void navigateTo(Node currentNode, String fxmlPath) {
        navigateToInternal(currentNode, fxmlPath, true);
    }

    public static void back(Node currentNode) {
        if (!pages.isEmpty()) {
            pages.pop();
        }
        String target = pages.isEmpty() ? "/fxml/Landing.fxml" : pages.peek();
        navigateToInternal(currentNode, target, false);
    }

    private static void navigateToInternal(Node currentNode, String fxmlPath, boolean pushToHistory) {
        if (isNavigating) {
            return;
        }
        isNavigating = true;

        try {
            Stage stage = (Stage) currentNode.getScene().getWindow();
            Scene scene = stage.getScene();

            Parent newRoot = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));
            newRoot.setOpacity(0);

            scene.setRoot(newRoot);

            FadeTransition fadeIn = new FadeTransition(FADE_DURATION, newRoot);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setOnFinished(e -> isNavigating = false);
            fadeIn.play();

            if (pushToHistory) {
                pages.push(fxmlPath);
            }

        } catch (Exception e) {
            isNavigating = false;
            e.printStackTrace();
        }
    }
}
