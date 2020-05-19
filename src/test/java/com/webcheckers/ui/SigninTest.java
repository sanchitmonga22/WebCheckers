package com.webcheckers.ui;

import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
class SigninTest {

    //Mock objects
    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine engine;

    private SigninRoute signInRoute;

    //Example usernames
    private static final String USERNAME = "player 1";
    private static final String USERNAME_2 = "player 2";
    private static final String NON_ALPHA_NUM = "player.1";
    private static final String EMPTY = "";
    private static final String SPACE_ONLY = "    ";

    @BeforeEach
    void setup() {
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        signInRoute = new SigninRoute(engine);
    }

    /**
     * Tests the GET request from the home page that will take you to the sign in form
     */
    @Test
    void get_request_received() {
        //Helper class for testing view model
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        //Mock template engine
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Mock GET request
        when(request.requestMethod()).thenReturn("GET");

        //Handle the signin request
        signInRoute.handle(request, response);

        //Ensure the proper view model is set up correctly
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //Check if we are on the right page
        testHelper.assertViewName(SigninRoute.SIGNIN_FORM_VIEW);

        //Ensure the correct title is displayed
        testHelper.assertViewModelAttribute("title", SigninRoute.SIGNIN_TITLE);
    }

    /**
     * Tests trying to sign in with the same username as someone who is already signed in
     */
    @Test
    void username_already_taken() {
        //Helper class for testing view model
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        //Player lobby containing a single player
        PlayerLobby lobby = new PlayerLobby();
        lobby.playerJoin(new Player(USERNAME));

        WebServer.lobby = lobby;

        //Mock template engine
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Mock POST request
        when(request.requestMethod()).thenReturn("POST");

        //Mock username param from POST request
        when(request.queryParams("userName")).thenReturn(USERNAME);

        //Handle the signin request
        signInRoute.handle(request, response);

        //Ensure the proper view model is set up correctly
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //Check if we are on the correct page
        testHelper.assertViewName(SigninRoute.SIGNIN_FORM_VIEW);

        //Ensure the correct title and error message are displayed
        testHelper.assertViewModelAttribute("title", SigninRoute.SIGNIN_TITLE);
        testHelper.assertViewModelAttribute("message", SigninRoute.USERNAME_TAKEN_ERROR);
    }

    /**
     * Tests trying to sign in with at least one non alpha-numberic character
     */
    @Test
    void username_not_alphanumeric() {
        //Helper class for testing view model
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        //Player lobby containing a single player

        WebServer.lobby = new PlayerLobby();

        //Mock template engine
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Mock POST request
        when(request.requestMethod()).thenReturn("POST");

        //Mock username param from POST request
        when(request.queryParams("userName")).thenReturn(NON_ALPHA_NUM);

        //Handle the signin request
        signInRoute.handle(request, response);

        //Ensure the proper view model is set up correctly
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //Check if we are on the correct page
        testHelper.assertViewName(SigninRoute.SIGNIN_FORM_VIEW);

        //Ensure the correct title and error message are displayed
        testHelper.assertViewModelAttribute("title", SigninRoute.SIGNIN_TITLE);
        testHelper.assertViewModelAttribute("message", SigninRoute.INVALID_USERNAME_ERROR);
    }

    /**
     * Tests trying to sign in with a username that only contains spaces
     */
    @Test
    void username_only_contains_spaces() {
        //Helper class for testing view model
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        WebServer.lobby = new PlayerLobby();

        //Mock template engine
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Mock POST request
        when(request.requestMethod()).thenReturn("POST");

        //Mock username param from POST request
        when(request.queryParams("userName")).thenReturn(SPACE_ONLY);

        //Handle the signin request
        signInRoute.handle(request, response);

        //Ensure the proper view model is set up correctly
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //Check if we are on the correct page
        testHelper.assertViewName(SigninRoute.SIGNIN_FORM_VIEW);

        //Ensure the correct title and error message are displayed
        testHelper.assertViewModelAttribute("title", SigninRoute.SIGNIN_TITLE);
        testHelper.assertViewModelAttribute("message", SigninRoute.INVALID_USERNAME_ERROR);
    }

    /**
     * Tests trying to sign in with a username that is empty
     */
    @Test
    void username_empty() {
        //Helper class for testing view model
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        //Player lobby containing a single player

        WebServer.lobby = new PlayerLobby();

        //Mock template engine
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Mock POST request
        when(request.requestMethod()).thenReturn("POST");

        //Mock username param from POST request
        when(request.queryParams("userName")).thenReturn(EMPTY);

        //Handle the signin request
        signInRoute.handle(request, response);

        //Ensure the proper view model is set up correctly
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //Check if we are on the correct page
        testHelper.assertViewName(SigninRoute.SIGNIN_FORM_VIEW);

        //Ensure the correct title and error message are displayed
        testHelper.assertViewModelAttribute("title", SigninRoute.SIGNIN_TITLE);
        testHelper.assertViewModelAttribute("message", SigninRoute.INVALID_USERNAME_ERROR);
    }

    /**
     * Tests signing in with a valid username
     */
    @Test
    void valid_username() {
        //Helper class for testing view model
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        //Player lobby containing a single player

        WebServer.lobby = new PlayerLobby();

        //Mock template engine
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Mock POST request
        when(request.requestMethod()).thenReturn("POST");

        //Mock session player list retrieval
        when(session.attribute("players")).thenReturn(WebServer.lobby);

        //Mock username param from POST request
        when(request.queryParams("userName")).thenReturn(USERNAME);

        //Handle the signin request
        try {
            signInRoute.handle(request, response);
        } catch(HaltException e) {
            //Expected
        }

        //Mock retrieval of session player object
        when(session.attribute("player")).thenReturn(WebServer.lobby.fetchByUsername(USERNAME));

        //Mock retrieval of session id for login
        when(session.attribute(session.id())).thenReturn(WebServer.lobby.fetchByUsername(USERNAME));

        //Check if we are on the right page
        verify(response).redirect(WebServer.HOME_URL);

    }

}
