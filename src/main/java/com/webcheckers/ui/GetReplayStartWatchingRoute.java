package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import com.webcheckers.util.Replay;
import com.webcheckers.util.ReplayModule;
import com.webcheckers.view.BoardView;
import com.webcheckers.view.Row;
import com.webcheckers.view.Space;
import spark.*;

import java.util.*;

import static com.webcheckers.model.Game.Visitor.REPLAY;
import static com.webcheckers.model.Piece.PieceColor.RED;
import static com.webcheckers.model.Piece.PieceColor.WHITE;

/**
 * The UI Controller to start watching a replay
 *
 * @author <a href='mailto:ajt7593@rit.edu'>Andrew Tarcza</a>
 */
public class GetReplayStartWatchingRoute implements Route {

    private static final int BOARD_WIDTH = 8;
    private Gson gson;
    public static final String MODE_OPTIONS_ATTR = "modeOptionsAsJSON";
    public static final String GAME_REPLAY_KEY_ATTR="replay-match";
    public static final String REPLAY_NEXT_ATTR="hasNext";
    public static final String REPLAY_PREVIOUS_ATTR="hasPrevious";
    public static final String REPLAY_BOARD_VIEW="boardView";

    private final TemplateEngine templateEngine;

    public GetReplayStartWatchingRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //Replay key in the form '[p2] challenged by [p1]'
        String replayKey = request.queryParams(GAME_REPLAY_KEY_ATTR).replace("+", " ");

        // putting the replay key in the session
        Session session=request.session();
        session.attribute(GAME_REPLAY_KEY_ATTR,replayKey);
        //Split the replay key into tokens based on spaces
        String tokens[] = replayKey.split(" ");

        //First token is the white player and last token is the red player
        String whiteUsername = tokens[0];
        String redUsername = tokens[tokens.length - 1];

        Player redPlayer;
        Player whitePlayer;

        Map<String, Object> vm = new HashMap<>();

        Player currentUser = request.session().attribute(GameRoute.CURRENT_USER_ATTR);

        currentUser.inReplay = true;

        Replay replay;

        if (currentUser.replay == null) {
            replay = new Replay(ReplayModule.getReplayGame(replayKey));
        } else {
            replay = currentUser.replay;
        }

        currentUser.replay = replay;


        Board replayBoard = replay.getBoard();


        //Remove the player from the lobby
        WebServer.lobby.removePlayer(currentUser);
        BoardView boardView;
        //Check if current user was the red or the white player
        if (redUsername.equals(currentUser.getUsername())) {
            redPlayer = currentUser;
            boardView = createBoardView(replayBoard, RED, replayKey);
            vm.put(GameRoute.BOARD_ATTR, boardView);

            //See if the white player still exists in the lobby
            whitePlayer = WebServer.lobby.fetchByUsername(whiteUsername);

            if (whitePlayer == null)
                whitePlayer = new Player(whiteUsername);
        } else {
            whitePlayer = currentUser;
            boardView = createBoardView(replayBoard, WHITE, replayKey);
            vm.put(GameRoute.BOARD_ATTR, boardView);

            redPlayer = WebServer.lobby.fetchByUsername(redUsername);

            //See if the redPlayer still exists in the lobby
            if (redPlayer == null)
                redPlayer = new Player(redUsername);
        }
        session.attribute(REPLAY_BOARD_VIEW,boardView);
        //Add initial attributes
        vm.put(GameRoute.TITLE_ATTR, GameRoute.TITLE);
        vm.put(GameRoute.CURRENT_USER_ATTR, request.session().attribute(GameRoute.CURRENT_USER_ATTR));
        vm.put(GameRoute.VIEW_MODE_ATTR, REPLAY);
        vm.put(GameRoute.RED_PLAYER_ATTR, redPlayer);
        vm.put(GameRoute.WHITE_PLAYER_ATTR, whitePlayer);
        vm.put(GameRoute.ACTIVE_COLOR_ATTR, RED);

        Map<String, Object> modeOptions = new HashMap<>();

        modeOptions.put(REPLAY_NEXT_ATTR, !replay.reachedEnd());
        modeOptions.put(REPLAY_PREVIOUS_ATTR, !replay.atStart());
        vm.put(MODE_OPTIONS_ATTR, gson.toJson(modeOptions));

        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }

    /**
     * Helper function to generate a BoardView object.
     * @return A new BoardView object.
     */
    public static BoardView createBoardView(Board board, Piece.PieceColor playerColor, String replayKey) {
        List<Row> boardRows = new ArrayList<>();

        if(playerColor == RED)
            board = board.copyAndRotateBoard();

        for (int rowIdx = 0; rowIdx < BOARD_WIDTH; rowIdx++) {
            List<Space> spaces = new ArrayList<>();
            for (int spaceIdx = 0; spaceIdx < BOARD_WIDTH; spaceIdx++) {
                // Alternate every spot to a different color.
                Spot.Color color = board.getSpotColorAt(new Position(spaceIdx, rowIdx));

                Piece piece = board.getPieceAt(new Position(spaceIdx, rowIdx));

                // Create new space and add it to the row of spaces.
                Space space = new Space(spaceIdx, color, piece, rowIdx%7 == 0);
                spaces.add(space);
            }
            // Create a new row and add it to the list of rows (board).
            Row row = new Row(rowIdx, spaces);
            boardRows.add(row);
        }
        return new BoardView(boardRows);
    }

}
