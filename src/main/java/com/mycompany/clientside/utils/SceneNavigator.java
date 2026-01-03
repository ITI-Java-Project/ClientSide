package com.mycompany.clientside.utils;

import java.util.Stack;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneNavigator {

    private static final Stack<String> pages = new Stack<>();
    private static final Duration FADE_DURATION = Duration.millis(1000);

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
        try {
            
            Scene scene = currentNode.getScene();
            
            Parent oldRoot = scene.getRoot();
            Parent newRoot = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));

            StackPane transitionRoot = new StackPane(oldRoot, newRoot);
            newRoot.setOpacity(0);

            scene.setRoot(transitionRoot);

            fadeOut(oldRoot);
            FadeTransition fadeInTransition = fadeIn(newRoot);

            fadeInTransition.setOnFinished(e -> {
                transitionRoot.getChildren().remove(oldRoot);
                scene.setRoot(transitionRoot);
            });

            if (pushToHistory) {
                pages.push(fxmlPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static FadeTransition fadeIn(Parent node) {
        FadeTransition ft = new FadeTransition(FADE_DURATION, node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        return ft;
    }

    private static void fadeOut(Parent node) {
        FadeTransition ft = new FadeTransition(FADE_DURATION, node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }

    private static StackPane createStack(Parent node) {
        return (node instanceof StackPane) ? (StackPane) node : new StackPane(node);
    }
}