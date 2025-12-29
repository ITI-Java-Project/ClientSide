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
        StackPane stack;
        try {
            Stage stage = (Stage) currentNode.getScene().getWindow();
            Scene scene = stage.getScene();

            Parent oldRoot = scene.getRoot();
            Parent newRoot = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));

            if (stage.isFullScreen()) stage.setFullScreenExitHint("");

            stack = createStack(oldRoot);
            scene.setRoot(stack);

            newRoot.setOpacity(0);
            stack.getChildren().add(newRoot);

            fadeOut(oldRoot);
            fadeIn(newRoot);
            fadeIn(newRoot).setOnFinished(e -> stack.getChildren().remove(oldRoot));
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    private static FadeTransition fadeIn(Parent node){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);  
        fadeIn.play();
        return fadeIn; 
    }
   
    private static void fadeOut(Parent node){
       FadeTransition fadeOut = new FadeTransition(Duration.millis(150), node);
       fadeOut.setFromValue(1);
       fadeOut.setToValue(0);
       fadeOut.play();
    } 
    
    private static StackPane createStack(Parent node){
        return (node instanceof StackPane)?(StackPane) node:new StackPane(node);
    }
}