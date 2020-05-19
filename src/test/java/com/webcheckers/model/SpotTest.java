package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class SpotTest {
    private Piece pieceOne;
    private Piece pieceTwo;
    private Piece pieceThree;
    private Piece pieceFour;
    private Spot spotOne;
    private Spot spotTwo;
    private Spot spotThree;

    @BeforeEach
    void setUp() {
        pieceOne = new Piece(Piece.PieceColor.RED);
        pieceTwo = new Piece(Piece.PieceColor.WHITE);
        pieceTwo.setKing();
        pieceThree = null;
        pieceFour = new Piece(Piece.PieceColor.WHITE);
        spotOne = new Spot(Spot.Color.DARK, pieceOne, false, false);
        spotTwo = new Spot(Spot.Color.LIGHT, pieceTwo, false, false);
        spotThree = new Spot(Spot.Color.DARK, pieceThree, true,false);
    }

    @Test
    void getPiece() {
        assertEquals(pieceOne, spotOne.getPiece());
        assertEquals(pieceTwo, spotTwo.getPiece());
        assertEquals(pieceThree, spotThree.getPiece());
    }

    @Test
    void moveTo() {
        spotThree.moveTo(pieceOne);
        assertNotNull(spotThree.getPiece());
        assertEquals(Piece.Type.KING, spotThree.getPiece().getType());
        Piece removed = spotThree.moveFrom();
        spotThree.moveTo(pieceFour);
        assertNotNull(spotThree.getPiece());
        assertEquals(Piece.Type.SINGLE, spotThree.getPiece().getType());
    }

    @Test
    void isValidMove() {
        assertFalse(spotOne.isValidMove());
        assertFalse(spotTwo.isValidMove());
        assertTrue(spotThree.isValidMove());
        Piece removed = spotTwo.moveFrom();
        assertFalse(spotTwo.isValidMove());
    }

    @Test
    void moveFrom() {
        Piece movedPieceOne = spotOne.moveFrom();
        Piece movedPieceTwo = spotTwo.moveFrom();
        Piece movedPieceThree = spotThree.moveFrom();
        assertEquals(pieceOne, movedPieceOne);
        assertEquals(pieceTwo, movedPieceTwo);
        assertEquals(pieceThree, movedPieceThree);
        assertNull(spotOne.getPiece());
        assertNull(spotTwo.getPiece());
        assertNull(spotThree.getPiece());
    }

    @Test
    void getColor() {
        assertEquals(Spot.Color.DARK, spotOne.getColor());
        assertEquals(Spot.Color.LIGHT, spotTwo.getColor());
        assertEquals(Spot.Color.DARK, spotThree.getColor());
    }
}
