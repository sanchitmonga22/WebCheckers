package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit testing component for the Player class
 *
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
@Tag("Model-tier")
class PlayerTest {

    final static String USERNAME_ONE = "cjn9414";
    final static String USERNAME_TWO = "ajd1824";
    private Player firstPlayer;
    private Player secondPlayer;

    @BeforeEach
    void setUp() {
        firstPlayer = new Player(USERNAME_ONE);
        secondPlayer = new Player(USERNAME_TWO);
    }

    @Test
    void getUsername() {
        assertEquals(firstPlayer.getUsername(), USERNAME_ONE);
        assertEquals(secondPlayer.getUsername(), USERNAME_TWO);
    }

    @Test
    void fetchWinLoss() {
        assertEquals(-1.0f, firstPlayer.fetchWinLoss());
        assertEquals(-1.0f, secondPlayer.fetchWinLoss());
    }

    @Test
    void testEquals() {
        assertNotEquals(secondPlayer, firstPlayer);
        Player copyPlayer = firstPlayer;
        assertEquals(copyPlayer, firstPlayer);
        assertEquals(firstPlayer, firstPlayer);
    }
}
