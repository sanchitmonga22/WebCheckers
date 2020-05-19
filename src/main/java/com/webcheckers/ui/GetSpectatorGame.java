package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.webcheckers.model.Game.Visitor.SPECTATOR;
import static com.webcheckers.model.Piece.PieceColor.RED;
import static com.webcheckers.model.Piece.PieceColor.WHITE;

public class GetSpectatorGame implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorGame.class.getName());
    //Public attributes that are called, easy to locate and use
    private final TemplateEngine templateEngine;
    public static final String TITLE = "Welcome to the game.";
    public static final String RED_PLAYER_ATTR = "redPlayer";
    public static final String WHITE_PLAYER_ATTR = "whitePlayer";
    public static final String ACTIVE_COLOR_ATTR = "activeColor";
    public static final String TITLE_ATTR = "title";
    public static final String VIEW_MODE_ATTR = "viewMode";
    public static final String CURRENT_USER_ATTR = "currentUser";
    public static final String SPECTATOR_ATTR = "spectate-match";
    public static final String GAME_ID_ATTR = "gameID";
    public static final String BOARD_ATTR = "board";
    public static final String SPECTATE_GAME_ID = "spectateGameID";
    private static final String GAME_OVER_MSG_ATTR = "gameOverMessage";
    private static final String MODE_OPTIONS_ATTR = "modeOptionsAsJSON";
    private static final String GAME_OVER_ATTR = "isGameOver";
    private Gson gson;

    /**
     * Constructor
     * @param templateEngine Engine used to display the game
     */
    public GetSpectatorGame(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;

        LOG.config("Spectator Game is initialized");

        gson = new Gson();
    }

    /**
     * Handle method for the get spectator route
     * @param request Handle request
     * @param response Handle response
     * @return The rendering of the spectator
     * @throws Exception Shouldn't be thrown
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("Get spectator was invoked");
        //vm to represent the board, and current session of the user
        Map<String, Object> vm = new HashMap<>();
        Session session = request.session();
        //Checks the request method
        if (request.requestMethod().equals("GET")){
            LOG.finer("Spectator game was invoked");

            //Gets the spectator and removes them from the player lobby
            Player self = session.attribute(CURRENT_USER_ATTR);
            self.spectating = true;
            WebServer.lobby.removePlayer(self);

            //Puts attributes in the vm
            vm.put(TITLE_ATTR, TITLE);
            vm.put(CURRENT_USER_ATTR, self);
            vm.put(VIEW_MODE_ATTR, SPECTATOR);

            //Gets the two names of the players you are spectating
            String twoNames = request.queryParams(SPECTATOR_ATTR);
            Player player1 = WebServer.allPlayers.get(twoNames.split(" challenged by ")[0]);
            Player player2 = WebServer.allPlayers.get(twoNames.split(" challenged by ")[1]);

            //Gets the game id and game of the spectated game
            String gameID = String.valueOf(GameHelper.getGameID(player1));
            Game spectatedGame = GameHelper.activeGames.get(gameID);

            if (spectatedGame == null) {
                response.redirect(WebServer.SPECTATE_LEAVE_URL);
                return null;
            }

            vm.put(GAME_ID_ATTR, gameID);
            vm.put(RED_PLAYER_ATTR, player2);
            vm.put(WHITE_PLAYER_ATTR, player1);
            session.attribute(SPECTATE_GAME_ID, gameID);


            //Checks who's turn it is
            if (spectatedGame.isMyTurn(player2)) {
                vm.put(ACTIVE_COLOR_ATTR, RED);
            } else {
                vm.put(ACTIVE_COLOR_ATTR, WHITE);
            }
            //Display for the board
            vm.put(BOARD_ATTR, GameRoute.createBoardView(RED, gameID, false));
            if(spectatedGame.isOver()){
                final Map<String, Object> modeOptions = new HashMap<>(2);

                Message message = Message.info("Game is over");

                modeOptions.put(GAME_OVER_MSG_ATTR, message.getText());
                modeOptions.put(GAME_OVER_ATTR, spectatedGame.isOver());

                vm.put(MODE_OPTIONS_ATTR, gson.toJson(modeOptions));
            }


        }
        //Returns the rendering for the board
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
