package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PlayerLobby} component
 *
 * @author <a href='mailto:ajt7593@rit.edu'>Andrew Tarcza</a>
 */
@Tag("Model-tier")
class PlayerLobbyTest {

    //Test players
    private final Player p1 = new Player("player 1");
    private final Player p2 = new Player("player 2");

    //Test lobby
    private PlayerLobby lobby;

    /**
     * Creates a new lobby
     */
    @BeforeEach
    void setup() {
        lobby = new PlayerLobby();
    }

    /**
     * Tests the creation and initialization of a new player lobby
     */
    @Test
    void lobby_creation() {
        //Check if the lobby was created
        assertNotNull(lobby, "The player lobby exists");

        //Check initial size of the lobby
        assertEquals(0, lobby.size(), "The lobby size is correct");
    }

    /**
     * Tests one player joining a player lobby
     */
    @Test
    void player_1_join() {
        //One player joining the lobby
        lobby.playerJoin(p1);

        //Check lobby size
        assertEquals(1, lobby.size(), "The lobby size is correct");

        //Check if the player lists is null
        assertNotNull(lobby.fetchPlayers(), "The player list exists");

        //Check if the player exists in the lobby
        assertTrue(lobby.playerExists(p1), "Player exists in the lobby");

        //Try to find the player by their username
        assertEquals(p1, lobby.fetchByUsername(p1.getUsername()), "Successfully fetched player by username");
    }

    /**
     * Tests two players joining a player lobby
     */
    @Test
    void player_2_join() {
        //Two players joining the lobby
        lobby.playerJoin(p1);
        lobby.playerJoin(p2);

        //Check lobby size
        assertEquals(2, lobby.size(), "The lobby size is correct");

        //Check if the player lists is null
        assertNotNull(lobby.fetchPlayers(), "The player list exists");

        //Check if the player exists in the lobby
        assertTrue(lobby.playerExists(p2), "Player exists in the lobby");

        //Try to find the player by their username
        assertEquals(p2, lobby.fetchByUsername(p2.getUsername()), "Successfully fetched player by username");
    }

    /**
     * Tests the removal of a player from the lobby via a reference to their player object
     */
    @Test
    void remove_via_player_object() {
        //Add 2 players to the lobby
        lobby.playerJoin(p1);
        lobby.playerJoin(p2);

        //Remove player 1 via its player object
        assertTrue(lobby.removePlayer(p1), "Player 1 successfully removed via player object");

        //Check if the player exists in the lobby
        assertFalse(lobby.playerExists(p1), "Lobby no longer contains player 1");

        //Check lobby size
        assertEquals(1, lobby.size(), "The lobby size is correct");

        //Check if the player lists is null
        assertNotNull(lobby.fetchPlayers(), "The player list still exists");

        //Check if the other player still exists in the lobby
        assertTrue(lobby.playerExists(p2), "Player 2 still exists in the lobby");

        //Try to remove player 1 if they aren't in the lobby anymore
        assertFalse(lobby.removePlayer(p1), "Correctly handled trying to remove non-existent player via player object");
    }

    /**
     * Tests the removal of a player from the lobby via their username
     */
    @Test
    void remove_via_username() {
        //Add 2 players to the lobby
        lobby.playerJoin(p1);
        lobby.playerJoin(p2);

        //Remove player 1 via its player object
        assertTrue(lobby.removePlayer(p1.getUsername()), "Player 1 successfully removed via username");

        //Check if the player exists in the lobby
        assertFalse(lobby.playerExists(p1), "Lobby no longer contains player 1");

        //Check lobby size
        assertEquals(1, lobby.size(), "The lobby size is correct");

        //Check if the player lists is null
        assertNotNull(lobby.fetchPlayers(), "The player list still exists");

        //Check if the other player still exists in the lobby
        assertTrue(lobby.playerExists(p2), "Player 2 still exists in the lobby");

        //Try to remove player 1 if they aren't in the lobby anymore
        assertFalse(lobby.removePlayer(p1.getUsername()), "Correctly handled trying to remove non-existent player via username");
    }
}
