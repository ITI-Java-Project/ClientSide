package com.mycompany.clientside.utils;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * A utility class for navigating between JavaFX scenes with optional fade
 * transitions. Supports passing data to controllers.
 */
public class SceneNavigator {

    private static final Stack<String> pages = new Stack<>();
    private static final Duration FADE_DURATION = Duration.millis(500);

    // ------------------- Public API -------------------
    public static void navigateTo(Node currentNode, String fxmlPath) {
        navigateTo(currentNode, fxmlPath, null);
    }
    public static <T> void navigateWithoutHistory(Node currentNode, String fxmlPath, Consumer<T> controllerInitializer) {
        navigateInternal(currentNode, fxmlPath, false, controllerInitializer);
     }
    public static <T> void navigateTo(Node currentNode, String fxmlPath, Consumer<T> controllerInitializer) {
        navigateInternal(currentNode, fxmlPath, true, controllerInitializer);
    }

    public static void back(Node currentNode) {
        if (!pages.isEmpty()) {
            pages.pop();
        }
        String target = pages.isEmpty() ? "/fxml/Landing.fxml" : pages.peek();
        navigateInternal(currentNode, target, false, null);
    }

    private static <T> void navigateInternal(Node currentNode,
            String fxmlPath,
            boolean pushToHistory,
            Consumer<T> controllerInitializer) {
        Scene scene = currentNode.getScene();
        Parent oldRoot = scene.getRoot();

        Parent newRoot = loadFXML(fxmlPath, controllerInitializer);

        if (newRoot == null) {
            return;
        }
        StackPane transitionRoot = createTransitionRoot(oldRoot, newRoot);
        scene.setRoot(transitionRoot);

        playFadeTransition(oldRoot, newRoot, transitionRoot);

        if (pushToHistory) {
            pages.push(fxmlPath);
        }
    }

    private static <T> Parent loadFXML(String fxmlPath, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();

            if (controllerInitializer != null) {
                T controller = (T) loader.getController();
                controllerInitializer.accept(controller);
            }

            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static StackPane createTransitionRoot(Parent oldRoot, Parent newRoot) {
        newRoot.setOpacity(0);
        return new StackPane(oldRoot, newRoot);
    }

    private static void playFadeTransition(Parent oldRoot, Parent newRoot, StackPane transitionRoot) {
        fadeOut(oldRoot);
        FadeTransition fadeIn = fadeIn(newRoot);

        fadeIn.setOnFinished(e -> {
            transitionRoot.getChildren().remove(oldRoot);
            transitionRoot.getChildren().setAll(newRoot);
        });
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
}
