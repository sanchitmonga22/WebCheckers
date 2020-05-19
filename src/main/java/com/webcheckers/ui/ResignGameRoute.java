package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to resign from a game
 *
 * @author <a href='mailto:ajt7593@rit.edu'>Andrew Tarcza</a>
 */
public class ResignGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SigninRoute.class.getName());

    private final TemplateEngine templateEngine;

    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /} HTTP requests.
     *
     * @param templateEngine
     *
     * the HTML template rendering engine
     */
    public ResignGameRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("ResignGameRoute is initialized.");
        gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("ResignGameRoute is invoked.");

        //Get the current player object
        Player player = request.session().attribute("player");

        String gameID = request.session().attribute(GameRoute.GAME_ID_ATTR);

        //Successful resignation message
        Message message = Message.info("GAME RESIGNATION SUCCESSFUL");

        Game game = GameHelper.activeGames.get(gameID);

        //Resign from the game and let the server know that this player is no longer in a match
        game.resign(player);
        player.inMatch = false;

        //If I resigned on my turn, change turns to reflect the resignation on the other player's screen
        if(game.isMyTurn(player))
            game.changeTurn();

        return gson.toJson(message);
    }
}
