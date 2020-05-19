package com.webcheckers.ui;

import com.webcheckers.model.Player;
import com.webcheckers.util.GameHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import spark.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

/**
 * JUnit testing component for the GetHomeRoute class
 *
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
@Tag("UI-tier")
class GetHomeRouteTest {

	private GetHomeRoute UuT;

	private Request request;
	private Session session;
	private TemplateEngine engine;
	private Response response;
	private GameHelper gameHelper;

	private List<Player> fullLobby;
	private List<Player> emptyLobby;
	private List<Player> testLobby;

	private Player p1, p2, p3, p4;

	private final String TEST_USERNAME1 = "example username1";
	private final String TEST_USERNAME2 = "competitor 1";
	private final String TEST_USERNAME3 = "competitor 2";
	private final String TEST_USERNAME4 = "competitor 3";


	/**
	 * Setting up mock objects.
	 */
	@BeforeEach
	void setup() {

		p1 = new Player(TEST_USERNAME1);
		p2 = new Player(TEST_USERNAME2);
		p3 = new Player(TEST_USERNAME3);
		p4 = new Player(TEST_USERNAME4);

		request = mock(Request.class);
		session = mock(Session.class);
		when(request.session()).thenReturn(session);
		response = mock(Response.class);
		engine = mock(TemplateEngine.class);
		//TODO: Fixes test null pointer crash, not necessarily proper implementation
		gameHelper = new GameHelper();
		UuT = new GetHomeRoute(engine);

		fullLobby = new ArrayList<>();
		fullLobby.add(p1);
		fullLobby.add(p2);
		fullLobby.add(p3);
		fullLobby.add(p4);
		emptyLobby = new ArrayList<>();


	}

	/**
	 * Runs test case of a signed-in user to a full player lobby.
	 */
	@Test
	void signedInTestFullLobby () {
		TemplateEngineTester engineHelper = new TemplateEngineTester();
		request.attribute(GetHomeRoute.USER_ATTR, p1);
		when(WebServer.lobby).thenAnswer(this.getLobbyResponse(fullLobby));
		when(engine.render(any(ModelAndView.class))).thenAnswer(engineHelper.makeAnswer());
		UuT.handle(request, response);
		assertHomeAttributes(engineHelper, fullLobby);
		//TODO: Broken
		assertSignedIn(engineHelper, true);
	}

	/**
	 * Runs test case of player not signed-in,
	 * with a full lobby of players.
	 */
	@Test
	void notSignedInFullLobby() {
		TemplateEngineTester engineHelper = new TemplateEngineTester();
		request.attribute(GetHomeRoute.USER_ATTR, null);
		when(WebServer.lobby).thenAnswer(this.getLobbyResponse(fullLobby));
		when(engine.render(any(ModelAndView.class))).thenAnswer(engineHelper.makeAnswer());
		UuT.handle(request, response);
		assertHomeAttributes(engineHelper, fullLobby);
		assertSignedIn(engineHelper, false);
	}

	/**
	 * Runs test case of player being signed into an empty lobby.
	 */
	@Test
	void signedInTestEmptyLobby() {
		TemplateEngineTester engineHelper = new TemplateEngineTester();
		request.attribute(GetHomeRoute.USER_ATTR, p1);
		when(WebServer.lobby).thenAnswer(this.getLobbyResponse(emptyLobby));
		when(engine.render(any(ModelAndView.class))).thenAnswer(engineHelper.makeAnswer());
		UuT.handle(request, response);
		assertHomeAttributes(engineHelper, emptyLobby);
		//TODO: Broken
		assertSignedIn(engineHelper, true);

	}

	/**
	 * Runs test case of player not being signed in and the
	 * player lobby being empty.
	 */
	@Test
	void notSignedInTestEmptyLobby() {
		TemplateEngineTester engineHelper = new TemplateEngineTester();
		request.attribute(GetHomeRoute.USER_ATTR, null);
		when(WebServer.lobby).thenAnswer(this.getLobbyResponse(emptyLobby));
		when(engine.render(any(ModelAndView.class))).thenAnswer(engineHelper.makeAnswer());
		UuT.handle(request, response);
		assertHomeAttributes(engineHelper, emptyLobby);
		assertSignedIn(engineHelper, false);

	}

	/**
	 * Mock response to fetching the player lobby from the web server.
	 * @param lobby: Lobby that is expected to return.
	 * @return A pre-created PlayerLobby that
	 * 		   can be referenced when running tests.
	 */
	Answer<Object> getLobbyResponse(List<Player> lobby) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				GetHomeRouteTest.this.testLobby = lobby;
				return null;
			}
		};
	}

	/**
	 * Performs assertions with respect to the
	 * view-model of the GetHomeRoute class.
	 * @param engineTester: TemplateEngineTester object used
	 *                      throughout class for testing.
	 * @param lobby: PlayerLobby object expected within the view-model.
	 */
	void assertHomeAttributes(TemplateEngineTester engineTester, List<Player> lobby) {
		engineTester.assertViewModelExists();
		engineTester.assertViewModelIsaMap();
		engineTester.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
		engineTester.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.WELCOME_MSG);
		//TODO: Broken
		engineTester.assertViewModelAttribute(GetHomeRoute.LOBBY_ATTR, fullLobby);
	}

	/**
	 * Performs assertions that check the sign-in status of a user.
	 * @param engineTester: TemplateEngineTester object used
	 *                      throughout class for testing.
	 * @param signedIn: True if player is expected to be signed in,
	 *                	False otherwise.
	 */
	void assertSignedIn(TemplateEngineTester engineTester, boolean signedIn) {
		if (signedIn) {
			engineTester.assertViewModelAttribute(GetHomeRoute.USER_ATTR, p1);
		} else {
			engineTester.assertViewModelAttribute(GetHomeRoute.USER_ATTR, null);
		}
	}

}
