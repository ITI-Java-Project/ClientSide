package com.mycompany.clientside.utils;

import com.mycompany.clientside.controllers.InvitationDialogController;
import com.mycompany.clientside.controllers.VideoDialogController;
import javafx.scene.Parent;

public class GameDialogs {

    private GameDialogs() {}


    public static InvitationDialogController openInvitationDialog(Parent root, String playerName) {
        Object result = DialogUtil.openDialog(root, "/fxml/InvitationDialog.fxml");
        
        if (result != null) {
            InvitationDialogController controller = (InvitationDialogController) result;
            controller.setPlayerName(playerName);
            return controller;
        }
        
        return null;
    }


    public static VideoDialogController openVideoDialog(
            Parent root, 
            boolean won, 
            String videoPath,
            Runnable onGoBack, 
            Runnable onPlayAgain) {
        
        Object result = DialogUtil.openDialog(root, "/fxml/VideoDialog.fxml");
        
        if (result != null) {
            VideoDialogController controller = (VideoDialogController) result;
            controller.setWon(won);
            controller.setOnGoBack(onGoBack);
            controller.setOnPlayAgain(onPlayAgain);
            controller.playVideo(videoPath);
            return controller;
        }
        
        return null;
    }

    public static VideoDialogController openWinDialog(
            Parent root, 
            String videoPath,
            Runnable onGoBack, 
            Runnable onPlayAgain) {
        
        return openVideoDialog(root, true, videoPath, onGoBack, onPlayAgain);
    }

    public static VideoDialogController openLoseDialog(
            Parent root, 
            String videoPath,
            Runnable onGoBack, 
            Runnable onPlayAgain) {
        
        return openVideoDialog(root, false, videoPath, onGoBack, onPlayAgain);
    }
}