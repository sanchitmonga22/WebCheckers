package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.BoardDraft;
import com.webcheckers.model.Move;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;


/**
 * UI controller for BackupMove
 * @author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 * @author <a href='mailto:cjn9414rit.edu'>Carter Nesbitt</a>
 */
public class BackupMoveRoute implements Route {

    private static final Logger LOG=Logger.getLogger(BackupMoveRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;

    /**
     * Creates Spark Route for all BackUpMove HTTP requests.
     * @param templateEngine The HTML template rendering engine.
     */
    public BackupMoveRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("BackupMove route is initialized");
        gson=new Gson();
    }

    /**
     * Handle the request to revert the move made by the user
     * @param request The HTTP request for the backupMove button.
     * @param response The HTTP response
     * @return The HTML render after handling the request to validate the move.
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("BackupMove is invoked");
        Message message;

        // fetching the gameID
        String gameID = request.queryParams(GameRoute.GAME_ID_ATTR);
        // fetching the moves played by the player from the server by providing the gameID as the key
        Move move = GameHelper.popMove(gameID);

        if(move != null){ // if there is no previous move
            BoardDraft boardDraft = GameHelper.fetchBoard(gameID);
            boardDraft.updateDraft(move, true);
            message = Message.info(move.toString());
            GameHelper.updateBoard(gameID, boardDraft);
            // The message is String in the form "The piece was moved from
            // ROW:'' and COLUMN:'' to ROW:'' and COLUMN:'' "
        }
        else{
            message = Message.error("Previous move does not exist.");
        }
        return gson.toJson(message);
    }
}

