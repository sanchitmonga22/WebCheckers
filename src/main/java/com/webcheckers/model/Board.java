package com.webcheckers.model;

/**
 * This Class is to represent the board being played on by the two players.
 * The board has two different sets of pieces on different parts of the board
 * and the board being played on.
 *
 * @author <a href='mailto:cjn9414@rit.edu'>Carter Nesbitt</a>
 */
public class Board {

    //2D array representing the board
    private Spot[][] board;


    /**
     * Constructs a new Board object by instantiating a standard checkers
     * board from the point of view of the player
     * in control of the white pieces.
     */
    public Board(){
        //set up board
        board = new Spot[8][8];
        // stores if the spot is a final spot for the red piece to reach to be king
        boolean redsFinal;

        // stores if the spot is a final spot for the white piece to reach to be king
        boolean whitesFinal;
        Spot.Color color;
        Piece piece;
        for(int row=0; row<board.length; row++){
            redsFinal = false;
            whitesFinal = false;
            for(int col=0; col<board[0].length; col++){
                if ((row+col)%2==1) {
                    color = Spot.Color.LIGHT;
                    piece = null;
                }
                else {
                    color = Spot.Color.DARK;
                    if (row<3){
                        piece = new Piece(Piece.PieceColor.RED); //Red
                    }
                    else if (row>4){
                        piece = new Piece(Piece.PieceColor.WHITE); //White
                    }
                    else {
                        piece = null;
                    }
                }


                // checks rows to see if they are the corresponding final rows
                if (row == 7){
                    redsFinal = true;
                }
                if (row == 0 ){
                    whitesFinal = true;
                }
                board[col][row] = new Spot(color, piece, redsFinal ,whitesFinal);
            }
        }
    }


    /**
     * Get what is in the spot on the board
     * @param pos: Position to fetch spot on the board.
     * @return Space object contained within the spot.
     */
    public Spot getPosition(Position pos){
        return board[pos.getCell()][pos.getRow()];
    }

    /**
     * Fetches the piece contained at the provided row and column.
     * Prerequisite: Piece exists at row and column specified.
     * @param pos Position in which the piece is contained.
     * @return Piece contained within the row and column.
     */
    public Piece getPieceAt(Position pos) {
        return board[pos.getCell()][pos.getRow()].getPiece();
    }


    /**
     * Fetches the color of the piece contained within a space.
     * Prerequisite: A piece must exist within the spot.
     * @param pos Position in which the piece is contained.
     * @return Color of the piece contained within the spot.
     */

    public Piece.PieceColor getColorAt(Position pos) {
        return getPieceAt(pos).getColor();
    }


    /**
     * Reverts a move in the model.
     * @param move Move that represent the move made by the user
     *             that is currently being reverted.
     */
    public void updateBoard(Move move, boolean reverse){
        int xStart, yStart, xEnd, yEnd;
        if (reverse) {
           move = move.reverseMove();
        }
        xStart = move.getStart().getCell();
        yStart = move.getStart().getRow();
        xEnd = move.getEnd().getCell();
        yEnd = move.getEnd().getRow();
        this.board[xEnd][yEnd].moveTo(this.board[xStart][yStart].moveFrom());
    }

  /**
     * Function that will return an identical board to the reference
     * board, except with an effective 180 degree rotation.
     * @return A new Board object that is rotated
     *         180 degrees from the reference.
     */
    public Board copyAndRotateBoard() {
        Board rotatedBoard = new Board();
        this.reverseBoard(rotatedBoard);
        return rotatedBoard;
    }


    /**
     * Copies the 2-dimensional Space array from one board
     * model and instantiates it onto another.
     * @param rotated: Board that is to have the rotated
     *                 referenced board instantiated onto it.
     */
    void reverseBoard(Board rotated) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                rotated.board[7 - col][7 - row] = this.board[col][row];
            }
        }
    }


    /**
     * Obtains the spot color contained at a
     * specific row and column of the baord.
     * @param pos Position on the checkers board.
     * @return Spot.Color object representing color of the spot.
     */
    public Spot.Color getSpotColorAt(Position pos) {
        return board[pos.getCell()][pos.getRow()].getColor();
    }


    /**
     * Developer function used to visualize board model via the terminal window.
     */
    public void debugPrint() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j <= 7; j++) {
                Piece piece = board[j][i].getPiece();
                if (piece == null) {
                    System.out.print("[ ] ");
                } else {
                    System.out.print((piece.getColor() == Piece.PieceColor.RED) ? "[R] " : "[W] ");
                }
            }
            System.out.println();
        }
    }


	/**
	 * Determines if a spot on the game board is empty.
	 * @param position Position being checked for occupancy.
	 * @return True if given spot is empty
	 * 		   False otherwise.
	 */
	public boolean spotEmpty(Position position) {
        Spot spot = board[position.getCell()][position.getRow()];
        return spot.getPiece() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Board) {
            Board board = (Board) obj;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (!board.getPosition(new Position(col, row)).equals
                            (this.getPosition(new Position(col, row)))) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
