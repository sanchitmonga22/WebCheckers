package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import com.webcheckers.util.Replay;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;


import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.GetReplayStartWatchingRoute.GAME_REPLAY_KEY_ATTR;


public class GetReplayStopWatchingRoute implements Route {

    private final TemplateEngine templateEngine;
    private final Logger LOG = Logger.getLogger(GetReplayStopWatchingRoute.class.getName());
    private final Gson gson;

    public GetReplayStopWatchingRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("Replay stop watching is initialized.");
        gson = new Gson();

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("Replay stop watching was invoked");

        Player currentUser = request.session().attribute(GameRoute.CURRENT_USER_ATTR);

        //resetting the replayModule to the initial stage
        String replayKey=request.session().attribute(GAME_REPLAY_KEY_ATTR);
        Replay replay=GameHelper.getReplay(replayKey);
        replay.resetReplay();

        //add the player back to the lobby
        WebServer.lobby.playerJoin(currentUser);

        currentUser.inReplay = false;

        Message message = Message.info("User exited replay");

        response.redirect(WebServer.HOME_URL);
        return gson.toJson(message);
    }
}
