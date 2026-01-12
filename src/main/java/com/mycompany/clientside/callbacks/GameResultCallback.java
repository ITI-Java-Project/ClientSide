/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clientside.callbacks;

public interface GameResultCallback {
    void onWin(char winner);
    void onLose(char loser);
    void onDraw();
}