package com.mycompany.clientside.service;

import com.mycompany.clientside.connection.ConnectionStrategy;
import com.mycompany.clientside.dto.Player;
import com.mycompany.clientside.utils.JsonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {
    
    private ConnectionStrategy conn;
    
    public PlayerService(ConnectionStrategy conn) {
        this.conn = conn;
    }
    
    public List<Player> getAllavaliablesPlayers() {
        try {
            conn.getWriter().println("GET_AVAILABLE_PLAYERS");
            
            String response = conn.getReader().readLine();
              if (response.contains(":")) {
                response = response.substring(response.indexOf(":") + 1).trim();
            }
            
            if ("NONE".equals(response)) {
                return new ArrayList<>();
            }
          
            return JsonUtil.fromJsonList(response);
        } catch (IOException ex) {
            System.getLogger(PlayerService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return new ArrayList<>();
        }
    }
}
