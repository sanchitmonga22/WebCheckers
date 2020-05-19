package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import com.webcheckers.util.MoveValidation;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.GameRoute.ACTIVE_COLOR_ATTR;
import static com.webcheckers.ui.GameRoute.GAME_ID_ATTR;

/**
 * Handles the Submit Turn HTTP request
 *
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
public class SubmitTurnRoute implements Route {

	private static final Logger LOG = Logger.getLogger(SubmitTurnRoute.class.getName());
	public static final String SUBMIT_ATTR = "submit";
	private final TemplateEngine templateEngine;
	private static final String JUMP_AVAILABLE_MESSAGE = "Jumps are available!";
	private static final String TURN_END_MESSAGE = "You have ended your turn.";
	private final Gson gson;


	/**
	 * Constructs a new Route object for handling Submit Turn requests.
	 * @param templateEngine The HTML template rendering engine.
	 */
	public SubmitTurnRoute(final TemplateEngine templateEngine){
		this.templateEngine = Objects.requireNonNull(templateEngine);
		LOG.config("SubmitTurn was initialized");
		gson = new Gson();
	}


	/**
	 * Handles the SubmitTurn HTTP request.
	 * @param request HTTP Request
	 * @param response HTTP Response
	 * @return JSON object containing a Message of type INFO.
	 */
	@Override
	public Object handle(Request request, Response response){
		LOG.finer("SubmitTurn has been invoked");
		Session session = request.session();

		session.attribute(SUBMIT_ATTR, true);
		String gameID = session.attribute(GAME_ID_ATTR);
		Move recentMove = GameHelper.popMove(gameID);
		GameHelper.pushMove(gameID, recentMove);
		Position endPosition = recentMove.getEnd();
		MoveValidation.MoveType moveType = session.attribute(ValidateMoveRoute.MOVE_TYPE_ATTR);


		if (moveType == MoveValidation.MoveType.STANDARD_SINGLE ||
				moveType == MoveValidation.MoveType.STANDARD_KING) {
			if (!MoveValidation.hadNoJump(gameID)) {
				return gson.toJson(Message.error(JUMP_AVAILABLE_MESSAGE));
			}
		} else if (!GameHelper.previousMoveWasStandard(gameID) &&
				MoveValidation.hasJumpsAvailable(gameID, endPosition, recentMove)) {
			return gson.toJson(Message.error(JUMP_AVAILABLE_MESSAGE));
		}

		Game game = GameHelper.activeGames.get(gameID);
		// storing the moves before we remove the pieces.
		GameHelper.storeMoves(session.attribute(GAME_ID_ATTR));
		GameHelper.removePieces(session.attribute(GAME_ID_ATTR), game);// removing all the pieces here

		Board draft  = GameHelper.fetchBoard(gameID).getDraft();
		if (draft.getPosition(endPosition).isNewKing()) {
			Piece king = draft.getPieceAt(endPosition);
			king.setKing(true);
			GameHelper.refreshBoard(gameID, true);
		} else {
			GameHelper.refreshBoard(gameID, false);

		}

		game.changeTurn();
		session.attribute(ACTIVE_COLOR_ATTR, game.getActiveColor());
		GameHelper.turnOver(session.attribute(GameRoute.GAME_ID_ATTR));
		return gson.toJson(Message.info(TURN_END_MESSAGE));
	}
}