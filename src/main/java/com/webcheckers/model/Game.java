package com.webcheckers.model;

import static com.webcheckers.model.Piece.PieceColor.RED;
import static com.webcheckers.model.Piece.PieceColor.WHITE;

/**
 * Controller for the game logic of WebCheckers.
 *
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
public class Game {
	// Specifies the type of player that is in the game.
	public enum Visitor {
		PLAY,
		SPECTATOR,
		REPLAY
	}

	private Player activePlayer, idlePlayer, resignedPlayer;
	private Piece.PieceColor activeColor;
	private int piecesRed, piecesWhite;

	private boolean resigned = false;

	/**
	 * Creates a new Game object to handle inter-game coordination.
	 * @param activePlayer Player making the first move.
	 * @param idlePlayer Player not active for the first move.
	 */
	public Game(Player activePlayer, Player idlePlayer) {
		this.activePlayer = activePlayer;
		this.idlePlayer = idlePlayer;
		this.activeColor = RED;
		this.piecesRed = 12;
		this.piecesWhite = 12;
	}


	public int numberOfPieces(Piece.PieceColor pieceColor) {
		return (pieceColor == RED ? piecesRed : piecesWhite);
	}


	/**
	 * Switches the turn of the active player and idle player.
	 * Modified the active color attribute.
	 */
	public void changeTurn() {
		this.activeColor = (this.activeColor == RED ? WHITE : RED);

		Player temp = this.activePlayer;
		this.activePlayer = this.idlePlayer;
		this.idlePlayer = temp;

	}


	/**
	 * Compares the active player against a provided player
	 * to determine if it is their turn.
	 * @param player Player being compared against the active player.
	 * @return True if it is the turn of the provided Player object.
	 * 		   False otherwise.
	 */
	public boolean isMyTurn(Player player) {
		return player.equals(this.activePlayer);
	}


	/**
	 * Fetches the piece color of the active player.
	 * @return PieceColor object (RED) or (WHITE).
	 */
	public Piece.PieceColor getActiveColor() {
		return this.activeColor;
	}

	public boolean isOver() {
		return this.piecesRed == 0 || this.piecesWhite == 0 || this.resigned;
	}

	public void decrementPieces(Piece.PieceColor pieceColor) {
		if (pieceColor == RED) {
			this.piecesRed--;
		} else {
			this.piecesWhite--;
		}
	}

	public void resign(Player player) {
		this.resigned = true;
		this.resignedPlayer = player;
	}

	public Player getResignedPlayer() {
		return this.resignedPlayer;
	}

	public void resetResignFlag() {
		this.resigned = false;
		this.resignedPlayer = null;
	}
}