/* Cell.java */ 

package player; 

public class Cell{
	public final static int WHITE = 1; 
	public final static int BLACK = 0; 
	public final static int EMPTY = -1;
	public final static int DEAD = -2;
	public final static int MOVED = -3;

	private int cellKind; 
	private int x; 
	private int y; 

	/**
	 * constructs an empty cell with the given coordinates
	 * @param x: the x coordinate
	 * @param y: the y coordinate
	 */
	protected Cell(int x, int y){
		cellKind = EMPTY; 
		this.x = x;
		this.y = y;
	}

	/**
	 * sets the cell's color with the given color 
	 * cell must not be dead in order to have its color set
	 * @param color: the color to set the cell to; either black (0) or white (1)
	 */
	protected void setColor(int color) {
		if(cellKind != DEAD){
		  cellKind = color;
		}	  	
	}

	/**
	 * sets the cell's kind to dead
	 * the cell must be empty for it to be set dead 
	 */
	protected void setDead() {
		if (cellKind == EMPTY){
			cellKind = DEAD;
		} 
	}
	
	/**
	 * sets the cell's kind to empty
	 * the cell must be active in order to be set to type empty 
	 */
	protected void setEmpty(){
		if (isActive()){
			cellKind = EMPTY; 
		}
	}

	/**
	 * gets the cell's kind
	 * @return: the cell's kind
	 */
	protected int getCellKind(){
		return cellKind;
	}
    
	/**
	 *  return true if given color matches this cell's color; otherwise return false
	 *  if this.color is DEAD or EMPTY return false
	 *  @param color: the given color that is used to check with this cell's color 
	 *  @return: a boolean that indicates if the given color matches this cell's color    
	 */
	protected boolean isColor(int color){
		if (color == WHITE){
			return (cellKind == WHITE);
		}
		else if (color == BLACK){
			return (cellKind == BLACK);
		} 
		else {
			return false; 
		}
	}

	/**
	 * returns true if this cell's color is WHITE; false otherwise
	 * @return returns true if this cell's color is WHITE; false otherwise
	 */
	protected boolean isWhite(){
		return cellKind == WHITE;
	}

	/**
	 * returns true if this cell's color is BLACK; false otherwise
	 * @return returns true if this cell's color is BLACK; false otherwise
	 */
	protected boolean isBlack(){
		return cellKind == BLACK;
	}

	/**
	 * returns true if this cell's kind is DEAD; false otherwise
	 * @return true if this cell's kind is DEAD; false otherwise
	 */
	protected boolean isDead(){
		return cellKind == DEAD; 
	}
	
	/**
	 * returns true if this cell's kind is EMPTY; false otherwise
	 * @return true if this cell's kind is EMPTY; false otherwise 
	 */
	protected boolean isEmpty(){
		return cellKind == EMPTY;
	}
	
	/**
	 * get this cell's x coordinate
	 * @return: this cell's x coordinate
	 */
	protected int getX() {
       return x; 
	}
	
	/**
	 * get this cell's y coordinate
	 * @return this cell's y coordinate
	 */
	protected int getY(){
		return y; 
	}
	
	/**
	 * returns true if this cell is active; false otherwise
	 * @return true if this cell is active; false otherwise
	 */
    protected boolean isActive(){
		return (isWhite() || isBlack());
	}

    /**
     * sets this cell's kind to MOVED
     */
	protected void setMoved(){
		cellKind = MOVED ; 
	}
	
	/**
	 * return this cell's opposite color, given that this cell is active
	 * if this cell's color is WHITE, return BLACK
	 * if this cell's color is BLACK, return WHITE
	 * if this cell is not active, return -10
	 * @return this cell's opposite color, either BLACK or WHITE
	 */
	protected int opposite(){
		if (isActive()){
			if (cellKind == WHITE){
				return BLACK;
			} else {
				return WHITE;
			}
		} else {
			return -10; 
		}
	}

	/**
	 * makes a String representation of this cell for printing
	 */
	public String toString(){
		String str = new String() ; 
		str = "x = " + x + "y = " + y + "cellKind = " + cellKind ; 
		return str; 
	}
	
} 
