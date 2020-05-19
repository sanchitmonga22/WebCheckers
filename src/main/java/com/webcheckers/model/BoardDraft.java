package com.webcheckers.model;

import com.webcheckers.util.GameHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Maintains the model for both the active and waiting players.
 */
public class BoardDraft {
	// current model for the waiting player
	private Board live;
	// current model for the active player
	private Board draft;
	// list of moves made by
	private List<Move> moves;

	public static boolean spectateBoardUpdate;


	/**
	 * Creates new live and draft Board objects, as well as a
	 * list to maintain all moves made during a given turn.
	 */
	public BoardDraft() {
		live = new Board();
		draft = new Board();
		moves = new ArrayList<>();
	}


	/**
	 * Fetches the draft Board object for the respective game.
	 * @return Board object seen by the active player.
	 */
	public Board getDraft() {
		return this.draft;
	}


	/**
	 * Fetches the live Board object for the respective game.
	 * @return Board object seen by the non-active player.
	 */
	public Board getLive() {
		return this.live;
	}


	/**
	 * Updates the draft board with a new move made during a turn.
	 * @param move Move object representing piece movement.
	 * @param reverse Boolean denoting that a backup has occurred.
	 */
	public void updateDraft(Move move, boolean reverse) {
		if (reverse) {
			moves.remove(moves.size()-1);
		} else {
			moves.add(move);
		}
		draft.updateBoard(move, reverse);
	}


	/**
	 * Updates a board with changes or reversions of the other board.
	 */
	public void hardBoardUpdate(boolean kingMe, boolean revert) {
		Move lastMove;
		for (Move move : moves) {
			lastMove = move;
			if (revert) {
				draft.updateBoard(move, true);
			} else {
				live.updateBoard(move, false);
				GameHelper.helperRemovePiece(move, this, true);

				if (kingMe) {
					Piece kingPiece = live.getPieceAt(lastMove.getEnd());
					kingPiece.setKing(true);
				}
			}
		}

		spectateBoardUpdate = true;

		moves = new ArrayList<>();
	}

}
