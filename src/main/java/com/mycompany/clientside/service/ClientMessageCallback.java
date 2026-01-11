package com.mycompany.clientside.service;

import com.mycompany.clientside.dto.Player;
import java.util.List;

public interface ClientMessageCallback {

    void loadPlayers(List<Player> players);

    void startGame();

    void onInviteReceived(String[] parts);

    void onInviteRejected(String[] parts);

    void onMoveReceived(int row, int col, char player);

    void onWin(char winner);

    void onLose(char loser);

    void onDraw();

    void onNotYourTurn();

    void onCellOccupied();
    
    void endGame();
    
    void loadLeaderBoard(List<Player> players);

}
