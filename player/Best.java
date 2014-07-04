/* Best */
package player;

public class Best {
    private Move move;
    private int score;
    
	/**
	 * get the move
	 * @return the move
	 */
	protected Move getMove() {
		return move;
	}
	
	/**
	 * set the move
	 * @param item: the move to set
	 */
	protected void setMove(Move item) {
		this.move = item;
	}

	/**
	 * get the score
	 * @return the score
	 */
	protected int getScore() {
		return score;
	}

	/**
	 * set the score
	 * @param score the score to set
	 */
	protected void setScore(int score) {
		this.score = score;
	}
}