/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clientside.service;

import com.mycompany.clientside.dto.Player;
import java.util.List;

public interface ClientMessageCallback {

    void loadPlayers(List<Player> players);
    void startGame();
    void onInviteReceived(String[] parts);

}
