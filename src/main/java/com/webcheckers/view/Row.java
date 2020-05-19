package com.webcheckers.view;

import java.util.Iterator;
import java.util.List;

/**
 * Contains the state of a specific row of a checkers board.
 *
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
public class Row {
	// Private data to maintain all spaces in a row of the checkerboard.
	private List<Space> spaces;

	// Index of the row on the checkerboard.
	private int index;

	/**
	 * Creates a new Row object by instantiating
	 * the index and list of Space objects that represent the row.
	 * @param index: Index of the row.
	 * @param spaces: List of spaces that make up the row.
	 */
	public Row(int index, List<Space> spaces) {
		this.index = index;
		this.spaces = spaces;
	}

	/**
	 * Fetches the index of the row.
	 * @return Primitive int to represent the row index.
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Fetches an iterator of the Space objects within the row.
	 * @return Iterator object to search through each Space object.
	 */
	public Iterator<Space> iterator() {
		return this.spaces.iterator();
	}
}
