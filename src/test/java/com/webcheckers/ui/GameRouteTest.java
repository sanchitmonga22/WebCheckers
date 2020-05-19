package com.webcheckers.ui;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import spark.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
/**
 * Test class to test UI Component GameRoute
 */
@Tag("UI-tier")
class GameRouteTest {

    private TemplateEngine engine;
    private TemplateEngine templateEngine;
    private Session session;
    private Request request;
    private Response response;

    private GameRoute gameroute;

    /**
     * Setting up objects to test
     */
    @BeforeEach
    void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameroute = new GameRoute(engine);

    }

    @Test
    void handle() {
        //TODO
    }

    @Test
    void createBoardView() {
        //TODO
    }
}
