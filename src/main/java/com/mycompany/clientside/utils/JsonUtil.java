package com.mycompany.clientside.utils;

import com.google.gson.Gson;
import com.mycompany.clientside.dto.Player;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {

    private static final Gson gson = new Gson();

    public static List<Player> fromJsonList(String json) {
        Player[] array = gson.fromJson(json, Player[].class);
        return Arrays.asList(array);
    }
}
