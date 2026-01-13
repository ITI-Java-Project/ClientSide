package com.mycompany.clientside.utils;

import com.mycompany.clientside.controllers.ChooseDifficultyDialogController;
import com.mycompany.clientside.controllers.ChooseTurnDialogController;
import com.mycompany.clientside.controllers.InvitationDialogController;
import com.mycompany.clientside.controllers.ReceiveInvitationDialogController;
import com.mycompany.clientside.controllers.RejectInvitationDialogController;
import com.mycompany.clientside.controllers.VideoDialogController;
import java.util.function.Consumer;
import javafx.scene.Parent;
import models.Difficulty;
import models.Turn;

public class GameDialogs {

    private GameDialogs() {
    }

    public static InvitationDialogController openInvitationDialog(
            Parent root,
            String playerName) {

        Object result = DialogUtil.openDialog(root, "/fxml/InvitationDialog.fxml");

        if (result != null) {
            InvitationDialogController controller =
                    (InvitationDialogController) result;
            controller.setPlayerName(playerName);
            return controller;
        }

        return null;
    }

    public static VideoDialogController openVideoDialog(
            Parent root,
            String title,
            String videoPath,
            Runnable onGoBack,
            Runnable onPlayAgain) {

        Object result = DialogUtil.openDialog(root, "/fxml/VideoDialog.fxml");

        if (result != null) {
            VideoDialogController controller =
                    (VideoDialogController) result;
            controller.setTitle(title);
            controller.setOnGoBack(onGoBack);
            controller.setOnPlayAgain(onPlayAgain);
            controller.playVideo(videoPath);
            return controller;
        }

        return null;
    }

    public static ReceiveInvitationDialogController openReceiveInvitationDialog(
            Parent root,
            String playerName,
            Runnable onAccept,
            Runnable onDeny) {

        Object result =
                DialogUtil.openDialog(root, "/fxml/ReceiveInvitationDialog.fxml");

        if (result != null) {
            ReceiveInvitationDialogController controller =
                    (ReceiveInvitationDialogController) result;
            controller.setPlayerName(playerName);
            controller.setOnAccept(onAccept);
            controller.setOnDeny(onDeny);
            return controller;
        }

        return null;
    }

    public static ChooseTurnDialogController openChooseTurnDialog(
            Parent root,
            Consumer<Turn> onTurnSelected) {

        Object result =
                DialogUtil.openDialog(root, "/fxml/ChooseTurnDialog.fxml");

        if (result != null) {
            ChooseTurnDialogController controller =
                    (ChooseTurnDialogController) result;
            controller.setOnTurnSelected(onTurnSelected);
            return controller;
        }

        return null;
    }

    public static RejectInvitationDialogController openRejectInvitationDialog(
            Parent root,
            String playerName) {

        Object result =
                DialogUtil.openDialog(root, "/fxml/RejectInvitationDialog.fxml");

        if (result != null) {
            RejectInvitationDialogController controller = (RejectInvitationDialogController) result;
            controller.setPlayerName(playerName);
            return controller;
        }

        return null;
    }
    public static ChooseDifficultyDialogController openChooseDifficultyDialog(
        Parent root,
        Consumer<Difficulty> onDifficultySelected) {

    Object result = DialogUtil.openDialog(root, "/fxml/ChooseDifficultyDialog.fxml");

    if (result != null) {
        ChooseDifficultyDialogController controller =
                (ChooseDifficultyDialogController) result;
        controller.setOnDifficultySelected(onDifficultySelected);
        return controller;
    }

    return null;
}
}
