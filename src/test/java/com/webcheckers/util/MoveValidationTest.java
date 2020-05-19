package com.webcheckers.util;

import com.webcheckers.model.*;
import static com.webcheckers.model.Piece.PieceColor.RED;
import static com.webcheckers.model.Piece.PieceColor.WHITE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class MoveValidationTest {

    private Board board;
    private Position originalPosition;
    private Position newPosition;
    private Position jumper;
    private Position jumped;
    private Position hide;
    private Position whitePosition;
    private Move stationaryMove;
    private Move redMove;
    private Move whiteMove;
    private Move redMoveReverse;
    private Move whiteMoveReverse;
    private Move jump;
    private Move hideRed;
    private Move serveWhite;
    private Player firstPlayer;
    private Player secondPlayer;
    private Game game;
    private GameHelper gameHelper;
    final static private String USERNAME_ONE = "cjn9414";
    final static private String USERNAME_TWO = "ajd1824";

    @BeforeEach
    void setUp() {
        board = new Board();
        originalPosition = new Position(2,0);
        newPosition = new Position(3,1);
        jumper = new Position(1, 3);
        jumped = new Position(2, 2);
        hide = new Position(3,3);
        whitePosition = new Position(7,5);
        hideRed = new Move(jumped, hide);
        serveWhite = new Move(whitePosition, jumped);
        stationaryMove = new Move(originalPosition, originalPosition);
        redMove = new Move(originalPosition, newPosition);
        whiteMove = redMove.invertMove();
        redMoveReverse = new Move(originalPosition, newPosition);
        redMoveReverse.reverseMove();
        whiteMoveReverse = redMoveReverse.invertMove();
        jump = new Move(jumper, newPosition);
        firstPlayer = new Player(USERNAME_ONE);
        secondPlayer = new Player(USERNAME_TWO);
        game = new Game(firstPlayer, secondPlayer);
        gameHelper = new GameHelper();
        GameHelper.addMatch(secondPlayer, firstPlayer);
        GameHelper.getGameID(firstPlayer);
    }

    @Test
    void checkStandardTypeSingle() {
        assertFalse(MoveValidation.checkStandardTypeKing(board,stationaryMove, "0"));
        assertTrue(MoveValidation.checkStandardTypeSingle(board, redMove, "0"));
        assertFalse(MoveValidation.checkStandardTypeSingle(board, whiteMove, "0"));
        Game.changeTurn();
        assertTrue(MoveValidation.checkStandardTypeSingle(board, whiteMove, "0"));
        Game.changeTurn();
        assertFalse(MoveValidation.checkStandardTypeSingle(board, redMoveReverse, "0"));
        Game.changeTurn();
        assertFalse(MoveValidation.checkStandardTypeSingle(board, whiteMoveReverse, "0"));
    }

    @Test
    void checkStandardTypeKing() {
        assertFalse(MoveValidation.checkStandardTypeKing(board, stationaryMove, "0"));
        assertTrue(MoveValidation.checkStandardTypeKing(board, redMove, "0"));
        Game.changeTurn();
        assertTrue(MoveValidation.checkStandardTypeKing(board, whiteMove, "0"));
        Game.changeTurn();
        assertTrue(MoveValidation.checkStandardTypeKing(board, redMoveReverse, "0"));
        Game.changeTurn();
        assertTrue(MoveValidation.checkStandardTypeKing(board, whiteMoveReverse, "0"));
    }

    @Test
    void checkJump() {
        assertFalse(MoveValidation.checkJump(board, jump));
        board.updateBoard(hideRed, false);
        assertFalse(MoveValidation.checkJump(board, jump));
        board.updateBoard(serveWhite, false);
        assertTrue(MoveValidation.checkJump(board, jump));
        board.updateBoard(jump, false);
        jump.reverseMove();
        assertTrue(MoveValidation.checkJump(board, jump));
        //TODO: Make this false?
    }

    @Test
    void checkMultiTypeKing() {
        assertFalse(MoveValidation.checkMultiTypeKing(board, jump));
        board.updateBoard(hideRed, false);
        assertFalse(MoveValidation.checkMultiTypeKing(board, jump));
        board.updateBoard(serveWhite, false);
        assertTrue(MoveValidation.checkMultiTypeKing(board, jump));
        board.updateBoard(jump, false);
        jump.reverseMove();
        assertTrue(MoveValidation.checkMultiTypeKing(board, jump));
    }

    @Test
    void noMoreJumpsStandard() {
        assertTrue(MoveValidation.noMoreJumpsStandard(board, jumper));
        board.updateBoard(hideRed, false);
        assertTrue(MoveValidation.noMoreJumpsStandard(board, jumper));
        board.updateBoard(serveWhite, false);
        assertFalse(MoveValidation.noMoreJumpsStandard(board, jumper));
        board.updateBoard(jump, false);
        assertTrue(MoveValidation.noMoreJumpsStandard(board, newPosition));
    }

    @Test
    void noMoreJumpsKing() {
    }

    @Test
    void determineMoveType() {
    }

    @Test
    void hasJumpsAvailable() {
    }

    @Test
    void hadNoJump() {
    }

    @Test
    void submitTurn() {
    }

    @Test
    void jumpAvailability() {
    }
}