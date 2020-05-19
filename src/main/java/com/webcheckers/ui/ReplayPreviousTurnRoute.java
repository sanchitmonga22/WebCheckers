package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Board;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import com.webcheckers.util.Replay;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.GetReplayStartWatchingRoute.*;


/**
 * UI Controller for Replay Mode that will update the board with previous turn or the move that was made
 * @author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 */
public class ReplayPreviousTurnRoute implements Route {

    private static final Logger LOG=Logger.getLogger(ReplayNextTurnRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;

    /**
     * Creates Spark Route for all ReplayPreviousTurn HTTP requests.
     * @param templateEngine The HTML template rendering engine.
     */
    public ReplayPreviousTurnRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine,"templateEngine is required");
        LOG.config("ReplayPreviousTurn is initialized");
        this.gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception{
        LOG.finer("ReplayPreviousTurn is invoked");
        Message message;

        Session session=request.session();

        Player viewer = session.attribute(GameRoute.CURRENT_USER_ATTR);
        Replay replay= viewer.replay;

        if(replay.takeTurnPrevious()) {
            message = Message.info("true");
        }
        else{
            message=Message.error("Previous move does not exist");
        }
        return gson.toJson(message);
    }
}
