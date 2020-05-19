package com.webcheckers.model;

/**
 * Maintains data for a placement of a piece
 * on a checkerboard. It is used to recognize
 * the starting and ending positions of a movement
 * made by a user during their turn.
 */
public class Position {
	private int row;
	private int cell;

	/**
	 * Creates a new Position object.
	 * @param row The row of a position.
	 * @param cell The cell of a position
	 *             within a row.
	 */
	public Position(int cell, int row) {
		this.row = row;
		this.cell = cell;
	}

	/**
	 * Fetches the current row of a position.
	 * @return An integer (0 - 7) representing
	 * 		   a row of the checkerboard.
	 */
	public int getRow() {
		return row;
	}


	/**
	 * Fetches the current cell of a position.
	 * @return An integer (0 - 7) representing
	 * 		   the cell of a position in a row.
	 */
	public int getCell() {
		return cell;
	}

	@Override
	public String toString(){
		return "ROW: " + this.row + " and COLUMN: " + this.cell;
	}


	/**
	 * Determines is a given position is in the bounds of the board.
	 * @param pos Position object with column and row coordinates
	 * @return True if the provided Position object is in bounds.
	 * 		   False otherwise.
	 */
	public static boolean inBounds(Position pos) {
		int row = pos.getRow();
		int col = pos.getCell();
		return row >= 0 && row <= 7 && col >= 0 && col <= 7;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position other = (Position) o;
			return this.cell == other.cell &&
					this.row == other.row;
		}
		return false;
	}

}
