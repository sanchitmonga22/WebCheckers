package com.webcheckers.view;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Spot;

/**
 * Maintains state for a given space on the checkers board.
 *
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 * @author <a href='mailto:csc9411@rit.edu>Christopher Curtice</a>
 */
public class Space {

	// Index of the space within its row.
	public int cellIdx;

	// Piece object occupying the space.
	public Piece piece;

	// Color of the Space object.
	public Spot.Color color;

	private boolean finalRow;

	/**
	 * Constructs a new space object to represent
	 * a location on the checkerboard.
	 * @param x: Index of the space within its row.
	 * @param color: Color of the space on the checkerboard.
	 * @param piece: Piece object if one exists on the space
	 *               Null otherwise.
	 */
	public Space(int x, Spot.Color color, Piece piece, boolean finalRow) {
		this.cellIdx = x;
		this.color = color;
		this.piece = piece;
		this.finalRow = finalRow;
	}

	/**
	 * Determines if the space is available to be occupied by a Piece object.
	 * @return True if space may be occupied.
	 * 			False otherwise.
	 */
	public boolean isValid() {
		return this.color == Spot.Color.DARK && this.piece == null;
	}


	/**
	 * Fetches the index of the space within its row.
	 * @return Index of space.
	 */
	public int getIndex() {
		return this.cellIdx;
	}

	/**
	 * Fetches the piece contained within a space of the checkers board.
	 * @return Piece object within a referenced Space object.
	 */
	public Piece getPiece() {
		return this.piece;
	}

	/**
	 * Move a given piece (taken using moveFrom()) to this spot
	 * @param piece: Piece object being moved into this spot.
	 */
	public void moveTo(Piece piece){
		//TODO: implement error when called with piece on spot
		//TODO: Implement error when called on Light spot
		this.piece = piece;
		if (this.finalRow && piece.getType() != Piece.Type.KING) {
			piece.setKing(true);
		}
	}

	/**
	 * Move a piece from this spot to another spot, by removing the piece before returning it
	 * @return Piece object being moved from this spot.
	 */
	public Piece moveFrom(){
		//TODO: implement error when called with no piece on spot
		Piece moved = this.piece;
		this.piece = null;
		return moved;
	}
}
