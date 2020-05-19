package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class to test the model component Board.
 */
@Tag("Model-tier")
class BoardTest {

    private Board board;
    private Position originalPosition;
    private Position newPosition;



    /**
     * Creates a new board
     */
    @BeforeEach
    void setup(){
        board = new Board();
        originalPosition = new Position(2,0);
        newPosition = new Position(3,1);
    }

    @Test
    void getPosition() {
        for(int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertNotNull(board.getPosition(new Position(row, col)));
            }
        }
    }

    @Test
    void getPieceAt() {
        Piece piece = new Piece(Piece.PieceColor.RED);
        assertEquals(piece, board.getPieceAt(new Position(1, 1)));
        assertNull(board.getPieceAt(new Position(1,0)));
    }

    @Test
    void getColorAt() {
        assertEquals(Piece.PieceColor.RED, board.getColorAt(new Position(0,0)));
        assertEquals(Piece.PieceColor.WHITE, board.getColorAt(new Position(0,6)));
    }

    @Test
    void updateBoard() {
        Board originalBoard = new Board();
        Move move = new Move(originalPosition, newPosition);
        board.updateBoard(move, false);
        assertEquals(board.getPieceAt(new Position(1,3)), originalBoard.getPieceAt(new Position(0,2)));
        assertEquals(board.getPieceAt(new Position(0,2)), originalBoard.getPieceAt(new Position(1,3)));
        assertNotEquals(board, originalBoard);
        board.updateBoard(move, true);
        assertEquals(board.getPieceAt(new Position(0,2)), originalBoard.getPieceAt(new Position(0,2)));
        assertEquals(board.getPieceAt(new Position(1,3)), originalBoard.getPieceAt(new Position(1,3)));
        assertEquals(board, originalBoard);
    }

    @Test
    void copyAndRotateBoard() {
        Board board2 = board.copyAndRotateBoard();
        assertNotEquals(board, board2);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) { // For each spot on board
                // checks if board space on board matches the proper revered one on board2
                assertEquals(board.getPosition(new Position(col, row)), board2.getPosition(new Position(7 - col, 7 - row)));
            }
        }
    }


    @Test
    void getSpotColorAt() {
        assertEquals(Spot.Color.DARK, board.getSpotColorAt(new Position(0,0)));
        assertNull(board.getColorAt(new Position(0,1)));
    }

    @Test
    void debugPrint() {
        board.debugPrint();
    }

    @Test
    void spotEmpty() {
        assertFalse(board.spotEmpty(originalPosition));
        assertTrue(board.spotEmpty(newPosition));
    }

    @Test
    void testEquals() {
        Board newBoard = new Board();
        assertEquals(board, newBoard);
        board.reverseBoard(board);
        assertNotEquals(board, newBoard);
        assertNotEquals(board, newPosition);
    }
}
