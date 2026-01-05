/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author 20000
 */
public class MiniMax {

    private static final char AI = 'O';
    private static final char HUMAN = 'X';

    public int[] decideMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        char[][] state = board.getBoard();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == '\0') {
                    state[i][j] = AI;
                    int score = minimax(state, 0, false);
                    state[i][j] = '\0';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        Integer result = evaluate(board);
        if (result != null) {
            // Depth-based scoring
            return result > 0 ? result - depth : result + depth;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = AI;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = '\0';
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
            return bestScore;

        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = HUMAN;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = '\0';
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
            return bestScore;
        }
    }

    private Integer evaluate(char[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0'
                    && board[i][0] == board[i][1]
                    && board[i][1] == board[i][2]) {
                return board[i][0] == AI ? 10 : -10;
            }

            if (board[0][i] != '\0'
                    && board[0][i] == board[1][i]
                    && board[1][i] == board[2][i]) {
                return board[0][i] == AI ? 10 : -10;
            }
        }

        if (board[0][0] != '\0'
                && board[0][0] == board[1][1]
                && board[1][1] == board[2][2]) {
            return board[0][0] == AI ? 10 : -10;
        }

        if (board[0][2] != '\0'
                && board[0][2] == board[1][1]
                && board[1][1] == board[2][0]) {
            return board[0][2] == AI ? 10 : -10;
        }

        boolean movesLeft = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    movesLeft = true;
                }
            }
        }

        return movesLeft ? null : 0;
    }

    public int[] randomMove(Board board) {
        List<int[]> empty = new ArrayList<>();
        char[][] b = board.getBoard();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b[i][j] == '\0') {
                    empty.add(new int[]{i, j});
                }
            }
        }

        return empty.get(new Random().nextInt(empty.size()));
    }
}
