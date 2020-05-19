package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.BoardDraft;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.ReplayModule;
import spark.*;

import com.webcheckers.util.Message;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  static final String TITLE = "Welcome!";

  static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  static final String TITLE_ATTR = "title";
  static final String MESSAGE_ATTR = "message";
  static final String USER_ATTR = "currentUser";
  static final String LOBBY_ATTR = "players";
  static final String MATCHES_ATTR = "matches";
  static final String CURRENT_MATCH_ATTR = "currentMatch";
  static final String REPLAYS_ATTR = "replays";

  private final TemplateEngine templateEngine;


  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {

    final Session session = request.session();

    LOG.finer("GetHomeRoute is invoked.");

    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_ATTR, TITLE);

    // Display a user message in the Home page
    vm.put(MESSAGE_ATTR, WELCOME_MSG);

    // Fetch the client-specific user from the session.
    Player currentUser = session.attribute(GameRoute.CURRENT_USER_ATTR);

    //Get the gameID from the session
    String gameID = session.attribute(GameRoute.GAME_ID_ATTR);

    //Fetch the game
    Game game = GameHelper.activeGames.get(gameID);
    PlayerLobby players = WebServer.lobby;
    //User went home while in an active match
    if(currentUser != null && currentUser.inMatch) {
      // Provide option to reenter into match from home page.
      vm.put(CURRENT_MATCH_ATTR, GameHelper.fetchCompetitor(currentUser));

      // Restore draft board if user went home mid move attempt
      BoardDraft boardDraft = GameHelper.fetchBoard(gameID);
      // null if user signed out before going home
      if (boardDraft != null && game.isMyTurn(currentUser)) {
        boardDraft.hardBoardUpdate(false, true);
        GameHelper.clearMoves(gameID);
      }
    }
    //User went home while watching a replay
    else if(currentUser != null && currentUser.inReplay) {
      currentUser.inReplay = false;

      WebServer.lobby.playerJoin(currentUser);
    }

    if (GameHelper.isInGame(currentUser) && game != null && game.isOver()) {
      if(!players.playerExists(currentUser))
        players.playerJoin(currentUser);
      Player opponent = GameHelper.fetchCompetitor(currentUser);
      if (players.playerExists(opponent)) {
        GameHelper.clearGameData(gameID, currentUser, opponent);
      }
    } else {
      // Check if a user is signed in.
      if (currentUser != null) {
        // Player in active match, must be starting game.
        Player challenger = GameHelper.fetchChallenger(currentUser);
        if (challenger != null) {
          response.redirect(WebServer.GAME_URL);
          halt();
          return null;
        } else if (!players.playerExists(currentUser)) {
          players.playerJoin(currentUser);
        }
      }
    }
    vm.put(USER_ATTR, currentUser);

    // Place list of players in lobby into view-model.
    vm.put(LOBBY_ATTR, players);

    Map<Player, Player> matches = GameHelper.fetchMatches();
    if (matches.size() > 0) {
      vm.put(MATCHES_ATTR, GameHelper.fetchMatches());
    }

    //TODO: Create replay module and uncomment this line
    vm.put(REPLAYS_ATTR, ReplayModule.getAllGames());
    // render the View
    return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}
