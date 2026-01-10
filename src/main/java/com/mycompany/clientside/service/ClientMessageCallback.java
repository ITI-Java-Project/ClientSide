package com.mycompany.clientside.service;

import com.mycompany.clientside.dto.Player;
import java.util.List;

public interface ClientMessageCallback {
    void loadPlayers(List<Player> players);
    void startGame();
    void onInviteReceived(String[] parts);
    void onInviteRejected(String[] parts);
}
