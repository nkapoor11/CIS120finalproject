

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import connect4.Board;
import connect4.Turn;

//import connect4.Board;
//import connect4.View;
//import junit.framework.Assert;

class GameTest {
	Board board;
	
	@BeforeEach
	void before() {
		board = new Board();
	}
	
	@Test
	void testWinner() {		
		assertFalse(board.winnerExists());
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		assertTrue(board.winnerExists());
	}
	
	@Test 
	void testOverflow() {
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1); // reached top
		board.nextTurn(1, 1); // should throw an error.
		assertTrue(board.isOutOfBounds()); // should be true.
	}
	
	@Test 
    void testNoWinner() {
	    assertFalse(board.winnerExists());
        board.nextTurn(1, 1);
        board.nextTurn(1, 1);
        board.nextTurn(2, 1);
        board.nextTurn(3, 1);
        assertFalse(board.winnerExists());
    }
	
	@Test 
    void testEncapsulation() {
	    board.nextTurn(1, 1);
        board.nextTurn(1, 1);
        board.nextTurn(1, 1);
	    Turn prev = board.getPrevious();
        prev.setCol(3);
        prev.setRow(2);
        assertFalse(prev.getCol() == board.getPrevious().getCol());
        assertFalse(prev.getRow() == board.getPrevious().getRow());
    }

	
	@Test
	void testDraw() {
		// fill up entire board except 1 slot.
		int varC = 0;
		for (int r = 0; r < board.getBoard().length; r++) {
			for (int c = 0; c < board.getBoard()[0].length; c++) {		
				if (r%2 == 0) {
					board.nextTurn(c, 1);
				} else {
					board.nextTurn(c, 2);
				}
				
			}
		}		
		assertTrue(board.drawExists());
	}
	
	@Test
	void testEmptyRowPosition() {
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1);
		board.nextTurn(1, 1); // reached top
		board.nextTurn(1, 1); // should return -1.
		assertEquals(-1, board.firstAvailable(1));
	}
}
