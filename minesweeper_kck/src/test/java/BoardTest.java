import org.example.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testBoardInitialization() {
        Board board = new Board(5, 5, 5);
        assertEquals(5, board.getX(), "Board should have 5 rows");
        assertEquals(5, board.getY(), "Board should have 5 columns");

        int bombCount = 0;
        for (int i = 0; i < board.getX(); i++) {
            for (int j = 0; j < board.getY(); j++) {
                if (board.getField(i, j).getValue() == 9) {
                    bombCount++;
                }
            }
        }
        assertEquals(5, bombCount, "Board should have 5 bombs");
    }

    @Test
    void testFlagging() {
        Board board = new Board(5, 5, 0);
        board.flag(2, 2);
        assertTrue(board.getField(2, 2).isFlagged(), "Field (2,2) should be flagged");

        board.unflag(2, 2);
        assertFalse(board.getField(2, 2).isFlagged(), "Field (2,2) should not be flagged");
    }

    @Test
    void testUnfoldField() {
        Board board = new Board(5, 5, 0);
        board.unfoldField(0, 0);
        assertFalse(board.getField(0, 0).isHidden(), "Field (0,0) should be revealed");
    }

    @Test
    void testBombUnfoldEndsGame() {
        Board board = new Board(5, 5, 1);

        // Find the bomb location
        int bombX = -1, bombY = -1;
        for (int i = 0; i < board.getX(); i++) {
            for (int j = 0; j < board.getY(); j++) {
                if (board.getField(i, j).getValue() == 9) {
                    bombX = i;
                    bombY = j;
                }
            }
        }

        // Unfolding a bomb should end the game (return false)
        assertFalse(board.unfoldField(bombX, bombY), "Unfolding a bomb should end the game");
    }

    @Test
    void testAutoUnfoldZero() {
        Board board = new Board(5, 5, 0);
        board.unfoldField(2, 2);

        // All connected zero-value fields should be unfolded
        for (int i = 0; i < board.getX(); i++) {
            for (int j = 0; j < board.getY(); j++) {
                if (board.getField(i, j).getValue() == 0) {
                    assertFalse(board.getField(i, j).isHidden(), "All zero-value fields should be revealed");
                }
            }
        }
    }

    @Test
    void testWinCondition() {
        Board board = new Board(5, 5, 3);

        // Flag all bombs
        int flaggedBombs = 0;
        for (int i = 0; i < board.getX(); i++) {
            for (int j = 0; j < board.getY(); j++) {
                if (board.getField(i, j).getValue() == 9) {
                    board.flag(i, j);
                    flaggedBombs++;
                }
            }
        }

        assertEquals(3, flaggedBombs, "There should be exactly 3 flagged bombs");
        assertTrue(board.checkIfWon(), "The game should be won when all bombs are flagged correctly");
    }

    @Test
    void testInvalidFlagging() {
        Board board = new Board(5, 5, 0);

        // Flag an out-of-bounds field
        assertThrows(IndexOutOfBoundsException.class, () -> board.getField(-1, 0), "Accessing out-of-bounds field should throw exception");
        assertThrows(IndexOutOfBoundsException.class, () -> board.getField(5, 5), "Accessing out-of-bounds field should throw exception");
    }
}
