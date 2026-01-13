/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clientside.callbacks;

import com.mycompany.clientside.matchRecording.Move;

public interface GameActionCallback {
    void onMoveReceived(Move move);
    void onNotYourTurn();
    void onCellOccupied();
}
