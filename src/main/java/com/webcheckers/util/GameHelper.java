package com.webcheckers.util;

import com.webcheckers.model.*;
import java.util.*;


/**
 * Helper class to implement game-related data structures.
 * @author <a href="mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 * @author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 */
public class GameHelper {

	// Map to maintain all active matches.
	private static Map<Player, Player> activeMatches;

	// Map to maintain all active board models.
	private static Map<String, BoardDraft> boardMap;

	// Map to all active game objects
	public static Map<String, Game> activeGames;

	// Map to all active moves of a specific turn in a game.
	private static Map<String, List<Move>> currentMoves;

	// An object that has all the Replay objects and can be accessed with the ReplayKey
	private static ReplayModule replayModule;

	// Has all the gameTurns for the particular game in the order
	private static Map<String,List<Turn>> gameTurns;

	private static Map<Player, Integer> gameIDMap;

	private static Map<String,List<Move>> replayMoves;

	private static int nextGameID;


	/**
	 * Constructs a new utility class to aid in maintaining and
	 * using various data structures for all active checker games.
	 */
	public GameHelper() {
		activeMatches = new HashMap<>();
		boardMap = new HashMap<>();
		activeGames = new HashMap<>();
		currentMoves = new HashMap<>();
		nextGameID = 0;
		gameIDMap = new HashMap<>();
		replayModule=new ReplayModule();
		gameTurns=new HashMap<>();
		replayMoves=new HashMap<>();
	}

  
	/**
	 * Pushes a new move to the stack (list) of moves.
	 * @param gameID String identification needed to fetch the current moves.
	 * @param move Move object most recently performed in the game.
	 */
	public static void pushMove(String gameID, Move move) {
		if (currentMoves.get(gameID) == null) {
			currentMoves.put(gameID, new ArrayList<>());
		}
		currentMoves.get(gameID).add(move);
	}


	/**
	 * Pops the most recent move from the stack (list) of moves.
	 * @param gameID String identification needed to fetch the current moves.
	 * @return Move object that was most recently performed by the active user.
	 */
	public static Move popMove(String gameID) {
		List<Move> moves = currentMoves.get(gameID);
		return moves.remove(moves.size()-1);
	}


	/**
	 * Fetches a board configuration for a given game.
	 * @param gameID Unique identification for
	 *               the game in which the board is being fetched.
	 * @return Board object representing current checker board configuration.
	 */
	public static BoardDraft fetchBoard(String gameID) {
		return boardMap.get(gameID);
	}


	/**
	 * Reassigns the board to an active game.
	 * @param gameID String identification needed to reassign the game board.
	 * @param boardDraft Board object that is replacing the current configuration.
	 */
	public static void updateBoard(String gameID, BoardDraft boardDraft) {
		boardMap.put(gameID, boardDraft);
	}


	/**
	 * Updates the live board with draft board contents.
	 * @param gameID Unique identification for a current game.
	 * @param kingMe A move made by a user resulted in a king creation.
	 */
	public static void refreshBoard(String gameID, boolean kingMe) {
		BoardDraft boardDraft = boardMap.get(gameID);
		boardDraft.hardBoardUpdate(kingMe, false);
		boardMap.put(gameID, boardDraft);
	}


	/**
	 * Retrieves all players that have been challenged to matches.
	 * @return Set object of players currently in game that
	 * 			were originally challenged by their opponent.
	 */
	public static Set<Player> fetchChallengedPlayers() {
		return activeMatches.keySet();
	}


	/**
	 * Fetches a player that has challenged the provided player.
	 * @param challenged Player in which the returning Player
	 *                   challenged to in a match.
	 * @return Player that challenged the provided player to the active match.
	 */
	public static Player fetchChallenger(Player challenged) {
		return activeMatches.get(challenged);
	}


	/**
	 * Determines if a provided player was challenged to a game or not.
	 * @param challenged Player whose state of being challenged is in question.
	 * @return True if provided player was challenged to a match.
	 * 		   False otherwise.
	 */
	public static boolean wasChallenged(Player challenged) {
		return activeMatches.containsKey(challenged);
	}


	/**
	 * Determines if a provided player was the challenger to a game or not.
	 * @param challenger Player whose state of being
	 *                     the challenger is in question.
	 * @return True if provided player was the challenger to a match.
	 * 		   False otherwise.
	 */
	public static boolean wasChallenger(Player challenger) { return activeMatches.containsValue(challenger); }


	/**
	 * Adds a match to the map to represent all active matches.
	 * @param challenged Player that was challenged to a match
	 * @param challenger Player that challenged opponent to match.
	 */
	public static void addMatch(Player challenged, Player challenger) {
		activeMatches.put(challenged, challenger);
	}


	public static Map<Player, Player> fetchMatches() {
		return activeMatches;
	}


	/**
	 * Determines if the stack of moves for a given game is empty or not.
	 * @param gameID Unique string to represent an active game.
	 * @return True if move stack is empty, false otherwise.
	 */
	public static boolean moveStackEmpty(String gameID) {
		List<Move> moves = currentMoves.get(gameID);
		if (moves == null) {
			return true;
		}
		return moves.size() == 0;
	}


	/**
     * When the turn is over adding all the moves of a turn into a Turn object
	 * Resets all necessary data structures at the end of a turn.
	 */
	public static void turnOver(String gameID) {
		Turn turn=new Turn();
		// getting all the moves for the current move after the turn was over and storing it all into a Turn object
		for(Move move: replayMoves.get(gameID)){
		    turn.addMove(move);
        }
		// initially in the start when there were no turns played, create a new arrayList for the turns
		if(gameTurns.get(gameID)==null){
			gameTurns.put(gameID,new ArrayList<>());
		}
		gameTurns.get(gameID).add(turn);// adding all the turns that have been played into the gameTurns object for the particular game
		currentMoves.put(gameID, new ArrayList<>());// once the turn is over, setting the currentMoves list to empty.
		replayMoves.put(gameID,new ArrayList<>());
	}


	/**
	 * Determines if a player is in a game.
	 * @param player Player object being checked for activity.
	 * @return True if player is in an active game.
	 * 			False otherwise.
	 */
	public static boolean isInGame(Player player) {
		return wasChallenged(player) || wasChallenger(player);
	}


	/**
	 * Fetches the game ID of a player or creates/stores a new one.
	 * @param player Player in which a game ID is being fetched for.
	 * @return integer value of unique game identification.
	 */
	public static int getGameID(Player player) {
		if (gameIDMap.containsKey(player)) {
			return gameIDMap.get(player);
		} else if (wasChallenged(player)) {
			return gameIDMap.get(fetchChallenger(player));
		} else {
			gameIDMap.put(player, GameHelper.nextGameID);
			return nextGameID++;
		}
	}


	/**
	 * Removes all pieces that were jumped over for a given turn.
	 * @param gameID Unique game identification.
	 * @param game Game object to maintain the status of a current game
	 */
	public static void removePieces(String gameID, Game game) {
		BoardDraft boardDraft = GameHelper.fetchBoard(gameID);
		while(currentMoves.get(gameID).size() > 0) {
			Move move = GameHelper.popMove(gameID);
			Piece removed = helperRemovePiece(move, boardDraft, false);
			if (removed != null) {
				game.decrementPieces(removed.getColor());
			}
		}
		GameHelper.updateBoard(gameID, boardDraft);
	}


    /**
     * Adds all the moves for the current turn and stores them for the replay mode
     * @param gameID The game id to access the moves of a particular game
     */
    public static void storeMoves(String gameID){
        if(replayMoves.get(gameID)==null){
            replayMoves.put(gameID,new ArrayList<>());
        }
        replayMoves.get(gameID).addAll(currentMoves.get(gameID));
    }

	/**
	 * Helper function used to perform the actual removing
	 * of the checker pieces from the board.
	 * @param move Move object that represents a jump.
	 * @param boardDraft BoardDraft object that contains board information.
	 * @param live Boolean - True if the live board is being updated
	 * @return Piece removed from the board.
	 */
	public static Piece helperRemovePiece(Move move, BoardDraft boardDraft, boolean live) {

		Board board;
		int jumpRow, jumpCell;
		int xStart, xEnd, yStart, yEnd;

		xStart = move.getStart().getCell();
		yStart = move.getStart().getRow();
		xEnd = move.getEnd().getCell();
		yEnd = move.getEnd().getRow();

    	if (Math.abs(xStart - xEnd) >= 2 || Math.abs(yStart-yEnd) >= 2) {
			jumpRow = (yStart - (yStart - yEnd) / 2);
			jumpCell = (xStart - (xStart - xEnd) / 2);
			if (live) {
				board = boardDraft.getLive();
			} else {
				board = boardDraft.getDraft();
			}
			Position jumpLocation = new Position(jumpCell, jumpRow);
			return board.getPosition(jumpLocation).moveFrom();
		}

		return null;
	}


	/**
	 * Terminates any game data at the end of a match.
	 * @param gameID Unique identification for a checkers match.
	 * @param p1 First Player in match.
	 * @param p2 Second Player in match.
	 */
	public static void clearGameData(String gameID, Player p1, Player p2) {
		String replayKey;
		boardMap.remove(gameID);
		currentMoves.remove(gameID);
		activeGames.remove(gameID);
		if (wasChallenged(p1)) {
			replayKey = getReplayKey(p1,p2);
			activeMatches.remove(p1);
			gameIDMap.remove(p2);
		} else {
			replayKey = getReplayKey(p2,p1);
			activeMatches.remove(p2);
			gameIDMap.remove(p1);
		}
		// Once the game is over adding all the turns that have all the moves into the replay object
        // and then setting the particular game as active in the replay Module
		if(gameTurns.get(gameID) != null) {
			Replay replay = new Replay();
			for (Turn turn : gameTurns.get(gameID)) {
				replay.addTurn(turn);
			}
			replayModule.setActive(replayKey, replay);// this means that the particular game can be added to the list of games that can be replayed because they are complete
		}
	}

	/**
	 * The key that is unique identifier for the games saved for the replayMode
	 * @param p1 Player that played the game
	 * @param p2 Player that played the game
	 * @return The string format of the key
	 */
	public static String getReplayKey(Player p1,Player p2){
		return p1.getUsername() + " challenged by " + p2.getUsername();
	}

	public static Boolean replayKeyExists(String key){
		String [] Players=key.split(" challenged by ");
		String OtherKey=Players[1]+" challenged by "+Players[0];
		if(replayModule.getReplayKeys().contains(key) || replayModule.getReplayKeys().contains(OtherKey)) {
			return true;
		}else{
			return false;
		}
	}


	/**
	 * Obtains the competitor of a given player in a checkers match.
	 * @param player Opponent of the competitor being searched for.
	 * @return Competitor of the provided player.
	 */
	public static Player fetchCompetitor(Player player) {
		if (GameHelper.wasChallenged(player)) {
			return GameHelper.fetchChallenger(player);
		} else {
			for (Player challenged : activeMatches.keySet()) {
				if (GameHelper.fetchChallenger(challenged).equals(player)) {
					return challenged;
				}
			}
		}
		return null;
	}


	/**
	 * Fetches the number of moves that were performed for a given turn.
	 * @param gameID Unique game identification
	 * @return Number of moves in a turn.
	 */
	public static int numberOfMoves(String gameID) {
		if (currentMoves.get(gameID) == null) {
			return 0;
		} else {
			return currentMoves.get(gameID).size();
		}
	}


	/**
	 * Determines if the previous move made was a standard (not a jump) move.
	 * @param gameID Unique game identification.
	 * @return True if the last move that was made was a standard move.
	 */
	public static boolean previousMoveWasStandard(String gameID) {
		if (moveStackEmpty(gameID)) {
			return false;
		} else {
			if (numberOfMoves(gameID) == 0) {
				return false;
			} else {
				Move lastMove = popMove(gameID);
				Position start = lastMove.getStart();
				Position end = lastMove.getEnd();
				pushMove(gameID, lastMove);
				return (Math.abs(start.getCell() - end.getCell()) == 1 &&
					Math.abs(start.getRow() - end.getRow()) == 1);
			}
		}
	}

    /**
     * This function returns the Replay object that has all the moves that can be replayed on the board
     * @param replayKey The key for the particular game
     * @return The Replay object that has all the moves of the game that was played before
     */
	public static Replay getReplay(String replayKey) {
		return replayModule.getReplayGame(replayKey);
	}

	public static void clearMoves(String gameID) {
		currentMoves.put(gameID, new ArrayList<>());
	}
	/**
	 * @param replayKey The key to access all the replay objects
	 * @return The board that is being replayed
	 */
    public static Board getReplayBoard(String replayKey){
	    return ReplayModule.getReplayBoard(replayKey);
    }
	/**
	 * @return Collection of the games that can be replayed
	 */
	public static Collection<Replay> getAllGames(){
		return replayModule.getAllGames().values();
	}
}
