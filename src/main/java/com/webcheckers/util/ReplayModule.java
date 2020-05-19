package com.webcheckers.util;

import com.webcheckers.model.Board;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 */
public class ReplayModule {


    //<replayKey,GameToBeReplayed>
    private static Map<String,Replay> games;    // contains all the games that can be replayed
    private static Map<String, Board> gameBoards;

    public ReplayModule(){
        games=new HashMap<>();
        gameBoards=new HashMap<>();
    }

    /**
     * This function adds the game that was over into the Map that has all other games that can be replayed
     * @param replayKey The key of the particular match that can be replayed
     * @param game The game of type Replay that can be replayed and has all the moves of the match
     */
    public static void setActive(String replayKey,Replay game){
        games.put(replayKey,game);
        gameBoards.put(replayKey,new Board());
    }
    /**
     * @param replayKey The unique String for the key of the Map
     * @return The board that has to be updated every time
     */
    public static Board getReplayBoard(String replayKey){
        return gameBoards.get(replayKey);
    }
    /**
     *
     * @return All the games that can be replayed
     */
    public static Map<String,Replay> getAllGames(){
        return games;
    }

    /**
     * @return All the replayKeys that are available for the games that can be replayed
     */
    public Set<String> getReplayKeys(){
        return this.games.keySet();
    }
    /**
     *
     * @param replayKey The unique String for the key of the Map
     * @return Replay object that has all the moves/turns in it.
     */
    public static Replay getReplayGame(String replayKey){
        return games.get(replayKey);
    }

}
