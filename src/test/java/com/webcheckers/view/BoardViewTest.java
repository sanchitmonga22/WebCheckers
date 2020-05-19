package com.webcheckers.view;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Spot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UI-tier")
class BoardViewTest {

    private BoardView board;
    private Row row1;
    private Row row3;

    @BeforeEach
    void setUp() {
        List<Row> rows = new ArrayList<>();
        for (int rowIdx = 0; rowIdx < 8; rowIdx++) {
            List<Space> spaces = new ArrayList<>();
            for (int spaceIdx = 0; spaceIdx < 8; spaceIdx++) {
                // Alternate every spot to a different color.
                Spot.Color color;
                if (spaceIdx + rowIdx % 2 == 0) {
                    color = Spot.Color.DARK;
                } else {
                    color = Spot.Color.LIGHT;
                }
                Piece piece = new Piece(Piece.PieceColor.WHITE);
                // Create new space and add it to the row of spaces.
                Space space = new Space(spaceIdx, color, piece, true);
                spaces.add(space);
            }
            Row row = new Row(rowIdx, spaces);
            if (rowIdx == 0) {
                row1 = row;
            }
            else if (rowIdx == 2) {
                row3 = row;
            }
            rows.add(row);
        }
        board = new BoardView(rows);
    }

    @Test
    void iterator() {
        Iterator<Row> iterator = board.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(row1, iterator.next());
        assertNotEquals(row3, iterator.next());
        assertEquals(row3, iterator.next());
    }
}