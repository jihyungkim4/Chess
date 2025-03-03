// Julia Gurando and Jihyung Kim
package chess;

import java.util.ArrayList;

public class Chess {

    enum Player { white, black }
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	
     static Board board;

     public static ReturnPlay play(String move) {
        return board.play(move);
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
        board = new Board();
	}
}

