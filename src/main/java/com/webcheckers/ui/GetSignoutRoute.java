package com.webcheckers.ui;

import com.webcheckers.model.BoardDraft;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to sign out
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:ajt7593@rit.edu'>Andrew Tarcza</a>
 */
public class GetSignoutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SigninRoute.class.getName());

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *
     * the HTML template rendering engine
   */
    public GetSignoutRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSignoutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetSignoutRoute is invoked.");

        Session session = request.session();

        //Remove the player from the lobby
        // handing over the exited player's session attributes
        Player exitedPlayer = session.attribute(GameRoute.CURRENT_USER_ATTR);
        WebServer.lobby.removePlayer(exitedPlayer);


        //Free up the username
        WebServer.reservedUsernames.remove(exitedPlayer.getUsername());


        if (exitedPlayer.inMatch) {
            // Restore draft board if user signed-out mid move attempt
            String gameID = String.valueOf(GameHelper.getGameID(exitedPlayer));
            Game game = GameHelper.activeGames.get(gameID);
            if (game.isMyTurn(exitedPlayer)) {
                BoardDraft boardDraft = GameHelper.fetchBoard(gameID);
                boardDraft.hardBoardUpdate(false, true);
                GameHelper.clearMoves(gameID);
            }
        }

        //Clear the session
        session.invalidate();

        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
