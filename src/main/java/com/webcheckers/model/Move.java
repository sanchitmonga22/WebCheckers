package com.webcheckers.model;

/**
 * Represents a move made by a user,
 * and contains both the starting and
 * ending position of said move.
 */
public class Move {

	private Position start, end;

	/**
	 * Creates a new Move object
	 * @param startPosition Starting position of a move
	 *                      made by the active user.
	 * @param endingPosition Ending position of a move
	 *                       made by the active user.
	 */
	public Move(Position startPosition, Position endingPosition) {
		this.start = startPosition;
		this.end = endingPosition;
	}


	/**
	 * Fetches the starting position of a checkers move.
	 * @return A Position object outlining the location
	 * 		   of the checkers piece before a move was made.
	 */
	public Position getStart() {
		return start;
	}


	/**
	 * Fetches the ending position of a checkers move.
	 * @return A Position object outline the location
	 * 		   of the checkers piece after a move was made.
	 */
	public Position getEnd() {
		return end;
	}


	/**
	 * Prepares a revert from a previous move. Returns opposite of a move made
	 */
	public Move reverseMove() {
		return (new Move(this.end,this.start));
	}


	/**
	 * Translates the move made by the red player to a form in which
	 * can be updated in the model.
	 * @return Updated move that accurately reflects the move
	 * 		   made by the red player.
	 */
	public Move invertMove() {
		Position start = new Position(7-this.start.getCell(), 7-this.start.getRow());
		Position end = new Position(7-this.end.getCell(), 7-this.end.getRow());
		return new Move(start, end);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move other = (Move) o;
			return other.start.equals(this.start) &&
					other.end.equals(this.end);
		}
		return false;
	}

	@Override
	public String toString(){
		return "The piece was moved from " + this.start+" to " + this.end;
	}
}
