package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class GameTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Tester");
        player2 = new Player("User");
        new Game(player1, player2);
    }

    @Test
    void numberOfPieces() {
        assertEquals(12, Game.numberOfPieces(Piece.PieceColor.RED));
        assertEquals(12, Game.numberOfPieces(Piece.PieceColor.WHITE));
    }

    @Test
    void changeTurn() {
        assertEquals(Piece.PieceColor.RED, Game.getActiveColor());
        assertTrue(Game.isMyTurn(player1));
        Game.changeTurn();
        assertEquals(Piece.PieceColor.WHITE, Game.getActiveColor());
        assertTrue(Game.isMyTurn(player2));
        Game.changeTurn();
        assertTrue(Game.isMyTurn(player1));
        assertEquals(Piece.PieceColor.RED, Game.getActiveColor());
    }

    @Test
    void isMyTurn() {
        assertTrue(Game.isMyTurn(player1));
        assertFalse(Game.isMyTurn(player2));
        Game.changeTurn();
        assertFalse(Game.isMyTurn(player1));
        assertTrue(Game.isMyTurn(player2));
    }

    @Test
    void getActiveColor() {
        assertEquals(Piece.PieceColor.RED, Game.getActiveColor());
        assertNotEquals(Piece.PieceColor.WHITE, Game.getActiveColor());
        Game.changeTurn();
        assertEquals(Piece.PieceColor.WHITE, Game.getActiveColor());
        assertNotEquals(Piece.PieceColor.RED, Game.getActiveColor());
    }

    @Test
    void isOver() {
        while (Game.numberOfPieces(Piece.PieceColor.RED) > 0) {
            assertFalse(Game.isOver());
            Game.decrementPieces(Piece.PieceColor.RED);
        }
        assertTrue(Game.isOver());
    }

    @Test
    void decrementPieces() {
        Game.decrementPieces(Piece.PieceColor.RED);
        Game.decrementPieces(Piece.PieceColor.WHITE);
        assertNotEquals(12, Game.numberOfPieces(Piece.PieceColor.RED));
        assertNotEquals(12, Game.numberOfPieces(Piece.PieceColor.WHITE));
        Game.decrementPieces(Piece.PieceColor.RED);
        Game.decrementPieces(Piece.PieceColor.WHITE);
        Game.decrementPieces(Piece.PieceColor.WHITE);
        assertEquals(10, Game.numberOfPieces(Piece.PieceColor.RED));
        assertEquals(9, Game.numberOfPieces(Piece.PieceColor.WHITE));
    }
}