package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import com.webcheckers.util.MoveValidation;
import spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.model.Piece.PieceColor.RED;

/**
 * The UI Controller to consider a move
 * made by a competitor in a checkers match.
 *
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
public class ValidateMoveRoute implements Route {
	private static final Logger LOG = Logger.getLogger(ValidateMoveRoute.class.getName());
	private final TemplateEngine templateEngine;
	private final Gson gson;

	public static final String MOVE_ATTR = "move";
	public static final String MOVE_TYPE_ATTR = "moveType";
	private static final String ACTION_ATTR = "actionData";

	/**
	 * Creates Spark Route for all ValidateMove HTTP requests.
	 * @param templateEngine The HTML template rendering engine.
	 */
	public ValidateMoveRoute(final TemplateEngine templateEngine) {
		this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
		LOG.config("ValidateMoveRoute is initialized.");
		gson = new Gson();
	}

	/**
	 * Handle the request to validate a move made by a user
	 * @param request The HTTP request for the validate move page.
	 * @param response The HTTP response
	 * @return The HTML render after handling
	 * 		   the request to validate the move.
	 */
	@Override
	public Object handle(Request request, Response response) {
		LOG.finer("ValidateMoveRoute is invoked.");

		Session session = request.session();

		String gameID = request.queryParams(GameRoute.GAME_ID_ATTR);

		// Fetch the move object outlining the players decision.
		String actionData = request.queryParams(ACTION_ATTR);
		Move move = gson.fromJson(actionData, Move.class);


		// Fetch the game board in question.
		BoardDraft boardDraft = GameHelper.fetchBoard(gameID);
		Board gameBoard = boardDraft.getDraft();

		Game game = GameHelper.activeGames.get(gameID);


		if (game.getActiveColor() == RED) {
			move = move.invertMove();
		}
		// Declare the response to the move.
		Message message;

		// Fetch unique identifier for current game.

		// Fetch the type of move that was made and by what type
		MoveValidation.MoveType moveType =
				MoveValidation.determineMoveType(gameBoard, move);

		if (isRedundantMove(move, gameID)) {
			message = Message.getNoBacktrackingMessage();
		} else {
			// Evaluate the move made by the user.
			message = evaluateMove(moveType, gameBoard, move, gameID);

			// Handle the message information returned to the view.
			if (message.isSuccessful()) {
				GameHelper.pushMove(gameID, move); // adding the moves into the list as the move was valid
				boardDraft.updateDraft(move, false);
				GameHelper.updateBoard(gameID, boardDraft);

				session.attribute(MOVE_ATTR, move);
				session.attribute(MOVE_TYPE_ATTR, moveType);
			}
		}
		return gson.toJson(message);
	}

	/**
	 * Determines if a move made by the active user is valid or not.
	 * @param board The current game board before the move was made
	 *              by the user.
	 * @param move The move by the user, containing the position
	 *             move from and the position that was moved to.
	 * @return INFO if the move that was made is valid as per
	 * 		   the standard rule-set of checkers. ERROR otherwise.
	 */
	private Message evaluateMove(MoveValidation.MoveType moveType,
								 Board board, Move move, String gameID) {

		Message isValidMove;

		Position jumpLocation = MoveValidation.jumpAvailability(gameID, move);

		// Run validation checks based upon the type of move that was made
		switch (moveType) {
			case STANDARD_SINGLE:
				if (GameHelper.numberOfMoves(gameID) != 0) {
					isValidMove = Message.getPreviousMoveMadeMessage();
					break;
				}
				if (jumpLocation != null) {
					isValidMove = Message.getJumpMoveAvailableMessage(jumpLocation);
				} else {
					if (MoveValidation.checkStandardTypeSingle(move, gameID)) {
						isValidMove = Message.getValidMoveMessage();
					} else {
						isValidMove = Message.getBadMoveSingleMessage();
					}
				}
				break;
			case STANDARD_KING:
				if (GameHelper.numberOfMoves(gameID) != 0) {
					isValidMove = Message.getPreviousMoveMadeMessage();
					break;
				}
				if (jumpLocation != null) {
					isValidMove = Message.getJumpMoveAvailableMessage(jumpLocation);
				} else {
					if (MoveValidation.checkStandardTypeKing(board, move, gameID)) {
						isValidMove = Message.getValidMoveMessage();
					} else {
						isValidMove = Message.getBadMoveKingMessage();
					}
				}
				break;
			case JUMP_SINGLE:
				if (GameHelper.previousMoveWasStandard(gameID)) {
					isValidMove = Message.getNoJumpAfterSingleMessage();
				} else {
					if (MoveValidation.checkJump(board, move, gameID)) {
						isValidMove = Message.getValidMoveMessage();
					} else {
						isValidMove = Message.getBadJumpSingleMessage();
					}
				}
				break;
			case MULTI_KING:
				if (GameHelper.previousMoveWasStandard(gameID)) {
					isValidMove = Message.getNoJumpAfterSingleMessage();
				} else {
					if (MoveValidation.checkJumpTypeKing(board, move, gameID)) {
						isValidMove = Message.getValidMoveMessage();
					} else {
						isValidMove = Message.getBadJumpKingMessage();
					}
				}
				break;
			default: // INVALID
				isValidMove = Message.getInvalidMoveMessage();
		}
		return isValidMove;
	}


	/**
	 * Checks if a move being made is backtracking.
	 * Prerequisite: Piece being moved is a king
	 * 				 (backtracking couldn't be attempted otherwise).
	 * @param move Last move that was performed.
	 * @param gameID Unique game identification
	 * @return True if the move being made is a backtrack.
	 */
	private boolean isRedundantMove(Move move, String gameID) {
		List<Move> previousMoves = new ArrayList<>();
		move = move.reverseMove();

		Move fromStack;
		boolean isRedundant = false;

		while (!GameHelper.moveStackEmpty(gameID)) {
			fromStack = GameHelper.popMove(gameID);
			previousMoves.add(fromStack);
			if (move.equals(fromStack)) {
				isRedundant = true;
			}
		}

		while(previousMoves.size() > 0) {
			Move popped = previousMoves.remove(previousMoves.size()-1);
			GameHelper.pushMove(gameID, popped);
		}

		return isRedundant;
	}

}
