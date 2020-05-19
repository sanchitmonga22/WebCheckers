package com.webcheckers.ui;

import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Board;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;
import com.webcheckers.util.GameHelper;
import com.webcheckers.util.Message;
import spark.*;
import spark.utils.StringUtils;

import static com.webcheckers.model.Game.Visitor.PLAY;

/**
 * The UI Controller for both GET/POST for the Signin page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author csc9411@rit.edu Christopher Curtice
 */
public class SigninRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SigninRoute.class.getName());

    private final TemplateEngine templateEngine;

    private Player player;

    //View-model attributes
    static final String SIGNIN_TITLE = "Sign In";
    static final String SIGNED_IN_QUESTION = "Signed In?";
    static final String SIGNIN_FORM_VIEW = "signin.ftl";
    static final Message INVALID_USERNAME_ERROR = Message.error("Invalid username");
    static final Message USERNAME_TAKEN_ERROR = Message.error("Username already taken");

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public SigninRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("SigninRoute is initialized.");
    }

    /**
     * Render the WebCheckers Signin page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Signin page
     */
    @Override
    public Object handle(Request request, Response response) {
        // Check whether request is GET or POST, and invoke appropriate handler
        if(request.requestMethod().equals("GET")){
            LOG.finer("GetSigninRoute is invoked.");

            // Initialize view
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", SIGNIN_TITLE);
            // render the View
            return templateEngine.render(new ModelAndView(vm , SIGNIN_FORM_VIEW));
        }
        else {
            LOG.finer("PostSigninRoute is invoked.");

            Session session = request.session();

            // Get username input and create a new Player object.
            String username = request.queryParams("userName");
            player = new Player(username);

            // Initialize View
            Map<String, Object> vm;
            //Check if username is empty, contains only blank spaces, or is not alphanumeric
            if(StringUtils.isBlank(username) || username.matches("^.*[^a-zA-Z0-9 ].*$")) {
                // Initialize view
                vm = new HashMap<>();

                vm.put("message", INVALID_USERNAME_ERROR);

                vm.put("title", SIGNIN_TITLE);
                // render the View
                return templateEngine.render(new ModelAndView(vm , SIGNIN_FORM_VIEW));
            }
            //Check if the username is taken
            else if(WebServer.reservedUsernames.contains(player.getUsername())) {
                //Initialize view
                vm = new HashMap<>();

                //Create the error message
                //Message error = Message.error("Username already taken");
                vm.put("message", USERNAME_TAKEN_ERROR);

                vm.put("title", SIGNIN_TITLE);
                // render the View
                return templateEngine.render(new ModelAndView(vm , SIGNIN_FORM_VIEW));
            }
            else {
                PlayerLobby players = WebServer.lobby;
                player = WebServer.allPlayers.get(username);
                if (player == null) {
                    player = new Player(username);
                    players.playerJoin(player);
                    WebServer.allPlayers.put(username, player);
                }
                session.attribute("player", player);
                // Initialize View
                vm = new HashMap<>();


                // Update list of players in session.
                vm.put("title", SIGNED_IN_QUESTION);

                session.attribute("players", players);

                //Add the username to the list of reserved usernames
                WebServer.reservedUsernames.add(player.getUsername());

                // Fetch username from session.
                session.attribute(GameRoute.CURRENT_USER_ATTR, player);

                // Redirect user to the home page.
                response.redirect(WebServer.HOME_URL);
            }
            Spark.halt();
            return null;
        }
    }
}