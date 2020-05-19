package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.model.Spectator;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.Objects;
import java.util.logging.Logger;

public class GetSpectatorStopWatching implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatching.class.getName());

    private final TemplateEngine templateEngine;

    private final Gson gson;

    /**
     * Constructor of the route class, sets the gson and template engine
     * @param templateEngine engine being used
     */
    public GetSpectatorStopWatching(TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("Spectator stop watching  is initialized.");
        gson = new Gson();
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("Spectator stop watching was invoked");

        //Gets the game and current player
        String gameID = request.session().attribute(GetSpectatorGame.GAME_ID_ATTR);
        Player self = request.session().attribute(GetSpectatorGame.CURRENT_USER_ATTR);

        Message message = Message.info("Spectator left the game");

        self.spectating = false;
        WebServer.lobby.playerJoin(self);
        //Redirects the user to the home url
        response.redirect(WebServer.HOME_URL);

        //Returns nothing
        return null;
    }
}
