package com.webcheckers.model;

/**
 * Maintains all information dedicated to a piece on a checkers board.
 *
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
public class Piece {

	// Type of piece options
	public enum Type {
		SINGLE,
		KING
	}

	// Piece color options
	public enum PieceColor {
		RED,
		WHITE
	}

	// Enumeration variables
	private PieceColor color;
	private Type type;

	/**
	 * Constructs a standard single piece object with a provided color.
	 * @param color Color of the piece.
	 */
	public Piece(PieceColor color) {
		this.color = color;
		this.type = Type.SINGLE;
	}

	/**
	 * Fetches the type of piece for the referenced Piece object.
	 * @return Type enum: KING if king piece, SINGLE otherwise.
	 */
	public Type getType() { return this.type; }

	/**
	 * Gets the color of the referenced Piece object.
	 * @return PieceColor enum object: RED if red piece, BLACK if black piece
	 */
	public PieceColor getColor() { return this.color; }

	/**
	 * Assigns a Piece object the status of king.
	 */
	public void setKing(boolean king) {
		this.type = (king ? Type.KING : Type.SINGLE);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Piece) {
			Piece piece = (Piece) other;
			return (this.getType().equals(piece.getType()) && this.getColor().equals(piece.getColor()));
		} else {
			return false;
		}
	}
}
