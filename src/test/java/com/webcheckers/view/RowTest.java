package com.webcheckers.view;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Spot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Row} component
 *
 * @author <a href='mailto:csc9411@rit.edu'>Christopher Curtice</a>
 */

@Tag("UI-tier")
class RowTest {
    //Test row
    private Row row;
    private Space space1;
    private Space space3;

    @BeforeEach
    void setUp() {

        List<Space> spaces = new ArrayList<>();
        for (int spaceIdx = 0; spaceIdx < 8; spaceIdx++) {
            // Alternate every spot to a different color.
            Spot.Color color;
            if (spaceIdx % 2 == 0) {
                color = Spot.Color.LIGHT;
            }
            else {
                color = Spot.Color.DARK;
            }
            Piece piece = new Piece(Piece.PieceColor.WHITE);

            // Create new space and add it to the row of spaces.
            Space space = new Space(spaceIdx, color, piece, true);
            if (spaceIdx == 0){
                space1 = space;
            }
            else if (spaceIdx == 2) {
                space3 = space;
            }
            spaces.add(space);
        }
        row = new Row(0, spaces);

    }


    @Test
    void getIndex() {
        assertEquals(0, row.getIndex());
        assertNotEquals(3, row.getIndex());
    }

    @Test
    void iterator() {
        Iterator<Space> iterator = row.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(space1, iterator.next());
        assertNotEquals(space3, iterator.next());
        assertEquals(space3, iterator.next());
    }
}