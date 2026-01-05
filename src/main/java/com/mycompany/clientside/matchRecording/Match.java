/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.matchRecording;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author sian
 */
public class Match {
    
    private String oponentName; 
    private Queue<Move>moves;
    private Result result;
    private Date date; 
    
    public Match(String oponentName ){
       date= new Date();
       this.oponentName=oponentName;
       this.moves= new LinkedList<>();
    } 
    
    public void addMove( Move move){
        this.moves.add(move);
    }
    
    public void finish(Result result){
        this.result=result;
        saveToFile();
    }
    private void saveToFile(){
        
    }

    public String getOponentName() {
        return oponentName;
    }

    public void setOponentName(String oponentName) {
        this.oponentName = oponentName;
    }

    public Queue<Move> getMoves() {
        return moves;
    }

    public void setMoves(Queue<Move> moves) {
        this.moves = moves;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    
}
