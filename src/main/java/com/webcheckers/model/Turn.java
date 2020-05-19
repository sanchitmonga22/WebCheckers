package com.webcheckers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold a number of move objects to allow for easier implementation
 * by the Replay class
 */
public class Turn {
    private List<Move> Moves;
    private int movecount;

    public Turn(){
        movecount = 0;
        this.Moves = new ArrayList<>();
    }

    /**
     * Add a move to the Turn
     * @param move move to add to the turn
     */
    public void addMove(Move move){
        Moves.add(move);
        movecount++;
    }

    /**
     * Remove a move from the turn. This may or may not end up being useless
     * @param move move to remove
     * @return True if successful removal false if not
     */
    public boolean removeMove(Move move){
        if (Moves.contains(move)){
            Moves.remove(move);
            return true;
        }
        else{
            return false;
        }

    }
    /**
     * Get the total number of moves made during this turn
     * @return
     */
    public int getMoveCount(){
        return movecount;
    }

    public Move getMove(int index) {
        return Moves.get(index);
    }

}
