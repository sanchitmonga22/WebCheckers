package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.BoardDraft;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.GetSpectatorGame.CURRENT_USER_ATTR;

public class SpectateCheckTurnRoute implements Route {

    private TemplateEngine templateEngine;
    private static final Logger LOG=Logger.getLogger(CheckTurnRoute.class.getName());
    private Gson gson;
    private boolean gameIsOver = true;

    public SpectateCheckTurnRoute(TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("SpectateCheckTurnRoute is initialized");
        gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("SpectateCheckTurnRoute has been invoked");
        Message message;
        Session session = request.session();

        String gameID = session.attribute(GetSpectatorGame.SPECTATE_GAME_ID);

        Player self = session.attribute(CURRENT_USER_ATTR);

        Game game = GameHelper.activeGames.get(gameID);
        if (session.attribute(SubmitTurnRoute.SUBMIT_ATTR) != null) {
            session.removeAttribute(SubmitTurnRoute.SUBMIT_ATTR);
            message = Message.info("false");
            BoardDraft.spectateBoardUpdate = false;
        }
        else if(BoardDraft.spectateBoardUpdate) {
            message = Message.info("true");
            BoardDraft.spectateBoardUpdate = false;
        }
        else if (game != null && gameIsOver && game.isOver()){
            message = Message.info("true");
            gameIsOver = false;
        }
        else
            message = Message.info("false");
        return gson.toJson(message);
    }
}
