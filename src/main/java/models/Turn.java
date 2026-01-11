/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author 20000
 */
public enum Turn {
    X("X"),
    O("O");
    
    private final String symbol;
    
    Turn(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
}
