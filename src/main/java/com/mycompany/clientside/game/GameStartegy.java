
package com.mycompany.clientside.game;

public interface GameStartegy {
    
    default int[][] winner() {
        
        char[][] board = Board.getInstance().getBoard();
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' &&
                board[i][0] == board[i][1] &&
                board[i][1] == board[i][2]) {
                return new int[][] { {i,0}, {i,1}, {i,2} };
            }
            if (board[0][i] != '\0' &&
                board[0][i] == board[1][i] &&
                board[1][i] == board[2][i]) {
                return new int[][] { {0,i}, {1,i}, {2,i} };
            }
        }
        
        if (board[0][0] != '\0' &&
            board[0][0] == board[1][1] &&
            board[1][1] == board[2][2]) {
            return new int[][] { {0,0}, {1,1}, {2,2} };
        }
        if (board[0][2] != '\0' &&
            board[0][2] == board[1][1] &&
            board[1][1] == board[2][0]) {
            return new int[][] { {0,2}, {1,1}, {2,0} };
        }
        return null;
    }


   
    
    
    public void play(int x, int y, char move);
    
}
