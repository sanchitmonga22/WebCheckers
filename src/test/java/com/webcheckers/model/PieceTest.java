package com.webcheckers.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Piece} component
 *
 * @author <a href='mailto:sm3468@rit.edu'>Sanchit Monga</a>
 */
@Tag("Model-tier")
class PieceTest {
    // Creating the Piece color objects
    private final Piece.PieceColor c1= Piece.PieceColor.RED;
    private final Piece.PieceColor c2= Piece.PieceColor.WHITE;
    // Creating the piece object to test it
    private Piece piece;

    /**
     * Creating a new Piece
     */
    @BeforeEach
    void setup(){
        // assigning red color to the piece in the beginning
        piece=new Piece(c1);
    }


    @Test
    void getType() {
        //checking whether the piece has a type
        assertNotNull(piece.getType(), "The piece type can be accessed");
    }

    @Test
    void getColor() {
        // checking whether the color assigned to the piece during the formation is the same
        assertEquals(piece.getColor(), c1, "The color of the piece is as expected");
        assertNotEquals(piece.getColor(), c2, "The color of the piece is as expected");
    }

    @Test
    void setKing() {
        // setting the piece from SINGLE to KING
        piece.setKing();
        // Checking whether the piece was assigned to the type KING
        assertEquals(Piece.Type.KING, piece.getType(),"The piece was successfully set to the KING piece");
    }

    @Test
    void testEquals() {
        Piece pieceTwo = new Piece(c1);
        Piece pieceThree = new Piece(c2);
        assertEquals(pieceTwo, piece);
        assertNotEquals(pieceThree, piece);
        pieceTwo.setKing();
        assertNotEquals(pieceTwo, piece);
        piece.setKing();
        assertEquals(pieceTwo, piece);
    }
}
