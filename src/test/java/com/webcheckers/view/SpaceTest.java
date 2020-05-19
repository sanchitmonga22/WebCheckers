package com.webcheckers.view;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Spot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UI-tier")
class SpaceTest {
    private Piece pieceOne;
    private Piece pieceTwo;
    private Piece pieceThree;
    private Space spaceOne;
    private Space spaceTwo;
    private Space spaceThree;
    private Space spaceFour;

    @BeforeEach
    void setUp() {
        pieceOne = new Piece(Piece.PieceColor.RED);
        pieceTwo = new Piece(Piece.PieceColor.WHITE);
        pieceTwo.setKing();
        pieceThree = null;
        spaceOne = new Space(0, Spot.Color.DARK, pieceOne, false);
        spaceTwo = new Space(4, Spot.Color.LIGHT, pieceTwo, false);
        spaceThree = new Space(3, Spot.Color.DARK, pieceThree, true);
        spaceFour = new Space(2, Spot.Color.LIGHT, null, false);
    }

    @Test
    void isValid() {
        assertTrue(spaceThree.isValid());
        assertFalse(spaceOne.isValid());
        assertFalse(spaceTwo.isValid());
        assertFalse(spaceFour.isValid());
    }

    @Test
    void getIndex() {
        assertEquals(0, spaceOne.getIndex());
        assertEquals(4, spaceTwo.getIndex());
        assertEquals(3, spaceThree.getIndex());
    }

    @Test
    void getPiece() {
        assertEquals(pieceOne, spaceOne.getPiece());
        assertEquals(pieceTwo, spaceTwo.getPiece());
        assertEquals(pieceThree, spaceThree.getPiece());
    }

    @Test
    void moveTo() {
        spaceThree.moveTo(spaceOne.moveFrom());
        assertEquals(pieceOne, spaceThree.getPiece());
    }

    @Test
    void moveFrom() {
        spaceThree.moveTo(spaceOne.moveFrom());
        assertNull(spaceOne.getPiece());
    }
}
