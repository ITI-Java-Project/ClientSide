/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.game;

import java.util.Random;
import models.Difficulty;

/**
 *
 * @author 20000
 */
public class AiGame implements GameStartegy {

    private final MiniMax miniMax = new MiniMax();
    private final Difficulty difficulty;

    public AiGame(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void play(int x, int y, char move) {
        Board board = Board.getInstance();
        board.setCell(x, y, move);

        int[] aiMove;

        switch (difficulty) {
            case EASY:
                aiMove = miniMax.randomMove(board);
                break;

            case MEDIUM:
                if (new Random().nextBoolean()) {
                    aiMove = miniMax.randomMove(board);
                } else {
                    aiMove = miniMax.decideMove(board);
                }
                break;

            case HARD:
                aiMove = miniMax.decideMove(board);
                break;

            default:
                throw new IllegalStateException("Unexpected difficulty: " + difficulty);
        }

        if (aiMove != null) {
            char ai = (move == 'X') ? 'O' : 'X';
            board.setCell(aiMove[0], aiMove[1],ai );
        }
    }
}
