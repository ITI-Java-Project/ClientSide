/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.game;


public class LocalGame implements GameStartegy{

    @Override
    public void play(int x, int y, char move) {
        Board.getInstance().setCell(x, y, move);
    }

   
}
