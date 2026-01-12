package com.mycompany.clientside.matchRecording;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MatchFileHandler {

    private static final String MATCHES_DIR = "matches";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public void saveMatch(Match match) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        File directory = new File(MATCHES_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String formattedDate = DATE_FORMAT.format(match.getDate());
        String fileName = "match_" + formattedDate + "_" + match.getResult() + ".json";
        File file = new File(directory, fileName);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(match, writer);
            System.out.println("Match saved successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving match: " + e.getMessage());
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
        if (files == null) {
            return matches;
        }

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                Match match = gson.fromJson(reader, Match.class);
                if (match != null) {
                    matches.add(match);
                }
            } catch (IOException e) {
                System.err.println("Error loading match from " + file.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return matches;
    }
}
