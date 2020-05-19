package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import com.webcheckers.view.BoardView;
import com.webcheckers.view.Row;
import com.webcheckers.view.Space;
import spark.*;

import java.util.*;
import java.util.logging.Logger;


import static com.webcheckers.model.Game.Visitor.PLAY;
import static com.webcheckers.model.Piece.PieceColor.RED;
import static com.webcheckers.model.Piece.PieceColor.WHITE;
import static com.webcheckers.ui.GetReplayStartWatchingRoute.GAME_REPLAY_KEY_ATTR;

/**
 * The UI Controller for both GET/POST for the Game page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
public class GameRoute implements Route {
	private static final Logger LOG = Logger.getLogger(GameRoute.class.getName());

	private final TemplateEngine templateEngine;
	public static final String TITLE = "Welcome to the game.";
	public static final String TITLE_ATTR = "title";
	public static final String CURRENT_USER_ATTR = "currentUser";
	public static final String VIEW_MODE_ATTR = "viewMode";
	public static final String ACTIVE_COLOR_ATTR = "activeColor";
	public static final String RED_PLAYER_ATTR = "redPlayer";
	public static final String WHITE_PLAYER_ATTR = "whitePlayer";
	public static final String BOARD_ATTR = "board";
	public static final String COMPETITOR_ATTR = "competitor";
	public static final String MESSAGE_ATTR = "message";
	public static final String GAME_ID_ATTR = "gameID";
	private static final String LOBBY_ATTR = "players";
	private static final String ERROR_PLAYER_IN_MATCH = "Player already in match";
	private static final int BOARD_WIDTH = 8;
	private static final String GAME_OVER_MSG_ATTR = "gameOverMessage";
	private static final String MODE_OPTIONS_ATTR = "modeOptionsAsJSON";
	private static final String GAME_OVER_ATTR = "isGameOver";
	private Gson gson;


	/**
	 * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
	 *
	 * @param templateEngine
	 *   the HTML template rendering engine
	 */
	public GameRoute(final TemplateEngine templateEngine) {
		this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
		//
		LOG.config("GameRoute is initialized.");
		gson = new Gson();
	}

	/**
	 * Render the WebCheckers Game page.
	 *
	 * @param request
	 *   the HTTP request
	 * @param response
	 *   the HTTP response
	 *
	 * @return
	 *   the rendered HTML for the Game page
	 */
	@Override
	public Object handle(Request request, Response response) {
		Map<String, Object> vm = new HashMap<>();
		Session session = request.session();

		// Check whether request is GET or POST, and invoke appropriate handler
		if(request.requestMethod().equals("GET")){
			LOG.finer("GetGameRoute is invoked.");

			// get player on local machine
			Player selfReference = session.attribute(CURRENT_USER_ATTR);
			Player competitor = GameHelper.fetchCompetitor(selfReference);
			// assign attributes common to the game route handling
			assignCommonAttributes(vm, selfReference);


			if (selfReference.inMatch || (GameHelper.isInGame(selfReference) && !competitor.inMatch)) {
				assignActiveMatchAttributes(vm, request);
			} else {
				assignNewMatchAttributes(vm, request);
				if (vm.get(GetHomeRoute.LOBBY_ATTR) != null) {
					return templateEngine.render(new ModelAndView(vm, "home.ftl"));
				}
				selfReference.inMatch = true;
				// render the View
				return templateEngine.render(new ModelAndView(vm, "game.ftl"));
			}
		}
		return templateEngine.render(new ModelAndView(vm, "game.ftl"));
	}


	/**
	 * Assigns the attributes that are necessary for te view-model irrespective
	 * of the redirect of this route.
	 * @param vm View-model map for attributes.
	 * @param self Player object representing the
	 *               active user on the local machine.
	 */
	private void assignCommonAttributes(Map<String, Object> vm, Player self) {
		// Initialize view
		vm.put(TITLE_ATTR, TITLE);
		vm.put(CURRENT_USER_ATTR, self);
		vm.put(VIEW_MODE_ATTR, PLAY);
	}


	/**
	 * Assigns the attributes and determines the routing
	 * details for a user that is presently in a checkers match.
	 * @param vm View-model map for attributes.
	 * @param request HTTP request for this route.
	 */
	private void assignActiveMatchAttributes(Map<String, Object> vm, Request request) {
		String gameID;
		Player selfReference = (Player)vm.get(CURRENT_USER_ATTR);
		Player redPlayer, whitePlayer;
		Session session = request.session();
		gameID = session.attribute(GAME_ID_ATTR);

		// Session is out of date (user signed out and back in
		if (gameID == null) {
			restoreSessionAttributes(session, selfReference);
			gameID = session.attribute(GAME_ID_ATTR);
		}

		redPlayer = session.attribute(RED_PLAYER_ATTR);
		whitePlayer = session.attribute(WHITE_PLAYER_ATTR);

		vm.put(GAME_ID_ATTR, gameID);
		vm.put(RED_PLAYER_ATTR, redPlayer);
		vm.put(WHITE_PLAYER_ATTR, whitePlayer);

		Game game = GameHelper.activeGames.get(gameID);
		boolean isActivePlayer = game.isMyTurn(selfReference);
		if (game.isMyTurn(redPlayer)) {
			vm.put(ACTIVE_COLOR_ATTR, RED);
		} else {
			vm.put(ACTIVE_COLOR_ATTR, WHITE);
		}

		final Map<String, Object> modeOptions = fetchModeOptionAttributes(
				selfReference, redPlayer, whitePlayer, game);

		//Check if the player resignation flag is set
		if(game.getResignedPlayer() != null) {
			//Send the proper game over fields to the JSON
			vm.put(MODE_OPTIONS_ATTR, gson.toJson(modeOptions));
		}

		if (selfReference.equals(redPlayer)) {
			vm.put(BOARD_ATTR, createBoardView(RED, gameID, isActivePlayer));
		} else {
			vm.put(BOARD_ATTR, createBoardView(WHITE, gameID, isActivePlayer));
		}

		boolean gameIsOver = game.isOver();

		if (gameIsOver) {
			Player currentUser = session.attribute(CURRENT_USER_ATTR);
			currentUser.inMatch = false;
		}

		modeOptions.put(GAME_OVER_ATTR, gameIsOver);
		vm.put(MODE_OPTIONS_ATTR, gson.toJson(modeOptions));
	}


	/**
	 * Assigns the attributes for a user that is
	 * attempting to enter a new match.
	 * @param vm View-model map for attributes.
	 * @param request HTTP request for this route.
	 */
	private void assignNewMatchAttributes(Map<String, Object> vm, Request request) {
		Session session = request.session();
		String gameID;
		Player selfReference = session.attribute(CURRENT_USER_ATTR);

		// These parameters are the same regardless
		// if user was challenged or is challenging
		vm.put(ACTIVE_COLOR_ATTR, RED);

		String competitorName = request.queryParams(COMPETITOR_ATTR);
		Player competitor =
				WebServer.lobby.fetchByUsername(competitorName);

		gameID = String.valueOf(GameHelper.getGameID(selfReference));
		Game game = GameHelper.activeGames.get(gameID);

		if(game != null) {
			game.resetResignFlag();
		}

		vm.put(GAME_ID_ATTR, gameID);

		if (competitor != null) {
			// Not null if client is challenging a competitor
			// Create a new board for the game.

			// Add the new game/board entity to the board map
			BoardDraft newGameBoard = new BoardDraft();
			GameHelper.updateBoard(gameID, newGameBoard);

			// Create a unique identifier for a game ID.
			WebServer.lobby.removePlayer(competitor);
			WebServer.lobby.removePlayer(selfReference);
			GameHelper.addMatch(competitor, selfReference);
			vm.put(RED_PLAYER_ATTR, selfReference);
			vm.put(WHITE_PLAYER_ATTR, competitor);
			vm.put(BOARD_ATTR, createBoardView(RED, gameID, true));
			//vm.put(GAME_REPLAY_KEY_ATTR,GameHelper.getReplayKey(selfReference,competitor));// putting the replayKey for the particular game in the vm
			Game newGame = new Game(selfReference, competitor);
			GameHelper.activeGames.put(gameID, newGame);
		} else {
			// Client was challenged to a match
			if (GameHelper.wasChallenged(selfReference)) {
				competitor = GameHelper.fetchChallenger(selfReference);
				vm.put(RED_PLAYER_ATTR, competitor);
				vm.put(WHITE_PLAYER_ATTR, selfReference);
				vm.put(BOARD_ATTR, createBoardView(WHITE, gameID, false));
			} else {
				//Initialize the view
				vm = new HashMap<>();

				//Create the error message
				Message err = Message.error(ERROR_PLAYER_IN_MATCH);
				vm.put(MESSAGE_ATTR, err);

				//Stay signed in on the new home page
				vm.put(TITLE_ATTR, TITLE);
				vm.put(LOBBY_ATTR, WebServer.lobby);
				vm.put(CURRENT_USER_ATTR, selfReference);
				return;
			}
		}
		session.attribute(CURRENT_USER_ATTR, vm.get(CURRENT_USER_ATTR));
		session.attribute(RED_PLAYER_ATTR, vm.get(RED_PLAYER_ATTR));
		session.attribute(WHITE_PLAYER_ATTR, vm.get(WHITE_PLAYER_ATTR));
		session.attribute(ACTIVE_COLOR_ATTR, vm.get(ACTIVE_COLOR_ATTR));
		session.attribute(GAME_ID_ATTR, gameID);
		//session.attribute(GAME_REPLAY_KEY_ATTR,GameHelper.getReplayKey(selfReference,competitor));// assigning the replayKey for the new match
	}


	private Map<String, Object> fetchModeOptionAttributes(Player self, Player red, Player white, Game game) {

		Map<String, Object> modeOptions = new HashMap<>(2);

		Message message = Message.error("");
		if (self.equals(red)) {
			if (game.numberOfPieces(RED) == 0) {
				message = Message.getLosingMessage(white);
			}
			else if (game.numberOfPieces(WHITE) == 0) {
				message = Message.getWinningMessage(white);
			}
		} else {
			if (game.numberOfPieces(RED) == 0) {
				message = Message.getWinningMessage(red);
			} else if (game.numberOfPieces(WHITE) == 0) {
				message = Message.getLosingMessage(red);
			}
		}

		if (message.getType() != Message.Type.ERROR) {
			modeOptions.put(GAME_OVER_MSG_ATTR, message.getText());
		}

		message = Message.error("");
		if (self.equals(red)) {
			if (white.inMatch && WebServer.lobby.playerExists(white)) {
				message = Message.getResigningMessage(white);
			}
		} else {
			if (red.inMatch && WebServer.lobby.playerExists(red)) {
				message = Message.getResigningMessage(red);
			}
		}
		if (message.getType() != Message.Type.ERROR) {
			modeOptions.put(GAME_OVER_MSG_ATTR, message.getText());
		}

		return modeOptions;
	}


	/**
	 * Helper function to generate a BoardView object.
	 * @return A new BoardView object.
	 */
	public static BoardView createBoardView(Piece.PieceColor playerColor, String gameID, boolean isMyTurn) {
		List<Row> boardRows = new ArrayList<>();
		Board board;

		// Board must be rotated if player color is red.
		if (playerColor == RED) {
			if (isMyTurn) {
				board = GameHelper.fetchBoard(gameID).getDraft().copyAndRotateBoard();
			} else {
				board = GameHelper.fetchBoard(gameID).getLive().copyAndRotateBoard();
			}
		} else {
			if (isMyTurn) {
				board = GameHelper.fetchBoard(gameID).getDraft();
			} else {
				board = GameHelper.fetchBoard(gameID).getLive();
			}
		}


		for (int rowIdx = 0; rowIdx < BOARD_WIDTH; rowIdx++) {
			List<Space> spaces = new ArrayList<>();

			for (int spaceIdx = BOARD_WIDTH - 1; spaceIdx >= 0; spaceIdx--) {
				Position currPos = new Position(spaceIdx, rowIdx);
				// Alternate every spot to a different color.
				Spot.Color color = board.getSpotColorAt(currPos);

				Piece piece = board.getPieceAt(currPos);

				// Create new space and add it to the row of spaces.
				Space space = new Space(spaceIdx, color, piece, rowIdx%7 == 0);
				spaces.add(space);
			}
			// Create a new row and add it to the list of rows (board).
			Row row = new Row(rowIdx, spaces);
			boardRows.add(row);
		}
		return new BoardView(boardRows);
	}


	/**
	 * Reassigns session attributes after a user signs out,
	 * in the event they are still in an active game.
	 * @param session HTTP session type
	 * @param user Player object representing current user.
	 */
	public static void restoreSessionAttributes(Session session, Player user) {
		Player competitor = GameHelper.fetchCompetitor(user);
		Player redPlayer, whitePlayer;
		String gameID = String.valueOf(GameHelper.getGameID(user));
		Game game = GameHelper.activeGames.get(gameID);
		if ( (game.isMyTurn(user) && game.getActiveColor() == RED) ||
				(game.isMyTurn(competitor) && game.getActiveColor() == WHITE)) {
			redPlayer = user;
			whitePlayer = competitor;
		} else {
			redPlayer = competitor;
			whitePlayer = user;
		}
		session.attribute(GAME_ID_ATTR, gameID);
		session.attribute(RED_PLAYER_ATTR, redPlayer);
		session.attribute(WHITE_PLAYER_ATTR, whitePlayer);
		session.attribute(ACTIVE_COLOR_ATTR, game.getActiveColor());
		session.attribute(CURRENT_USER_ATTR, user);
	}
}