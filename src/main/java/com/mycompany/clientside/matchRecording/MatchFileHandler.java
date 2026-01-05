
package com.mycompany.clientside.matchRecording;

import com.google.gson.Gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchFileHandler {

    private static final String MATCHES_DIR = "matches";

    public void saveMatch(Match match) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        File directory = new File(MATCHES_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName = "match_" +match.getDate()+"Res_"+match.getResult() + ".json";
        File file = new File(directory, fileName);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(match, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Match> loadAllMatches() {
        List<Match> matches = new ArrayList<>();
        Gson gson = new Gson();

        File directory = new File(MATCHES_DIR);
        if (!directory.exists() || !directory.isDirectory()) {
            return matches;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return matches;

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                Match match = gson.fromJson(reader, Match.class);
                if (match != null) {
                    matches.add(match);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return matches;
    }
}
