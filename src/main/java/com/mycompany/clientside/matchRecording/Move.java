/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.matchRecording;


public class Move {
    int x; 
    int y; 
    char move; 

    public Move(int x, int y, char move) {
        this.x = x;
        this.y = y;
        this.move = move;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getMove() {
        return move;
    }

    public void setMove(char move) {
        this.move = move;
    }

    
}
