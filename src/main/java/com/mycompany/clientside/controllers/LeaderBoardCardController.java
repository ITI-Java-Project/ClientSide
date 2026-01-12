package com.mycompany.clientside.controllers;

import com.mycompany.clientside.dto.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class LeaderBoardCardController {

    @FXML private HBox root;
    @FXML private Label playerName;
    @FXML private Label score;
    @FXML private Label title;
    @FXML private ImageView crown;

    public void setData(Player player, int rank) {

        playerName.setText(player.getName());
        score.setText("Score: " + player.getScore());
        title.setText(getTitle(rank));

        crown.setVisible(rank == 1);

        root.setStyle(getRankStyle(rank));
    }

    // ===== Java 11 safe =====
    private String getTitle(int rank) {
        if (rank == 1) return "Tic-Tac Legend";
        if (rank == 2) return "Grid Master";
        if (rank == 3) return "Smart Player";
        if (rank == 4) return "Rookie";
        if (rank == 5) return "Challenger";
        return "Newbie";
    }

    private String getRankStyle(int rank) {
        String color;

        switch (rank)  {
            case 1:
                color = "#FFD84D";
                break;
            case 2:
                color = "#CFCFCF";
                break;
            case 3:
                color = "#D6862F";
                break;
            default:
                color = "#E0E0E0";
                break;
        }

        return "-fx-background-color: " + color + ";" +
               "-fx-background-radius: 30;" +
               "-fx-border-color: black;" +
               "-fx-border-radius: 30;" +
               "-fx-border-width: 2;";
    }
}
