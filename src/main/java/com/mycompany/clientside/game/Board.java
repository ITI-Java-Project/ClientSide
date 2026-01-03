
package com.mycompany.clientside.game;

import javafx.scene.effect.Light.Point;

public class Board {
    
    private static Board board; 
    private char[][] gameBoard; 
    
    private Board(){
        gameBoard= new char[3][3];
    }
    
    public static Board getInstance(){
       if(board==null){
           board=new Board();
       }
       return board;
    }
    
    public char[][] getBoard(){
        return gameBoard;
    }
    
    public void setCell(int x, int y, char move){
        gameBoard[x][y]=move;
    }
    
    public void resetBoard(){
        gameBoard= new char[3][3];
    }
}
