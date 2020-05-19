package com.webcheckers.ui;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 *Testing the functionality of GetSignOutRoute class
 */
@Tag("UI-tier")
class GetSignoutRouteTest {
    private GetSignoutRoute CuT;
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;

    private final String Username1="Player 1";
    private final String Username2="Player 2";
    private final String Username3="Player 3";
    private List<Player> PlayerLobby;
    private List<Player> EmptyLobby;

    @BeforeEach
    void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response=mock(Response.class);
        engine=mock(TemplateEngine.class);
        CuT=new GetSignoutRoute(engine);
        PlayerLobby = new ArrayList<>();
        // adding players into the lobby to create a mock lobby object from which the player will signOut
        PlayerLobby.add(new Player(Username1));
        PlayerLobby.add(new Player(Username2));
        PlayerLobby.add(new Player(Username3));
        EmptyLobby=null;
    }

    /**
     * Checking if you can sign out of empty lobby
     */
    @Test
    void signOutTestEmptyLobby(){
        TemplateEngineTester engineHelper = new TemplateEngineTester();
        //request.attribute(request.session().id(),);
    }

    @Test
    void handle() {
    }
}
