package com.mycompany.clientside.callbacks;

import com.mycompany.clientside.dto.Session;

public interface SessionCallback {
    void onSessionDataReceived(Session session);
}