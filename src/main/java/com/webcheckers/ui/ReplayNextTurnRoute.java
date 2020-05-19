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
 * UI Controller for Replay Mode that will update the board with the next turn
 * @author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 */
public class ReplayNextTurnRoute implements Route {
    private static final Logger LOG=Logger.getLogger(ReplayNextTurnRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;

    /**
     * Creates Spark Route for all ReplayNextTurn HTTP requests.
     * @param templateEngine The HTML template rendering engine.
     */
    public ReplayNextTurnRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine,"templateEngine is required");
        LOG.config("ReplayNextTurn is initialized");
        this.gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response response){
        LOG.finer("ReplayNextTurn is invoked");

        // taking the key from the session
        Session session=request.session();

        Player viewer = session.attribute(GameRoute.CURRENT_USER_ATTR);

        Replay replay= viewer.replay; // has the replay object with all the turns
        Message message;

        if(replay.takeTurnNext()) {
            message=Message.info("true");
        }else{
            message=Message.error("Next move is not available");
        }
        return gson.toJson(message);
    }

}
