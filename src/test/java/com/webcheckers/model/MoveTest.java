package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class MoveTest {

    private Position originalPosition;
    private Position newPosition;
    private Move move;

    @BeforeEach
    void setUp() {
        originalPosition = new Position(2,0);
        newPosition = new Position(3,1);
        move = new Move(originalPosition, newPosition);
    }

    @Test
    void getStart() {
        assertEquals(originalPosition, move.getStart());
    }

    @Test
    void getEnd() {
        assertEquals(newPosition, move.getEnd());
    }

    @Test
    void reverseMove() {
        move.reverseMove();
        assertEquals(newPosition, move.getStart());
        assertEquals(originalPosition, move.getEnd());
    }

    @Test
    void invertMove() {
        move = move.invertMove();
        Position invertedOriginalPosition = new Position(5, 7);
        Position invertedNewPosition = new Position(4, 6);
        assertEquals(invertedOriginalPosition, move.getStart());
        assertEquals(invertedNewPosition, move.getEnd());
    }

    @Test
    void testEquals() {
        Move copiedMove = move;
        copiedMove.invertMove();
        copiedMove.reverseMove();
        copiedMove.invertMove();
        copiedMove.reverseMove();
        assertEquals(move, copiedMove);
    }

    @Test
    void testToString() {
        assertEquals(move.toString(), "The piece was moved from " + originalPosition + " to " + newPosition);
    }
}