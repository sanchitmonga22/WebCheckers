package com.webcheckers.util;

import com.webcheckers.model.*;

import static com.webcheckers.model.Piece.PieceColor.RED;
import static com.webcheckers.model.Piece.PieceColor.WHITE;

/**
 * Utility class to model the validation logic for
 * moving a piece on the board.
 *
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
public class MoveValidation {

	/**
	 * List of all types of moves that can be
	 * performed on a single iteration of a move.
	 */
	public enum MoveType {
		STANDARD_SINGLE,
		STANDARD_KING,
		JUMP_SINGLE,
		MULTI_KING,
		INVALID
	}
	// TODO Bug: King piece single turn reports more jumps available when a piece could be jumped in the next turn.
	/**
	 * Runs a validation check on any move that is a standard
	 * move type from a single piece.
	 * @return True if a move by a single piece type is valid.
	 */
	public static boolean checkStandardTypeSingle(Move move, String gameID) {
		Game game = GameHelper.activeGames.get(gameID);

		// checks to make sure piece moved forward
		if (GameHelper.moveStackEmpty(gameID)) {
			if (game.getActiveColor() == RED) {
				return move.getEnd().getRow() > move.getStart().getRow();
			} else {
				return move.getEnd().getRow() < move.getStart().getRow();
			}
		}
		return false;
	}

	/**
	 * Runs a validation check on any move that is a standard
	 * move type by a king piece.
	 * @return True if the standard type move by a king piece
	 * 		   type is valid.
	 */
	public static boolean checkStandardTypeKing(Board board, Move move, String gameID) {
		// if the king's start row is not equal to end row
		if (GameHelper.moveStackEmpty(gameID)) {
			return move.getEnd().getRow() != move.getStart().getRow();
		}
		return false;
	}

	/**
	 * Runs a validation check on any jump type move by a
	 * single piece.
	 * @return True if the jump type move by the single piece
	 * 		   type is valid.
	 */
	public static boolean checkJump(Board board, Move move, String gameID) {
		Game game = GameHelper.activeGames.get(gameID);

		// Row of the spot that is being jumped

		int jumpRow, jumpCell;
		int xStart, xEnd, yStart, yEnd;


		xStart = move.getStart().getCell();
		yStart = move.getStart().getRow();
		xEnd = move.getEnd().getCell();
		yEnd = move.getEnd().getRow();

		// row of the spot being jumped
		jumpRow = (yStart - (yStart - yEnd) / 2);

		// cell of the spot being jumped
		jumpCell = (xStart - (xStart - xEnd) / 2);

		Position jumpPosition = new Position(jumpCell, jumpRow);

		// If there is a piece in the spot to jump && that piece is not the same color as the piece making the jump
		if (board.getPieceAt(jumpPosition) != null &&
				(board.getColorAt(jumpPosition) != game.getActiveColor())) {
			return true; // valid jump
		}
		return false; // invalid jump
	}

	/**
	 * Runs a validation check on any jump type move
	 * by a king piece.
	 * @return True if the jump type move by the king piece
	 * 		   type is valid.
	 */
	public static boolean checkJumpTypeKing(Board board, Move move, String gameID) {
		return checkJump(board, move, gameID);
	}
	/**
	 * Checks if no more jumps are possible by a single
	 * piece type.
	 * @return True if there are no possible jumps that
	 * 		   can be made.
	 */
	static boolean noMoreJumpsStandard(Board board, Position pieceStart) {
		// Starting row and cell of the Position
		int startRow = pieceStart.getRow();
		int startCell = pieceStart.getCell();

		Position upLeft = new Position(startCell - 1, startRow + 1);
		Position upRight = new Position(startCell - 1, startRow - 1);
		Position adjLeft = new Position(startCell - 1, startRow + 2);
		Position adjRight = new Position(startCell - 1, startRow - 2);

		//Piece being moved
		Piece piece = board.getPosition(pieceStart).getPiece();
		// checks that the piece does not have any jumps to make in the start position of the move
		// Checks one possible diagonal jump
		if (board.getPosition(upLeft).getPiece() != null){
			// if adjacent spot does not have the same color and the
			// spot after that is empty, then a jump is available
			if (board.getPosition(upLeft).getPiece().getColor() != piece.getColor()
					&&  (board.getPosition(adjLeft).getPiece() == null)){
				return false;
			}
		}
		//Checks the other diagonal jump
		else if (board.getPosition(upRight).getPiece() != null){
			// if adjacent spot does not have the same color and the
			// spot after that is empty, then a jump is available
			return !(board.getPosition(upRight).getPiece().getColor() != piece.getColor()
					&& (board.getPosition(adjRight).getPiece() == null));
		}
		//It returns true if it didn't meet the two diagonal jump conditions
		return true;
	}

	/**
	 * Checks if no more jumps are possible by a king
	 * piece type.
	 * @return True if there are no possible jumps that
	 * 		   can be made.
	 */
	public static boolean noMoreJumpsKing(Board board, Position pieceStart) {
		// row/column integers for move start location
		int startRow = pieceStart.getRow();
		int startCell = pieceStart.getCell();

		Position upLeft = new Position(startCell + 1, startRow + 1);
		Position upRight = new Position(startCell + 1, startRow - 1);
		Position adjLeft = new Position(startCell + 1, startRow + 2);
		Position adjRight = new Position(startCell + 1, startRow - 2);

		// piece to bottom right of start
		Piece bottomRight = board.getPieceAt(upLeft);
		// piece to bottom right 2 spaces from start
		Piece bottomRight2 = board.getPieceAt(adjLeft);

		// piece to bottom left of start
		Piece bottomLeft = board.getPieceAt(upRight);

		// piece to bottom left 2 spaces from start
		Piece bottomLeft2 = board.getPieceAt(adjRight);


		// piece being moved
		Piece piece = board.getPosition(pieceStart).getPiece();

		// checks spot to front right and front left of piece
		if (!noMoreJumpsStandard(board, pieceStart)) {
			return false;
		}

		// check spots to bottom left and right right of piece

		// if spot to bottom right has a piece, and the spot after is not null
		// or the spot to the bottom left has a piece and the spot after is not null
		else if (((bottomRight != null) &&  (bottomRight2 == null)) ||
				((bottomLeft != null) &&  (bottomLeft2 == null))) {

			// if spot to bottom right is not the same color as piece being moved
			if (bottomRight.getColor() != piece.getColor()) {
				return false;
			}
			// if spot to bottom left is not the same color as piece being moved
			else return (bottomLeft.getColor() == piece.getColor());
		}
		return true;
	}


	/**
	 * Obtains the type of move performed by the active user to
	 * cross-reference for validation purposes.
	 * @param board Board object containing present game data.
	 * @param move Move object containing recent move by a user.
	 * @return MoveType enum outlining exact move type that was made.
	 */
	public static MoveType determineMoveType(Board board, Move move) {
		//locations that we started and ended with
		Position previousLocation = move.getStart();
		Position newLocation = move.getEnd();
		//Rows and cells of these locations
		int prevRow = previousLocation.getRow();
		int prevCell = previousLocation.getCell();
		int newRow = newLocation.getRow();
		int newCell = newLocation.getCell();

		// Move type to be assigned and returned.
		MoveType moveType;

		// Calculate the distances moved from the original location.
		int distanceRow = Math.abs(prevRow-newRow);
		int distanceCell = Math.abs(prevCell-newCell);
		//Checks whether the piece type is a king
		if (board.getPieceAt(previousLocation).getType() == Piece.Type.KING) {
			//Checks whether the row and cell distance are the same
			if (distanceRow == distanceCell) {
				if (distanceCell == 1) {
					moveType = MoveType.STANDARD_KING;
				} else { /* distanceCell != 1 */
					moveType = MoveType.MULTI_KING;
				} /* end if/else (distanceCell == 1) */
			} else {
				moveType = MoveType.INVALID;
			} /* end if/else (distanceRow == distanceCell) */
		} else { /* Piece.Type.SINGLE */
			if (distanceRow == 1 && distanceCell == 1) {
				moveType = MoveType.STANDARD_SINGLE;
			} else if (distanceRow == 2 && distanceCell == 2) {
				Piece.PieceColor pieceColor = board.getColorAt(previousLocation);
				if ((pieceColor == RED && newRow > prevRow) ||
						(pieceColor == WHITE && newRow < prevRow)) {
					moveType = MoveType.JUMP_SINGLE;
				} else {
					moveType = MoveType.INVALID;
				}
			} else {
				moveType = MoveType.INVALID;
			} /* end if/else (distanceRow == 1 && distanceCell == 1) */
		} /* end if/else (board.getPosition(...).piece.getType() == Piece.Type.KING */
		return moveType;
	}

	/**
	 * Based on a final board configuration for a turn, it is determined
	 * if another jump move by the active player is possible.
	 * @param gameID Unique identification for game
	 * @param position Position object to represent location of moved piece.
	 * @return True if more jumps are possible
	 * 		   False otherwise.
	 */
	public static boolean hasJumpsAvailable(String gameID, Position position, Move recentMove) {
		//Sets up and instantiates variables for usage
		Board board = GameHelper.fetchBoard(gameID).getDraft();
		Spot currentSpot = board.getPosition(position);
		Position upLeft, upRight,backLeft,backRight;
		Move possibleJumpLeftUp, possibleJumpRightUp, possibleJumpLeftBackward, possibleJumpRightBackward;
		Piece.PieceColor color = currentSpot.getPiece().getColor();

		boolean isKing = currentSpot.getPiece().getType() == Piece.Type.KING;
		boolean retval = false;

		int colForward, colBackward, rowForward, rowBackward;

		rowBackward = position.getRow()-2;
		colForward = position.getCell()+2;
		rowForward = position.getRow()+2;
		colBackward = position.getCell()-2;

		//Checks the color of the current spot and updates the upperLeft and upperRight accordingly
		if (color == RED) {
			upLeft = new Position(colForward, rowForward);
			upRight = new Position(colBackward, rowForward);
			backLeft=new Position(colForward,rowBackward);
			backRight=new Position(colBackward,rowBackward);
		} else {
			upLeft = new Position(colBackward, rowBackward);
			upRight = new Position(colForward, rowBackward);
			backLeft=new Position(colBackward,rowForward);
			backRight=new Position(colForward,rowForward);
		}
		recentMove = recentMove.reverseMove();

		if (Position.inBounds(upLeft) && board.spotEmpty(upLeft)) {
			possibleJumpLeftUp = new Move(position, upLeft);
			if (isKing) {
				if(!possibleJumpLeftUp.equals(recentMove)) {
					retval |= checkJumpTypeKing(board, possibleJumpLeftUp, gameID);
				}
			} else {
				retval |= checkJump(board, possibleJumpLeftUp, gameID);
			}
		}

		if (Position.inBounds(upRight) && board.spotEmpty(upRight)) {
			possibleJumpRightUp = new Move(position, upRight);
			if (isKing) {
				if(!possibleJumpRightUp.equals(recentMove))
				retval |= checkJumpTypeKing(board, possibleJumpRightUp, gameID);
			} else {
				retval |= checkJump(board, possibleJumpRightUp, gameID);
			}
		}

		// Two extra cases for the King as it can move backward
		if(Position.inBounds(backLeft) && board.spotEmpty(backLeft)){
			possibleJumpLeftBackward=new Move(position,backLeft);
			if(isKing && !possibleJumpLeftBackward.equals(recentMove)) {
				retval |= checkJumpTypeKing(board, possibleJumpLeftBackward, gameID);
			}
		}
		if( Position.inBounds(backRight) && board.spotEmpty(backRight)) {
			possibleJumpRightBackward = new Move(position, backRight);
			if (isKing && !possibleJumpRightBackward.equals(recentMove)) {
				retval |= checkJumpTypeKing(board, possibleJumpRightBackward, gameID);
			}
		}

		return retval;
	}

	/**
	 * Given that a single diagonal move was made, it is determined
	 * whether or not a jump move was originally possible or not.
	 * @param gameID String representing key to unique board configuration
	 * @return True if there was no jump originally available.
	 * 		   False otherwise.
	 */
	public static boolean hadNoJump(String gameID) {
		Board currentBoard = GameHelper.fetchBoard(gameID).getDraft();
		boolean hadNoJumpAvailable;

		if (GameHelper.moveStackEmpty(gameID)) {
			hadNoJumpAvailable = true;
		} else {
			Move onlyMove = GameHelper.popMove(gameID);
			onlyMove = onlyMove.reverseMove();
			currentBoard.updateBoard(onlyMove, false);
      
			hadNoJumpAvailable = !hasJumpsAvailable(gameID, onlyMove.getEnd(), onlyMove);
			onlyMove = onlyMove.reverseMove();
			GameHelper.pushMove(gameID, onlyMove);
			currentBoard.updateBoard(onlyMove, false);

		}
		return hadNoJumpAvailable;
	}



	/**
	 * Finds each piece for a given color and determines if a jump move can
	 * be made with that piece. Piece that was most recently moved is not
	 * considered.
	 * @param gameID Unique game identification.
	 * @param newMove Move that was recently performed.
	 * @return Position of a piece that can perform a jump move.
	 */
    public static Position jumpAvailability(String gameID, Move newMove) {
		Board gameBoard = GameHelper.fetchBoard(gameID).getDraft();
		Piece.PieceColor pieceColor = gameBoard.getColorAt(newMove.getStart());
		Position availableSpot = null;
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				Position currPos = new Position(col, row);
				Piece piece = gameBoard.getPieceAt(currPos);
				if (piece != null && piece.getColor() == pieceColor) {
					Position jumpSpotAvailability = new Position(col, row);
					if (!jumpSpotAvailability.equals(newMove.getStart())) {
						newMove.reverseMove();
						if (hasJumpsAvailable(gameID, jumpSpotAvailability, newMove)) {
							availableSpot = jumpSpotAvailability;
						}
						newMove.reverseMove();
					}
				}
			}
		}
		return availableSpot;
	}
}
