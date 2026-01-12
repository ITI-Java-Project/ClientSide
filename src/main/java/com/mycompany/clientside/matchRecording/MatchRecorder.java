/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.matchRecording;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author sian
 */
public class MatchRecorder {
   
    private Match match;
    private MatchFileHandler matchFileHandler;

    public MatchRecorder(String  oponentName) {
        match = new Match(oponentName);
        matchFileHandler = new MatchFileHandler();
    }
    
    public void addMove(Move move){
        match.addMove(move);
    }
    
    public void endMatch(Result result){
        match.setResult(result);
        matchFileHandler.saveMatch(match);
    }
    
    public List<Match> loadAllMatches(){
        return matchFileHandler.loadAllMatches();
    }
          
    
}
