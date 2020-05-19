package com.webcheckers.model;

public class Spectator extends Player{

    public boolean spectating;
    public Spectator(String name){
        super(name);
        spectating = false;
    }


}
