package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Game;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI controller to the CheckTurn page.
 *
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
public class CheckTurnRoute implements Route {

	private TemplateEngine templateEngine;
	private static final Logger LOG=Logger.getLogger(CheckTurnRoute.class.getName());
	private Gson gson;

	/**
	 * Creates Spark Route for all CheckTurn HTTP requests.
	 * @param templateEngine The HTML template rendering engine.
	 */
	public CheckTurnRoute(TemplateEngine templateEngine) {
		this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
		LOG.config("Check turn route is initialized");
		gson = new Gson();
	}

	/**
	 * Handles the request and response of determining the
	 * active player of a checkers match after a move is made.
	 * @param request The HTTP request being handled
	 * @param response The HTTP response to the request.
	 * @return HTML render response from
	 * 		   the CheckTurnRoute controller handle.
	 */
	@Override
	public Object handle(Request request, Response response) {
		LOG.finer("POST CheckTurnRoute is invoked.");
		Message message;
		Session session = request.session();

		String gameID = session.attribute(GameRoute.GAME_ID_ATTR);

		Game game = GameHelper.activeGames.get(gameID);

		if (session.attribute(SubmitTurnRoute.SUBMIT_ATTR) != null) {
			session.removeAttribute(SubmitTurnRoute.SUBMIT_ATTR);
			message = Message.info("false");
		} else if (game.isMyTurn(session.attribute(GameRoute.CURRENT_USER_ATTR))) {
			message = Message.info("true");
		} else {
			message = Message.info("false");
		}
		return gson.toJson(message);
	}
}
