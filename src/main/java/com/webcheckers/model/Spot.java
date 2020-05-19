package com.webcheckers.model;

/**
 * Maintains state for a specific spot within a row of the checkerboard.
 *
 * @author <a href='mailto:csc9411@rit.edu>Christopher Curtice</a>
 * @author <a href='mailto:cjn9414@rit.edu>Carter Nesbitt</a>
 */
public class Spot{
    /**
     * Color color: Is square light or dark
     * Piece piece: Piece (if any) on square
     * Boolean finalRow: Is spot on the last row of board?
     */
    private Color color;
    private Piece piece;

    // stores if it is the final row for a red piece (end row on white's side of board)
    private boolean redfinalRow;

    // stores if it is the final row for a white piece (end row on red's side of the board)
    private boolean whiteFinalRow;

    private Boolean kingFlag;

    // Enum class representing whether a spot is dark (playable) light (non-playable)
    public enum Color {
        LIGHT, DARK
    }

    /**
     * Spot object constructor that collects the data stored in the object.
     * @param color: Color of the spot on the checkerboard.
     * @param piece: Piece occupying the spot on the board.
     * @param redfinalRow: Boolean to represent that the spot is on the
     *                  final row of the checkerboard.
     */
    Spot(Color color, Piece piece, boolean redfinalRow, boolean whiteFinalRow){
        this.color = color;
        this.piece = piece;
        this.redfinalRow = redfinalRow;
        this.whiteFinalRow = whiteFinalRow;
        this.kingFlag = false;
    }


    /**
     * Fetches the piece contained in a spot.
     * @return Piece oject within a spot.
     */
    public Piece getPiece() {
        return this.piece;
    }


    /**
     * Move a given piece (taken using moveFrom()) to this spot
     * @param piece: Piece object being moved into this spot.
     */
    public void moveTo(Piece piece){
        // Move a given piece (taken using moveFrom()) to this spot
        if(isValidMove()) {
            this.piece = piece;
        }

        // checks to see if the piece is on row it needs to reach to be king
        if ((piece.getColor() == Piece.PieceColor.RED) && this.redfinalRow){
            this.kingFlag = true;
        }
        if ((piece.getColor() == Piece.PieceColor.WHITE && this.whiteFinalRow)){
            this.kingFlag = true;
        }
    }


    /**
     * Move a piece from this spot to another spot, by removing the piece before returning it
     * @return Piece object being moved from this spot.
     */
    boolean isValidMove(){
        return this.piece==null && this.color.equals(Color.DARK);
    }


    /**
     * @return The piece that was removed from the spot
     */
    public Piece moveFrom(){
        // Move a piece from this spot to another spot, by removing the piece before returning it
        if(!(this.piece==null)) {
            Piece moved = this.piece;
            this.piece = null;
            return moved;
        }
        return null;
        //TODO: Throw error rather than returning null
    }


    /**
     * Fetches the spot color.
     * @return Spot.Color enumerated object representing the spot color.
     */
    public Color getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Spot) {
            Spot spot = (Spot) obj;
            if (spot.getPiece() == null || this.getPiece() == null) {
                if (spot.getPiece() != this.getPiece()) {
                    return false;
                }
            }
            else {
                if (!spot.getPiece().equals(this.piece)) {
                    return false;
                }
            }
            return (spot.getColor().equals(this.color) && spot.redfinalRow == this.redfinalRow &&
                    spot.whiteFinalRow == this.whiteFinalRow);
        } else {
            return false;
        }
    }
    public boolean isNewKing() {
        boolean isKing = this.kingFlag;
        this.kingFlag = false;
        return isKing;
    }
}
