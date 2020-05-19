package com.webcheckers.util;

import com.webcheckers.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 *@author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 */
public class Replay {
    // list to store the turns that were made throughout the game
    private List<Turn> turnsTaken;
    private Board replayBoard;
    private int replayTurnIndex;
    private List<Piece> replayPiecesRemoved;
    private List<Integer> turnsSinceKing;

    public Replay(){
        this.turnsTaken = new ArrayList<>();
        this.turnsTaken.add(0,null);// adding the null in the starting because there was no turn made
        this.replayBoard = new Board();
        this.replayTurnIndex = 0;
        this.replayPiecesRemoved = new ArrayList<>();
        this.turnsSinceKing = new ArrayList<>();
    }

    //Initially setting up all the moves for a particular game

    /**
     * Adds a turn to the turn list.
     * @param turn
     */
    public void addTurn(Turn turn){
        // add to total turns in list
        turnsTaken.add(turn);
    }

    /**
     * Returns the turn the Replay is currently on
     */
    public Turn getCurrentTurn(){
        int currIdx = getReplayIndex();
        if (turnsTaken.size() < currIdx){
            return null;
        }
        return this.turnsTaken.get(currIdx);
    }


    // Following 2 methods return the next and previous move played by the player in the game

    /**
     * Retrieves the next move made in the game
     * also sets the next move to be the currently selected move in the replay
     * @return Boolean whether the next turn was made or not
     */
    public Boolean takeTurnNext() {
        // increments turn index to be next turn
        nextIndex(true);
        int currIdx = getReplayIndex();
        if(turnsTaken.size()-1 < currIdx){
            return false;
        }
        Turn turn = this.turnsTaken.get(getReplayIndex());
        for (int i = 0; i < turn.getMoveCount(); i++) {
            Move move = turn.getMove(i);
            Position start = move.getStart();
            Position end = move.getEnd();

            if (Math.abs(start.getRow() - end.getRow()) > 1) {
                Position middlePos = new Position((start.getCell() + end.getCell())/2,
                        (start.getRow() + end.getRow())/2);
                Spot jumped = replayBoard.getPosition(new Position(middlePos.getCell(), middlePos.getRow()));
                addPieceRemoved(jumped.moveFrom());
            }

            replayBoard.updateBoard(move,false);
            Piece activePiece = replayBoard.getPieceAt(end);

            if (replayBoard.getPosition(end).isNewKing() &&
                    !(activePiece.getType() == Piece.Type.KING)) {
                activePiece.setKing(true);
                addNewKing();
            }
        }
        replayBoard.debugPrint();
        updateTimeSinceKing(true);
        return true;
    }

    /**
     * Retrieves the previous move made in the game
     * also sets the previous move to be the currently selected move in the replay
     * @return Boolean whether the previous turn was made or not
     */
    public Boolean takeTurnPrevious(){
        int currIdx = getReplayIndex();
        if(currIdx <= 0){
            return false;
        }
        Turn turn = this.turnsTaken.get(currIdx);
        for (int i = turn.getMoveCount()-1 ; i >=0 ; i--) {
            Move move = turn.getMove(i);
            Position current = move.getEnd();
            Position prev = move.getStart();
            if (Math.abs(current.getRow() - prev.getRow()) > 1) {
                Position middlePos = new Position((current.getCell() + prev.getCell())/2,
                        (current.getRow() + prev.getRow())/2);
                Spot jumped = replayBoard.getPosition(new Position(middlePos.getCell(), middlePos.getRow()));
                Piece lastRemoved = getLastPieceRemoved();
                jumped.moveTo(lastRemoved);
            }
            replayBoard.updateBoard(move,true);
            Piece activePiece = replayBoard.getPieceAt(prev);
            if (getTurnsSinceKing() == 1) {
                activePiece.setKing(false);
                removeLastKing();
            }
        }
        // decrements turn index to be the previous turn
        nextIndex(false);
        replayBoard.debugPrint();
        updateTimeSinceKing(false);
        return true;
    }

    /**
     * Resets the replay to start at the beginning
     * by iterating through all current moves replayed
     * and reversing them
     * @Param board board to update
     */
    public void resetReplay(){
        Boolean prev=true;
        while(prev){
            prev=takeTurnPrevious();
        }
    }


    public int getReplayIndex() {
        return this.replayTurnIndex;
    }

    public void nextIndex(boolean incr) {
        if (incr) {
            this.replayTurnIndex++;
        } else this.replayTurnIndex--;
    }

    public Piece getLastPieceRemoved() {
        return replayPiecesRemoved.remove(replayPiecesRemoved.size()-1);
    }

    public void addPieceRemoved(Piece removed) {
        this.replayPiecesRemoved.add(removed);
    }

    public Board getBoard() {
        return this.replayBoard;
    }

    public boolean reachedEnd() {
        return this.replayTurnIndex == turnsTaken.size()-1;
    }

    public boolean atStart() {
        return this.replayTurnIndex == 0;
    }

    public Replay (Replay copyOf) {
        this.turnsTaken = copyOf.turnsTaken;
        this.replayBoard = new Board();
        this.replayTurnIndex = 0;
        this.replayPiecesRemoved = new ArrayList<>();
        this.turnsSinceKing = new ArrayList<>();
    }

    private void updateTimeSinceKing(boolean incr) {
        if (!turnsSinceKing.isEmpty()) {
            int recentIndex = turnsSinceKing.size() - 1;
            int pastValue = turnsSinceKing.get(recentIndex);
            pastValue += (incr ? 1 : -1);
            turnsSinceKing.set(turnsSinceKing.size() - 1, pastValue);
        }
    }

    private int getTurnsSinceKing() {
        if (!turnsSinceKing.isEmpty()) {
            return turnsSinceKing.get(turnsSinceKing.size() - 1);
        } return -1;
    }

    private void removeLastKing() {
        if (!turnsSinceKing.isEmpty()) {
            turnsSinceKing.remove(turnsSinceKing.size() - 1);
            updateTimeSinceKing(true);
        }
    }

    private void addNewKing() {
        turnsSinceKing.add(0);
    }

}
