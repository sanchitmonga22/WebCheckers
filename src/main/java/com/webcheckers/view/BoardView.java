package com.webcheckers.view;


import java.util.Iterator;
import java.util.List;

/**
 * Maintains the state of the checkers board.
 *
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
public class BoardView {
	// Private data to maintain rows of checkers board.
	private List<Row> rows;

	/**
	 * Creates a new BoardView object by storing the rows
	 * of the checkerboard internally.
	 * @param rows: List of rows of the checkerboard.
	 */
	public BoardView(List<Row> rows) {
		this.rows = rows;
	}


	/**
	 * Fetches an iterator for the rows of the checkerboard.
	 * @return Iterator object for a list of Row objects.
	 */
	public Iterator<Row> iterator() {
		return this.rows.iterator();
	}
}
