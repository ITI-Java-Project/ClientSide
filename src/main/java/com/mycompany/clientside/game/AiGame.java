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

    // Human move ONLY
    @Override
    public void play(int x, int y, char move) {
        Board.getInstance().setCell(x, y, move);
    }

    // AI move ONLY
    public void playAi(char humanChar) {
        Board board = Board.getInstance();
        char aiChar = (humanChar == 'X') ? 'O' : 'X';

        int[] aiMove;

        switch (difficulty) {
            case EASY:
                aiMove = miniMax.randomMove(board);
                break;

            case MEDIUM:
                aiMove = new Random().nextBoolean()
                        ? miniMax.randomMove(board)
                        : miniMax.decideMove(board);
                break;

            case HARD:
                aiMove = miniMax.decideMove(board);
                break;

            default:
                throw new IllegalStateException();
        }

        if (aiMove != null) {
            board.setCell(aiMove[0], aiMove[1], aiChar);
        }
    }
}