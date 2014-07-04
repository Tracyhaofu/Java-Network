/* Board.java */

package player;
import list.*;

public class Board {
	private Cell[][] current; 
	private int numOfBlack; 
	private int numOfWhite; 

	/**
	 * Helper method: prints out this board
	 */
	protected void printBoard(){
		String[] str = new String[8];
		for (int j = 0 ; j < 8 ; j++){
			str[j]= new String(); 
	  		for (int i = 0 ; i < 8 ; i++){
	  			if (current[i][j].isDead()){
	  				str[j] += "X ";
	  			}
	  			else if(current[i][j].isBlack()){
	  			  str[j] += "B "; 
	  			}
	  		    else if (current[i][j].isWhite()){
	  		    	str[j] += "W "; 
	  		    } 
	  		    else {
	  		    	str[j] += "- "; 
	  		    }
	  		}
	  	}
	  	for (int i = 0 ; i < 8 ; i++){
	  		System.out.println(str[i]); 
	  	} 

	}
	
	/**
	 * a board constructor. Makes an empty 8x8 board; ensures that no chips can be 
	 * placed on the board's corners
	 */
	protected Board(){
		current = new Cell[8][8]; 
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				current[i][j] = new Cell(i,j);
			}
		}
		current[0][0].setDead();
		current[0][7].setDead();
		current[7][0].setDead();
		current[7][7].setDead();
		numOfWhite = 0;
		numOfBlack = 0;
	} 
  
	/**
	 * places a chip on this board; the chip's color and coordinates are determined by the given move
	 * @param move: a given move that instructs where to place or move chip
	 * @param color: a given color for the chip being moved or placed
	 */
	protected void action(Move move, int color) {
		if (move.moveKind == Move.ADD){
			current[move.x1][move.y1].setColor(color);
			if (color == Cell.BLACK){
				numOfBlack++ ; 
			} else if (color == Cell.WHITE){
				numOfWhite++; 
			}
		} else if (move.moveKind == Move.STEP){
			current[move.x2][move.y2].setEmpty();
			current[move.x1][move.y1].setColor(color);  				
		} else if (move.moveKind == Move.QUIT){
			return;
		}
	} 
	
	/**
	 * undoes a previous action which changed this board
	 * @param move: the previous move taken
	 * @param color: the color for the previous move's chip 
	 */
	protected void undo(Move move, int color){
		if (move.moveKind == Move.ADD){
			current[move.x1][move.y1].setEmpty();
			if (color == Cell.BLACK){
				numOfBlack--;
			} else if (color == Cell.WHITE){
				numOfWhite--;
			}
		}
		else if (move.moveKind == Move.STEP){
			current[move.x2][move.y2].setColor(color);
			current[move.x1][move.y1].setEmpty();
		} else if (move.moveKind == Move.QUIT){
			return;
		}
	}

	/**
	 *  Determines whether a given move on this board is valid; returns true if it is, false otherwise 
	 *  @param move: the given move to check
	 *  @param color: the chip's color
	 */
	protected boolean isValidMove(Move move, int color){
		if (color == Cell.BLACK && numOfBlack < 10 && move.moveKind == Move.STEP){
			return false;
		}
		if (color == Cell.WHITE && numOfWhite < 10 && move.moveKind == Move.STEP){
			return false;
		}
		if (color == Cell.BLACK && numOfBlack == 10 && move.moveKind == Move.ADD){
			return false;
		}
		if (color == Cell.WHITE && numOfWhite == 10 && move.moveKind == Move.ADD){
			return false;
		}
		if ((move.x1 < 0) || (move.x1 > 7) || (move.y1 < 0) || (move.y1 > 7)){
			return false;
		}
		if (current[move.x1][move.y1].isDead()) {
			return false;
		}
		if (color == Cell.BLACK) {
			if ((move.x1 == 0)  || (move.x1 == 7)) {
				return false;
			}
		}
		if (color == Cell.WHITE){
			if ((move.y1 == 0) || (move.y1 == 7)) { 
				return false;
			}
		}
		if (current[move.x1][move.y1].isActive()){
			return false;
		}
		if (move.moveKind == Move.STEP){
			if (move.x1 == move.x2 && move.y1 == move.y2){
				return false;
			}
			if (current[move.x2][move.y2].isEmpty()){
				return false;
			}
			if (current[move.x1][move.y1].isDead() || current[move.x2][move.y2].isDead()){
				return false;
			}
			current[move.x2][move.y2].setMoved();
		}

		boolean cluster = hasCluster(move, color);
    
		if (move.moveKind == Move.STEP){
			current[move.x2][move.y2].setColor(color);
		}
		return (!cluster);
	}

    /**
     * Helper method: checks if three or more chips of the same color are in a cluster
     * @param move: the given move to take
     * @param color: the moving/adding chip's color
     * @return: true if there is a cluster; false otherwise 
     */
	private boolean hasCluster(Move move, int color){
		int count = 0;
		int neighborX = 0;
		int neighborY = 0;
 
		for (int i = move.x1-1; i <= move.x1+1; i++){
			for (int j = move.y1-1; j <= move.y1+1; j++){
				if (i == -1){
					i++;
				}
				if (i == 8){
					break;
				}
				if (j == -1){
					j++;
				}
				if (j == 8){
					break;
				}
				if (current[i][j].isColor(color)){
					count++;
					neighborX = i;
					neighborY = j;
				}
			}
		}
    	if (count == 0){
			return false;
		}
    	if (count >= 2){
    		return true;
    	}
    	else if (count >= 1){
    		for (int i = neighborX-1; i <= neighborX+1; i++){
    			for (int j = neighborY-1; j <= neighborY+1; j++){
    				if (i == -1){
    					i++;
    				}
    				if (i == 8){
    					break;
    				}
    				if (j == -1){
    					j++;
    				}
    				if (j == 8){
    					break;
    			}
    				if (current[i][j].isColor(color)){
    					count++;
    				}
    			}
    		}
    	}
    	return (count-1 >= 2);
	}
  
	/**
	 * lists all the possible valid moves for chips of given color on this board
	 * @param color: the color of the chips to check
	 * @return: a list that contains all possible valid moves for chips of given color on this board 
	 */
	protected DList listValidMove(int color){
		DList list = new DList();
		int[] chipX = new int[64];
		int[] chipY = new int[64];
		int arrayIndex = 0;
		
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (current[i][j].isColor(color)){
					chipX[arrayIndex] = i;
					chipY[arrayIndex] = j;
					arrayIndex++;
				}
				Move addMove = new Move(i, j);
				if (isValidMove(addMove, color)){
					list.insertBack(addMove);
				}
			}
		}
		for (int k = arrayIndex; k > 0; k--){
			for (int i = 0; i < 8; i++){
				for (int j = 0; j < 8; j++){
					Move stepMove = new Move(i, j, chipX[k], chipY[k]);
					if (isValidMove(stepMove, color)){
						list.insertBack(stepMove);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Helper method: returns the number of connected chips
	 * @param given: a Cell on this board
	 * @return: an integer that indicates the number of connected chips on this board
	 */
	private int numConnectedChips(Cell given){
		return findChips(given).length();
	} 

	/**
	 * find possiable connected chips horizontally given by the given move
	 * if given chip is in Black goal, then will return length 0 dlist
	 * @param given: a given chip 
	 * @return dlist which contains pissible connect chips
	 */
  
	private DList horizontallyConnect (Cell given){
		DList list = new DList();
		int color = given.getCellKind();
		int opposite = given.opposite();
		int y = given.getY();
		int x = given.getX();
		
		if (!given.isActive()){
			return list;
		}
		if (given.isBlack() && (y == 0 || y == 7)){
			return list; 
		}
		for (int i = x - 1; i >= 0; i--){
			if (current[i][y].isColor(opposite)){
				break; 
			} else if (current[i][y].isColor(color)){
				list.insertBack(current[i][y]);
				break;
			}
		}
		for (int i = x+1; i<=7; i++){
			if (current[i][y].isColor(opposite)){
				break; 
			} else if (current[i][y].isColor(color)){
				list.insertBack(current[i][y]);
				break; 
			}
		}
		return list; 
	}
    
    /**
	 * find possiable connected chips vertically given by the given move
	 * if given chip is in white goal, then will return length 0 dlist
	 * @param given: a given chip 
	 * @return dlist which contains pissible connect chips
	 */
	private DList verticallyConnnect (Cell given){
		DList list = new DList();
		int color = given.getCellKind();
		int opposite = given.opposite();
		int y = given.getY();
		int x = given.getX();
		
		if (!given.isActive()){
			return list;
		}
		if (given.isWhite() && (x==0 || x == 7)){
			return list;
		}
		for (int i = y - 1; i >= 0; i--){
			if (current[x][i].isColor(opposite)){
				break; 
			} else if (current[x][i].isColor(color)){
				list.insertBack(current[x][i]);
				break; 
			}
		}
		for (int i = y+1; i<=7; i++){
			if (current[x][i].isColor(opposite)){
				break; 
			} else if (current[x][i].isColor(color)){
				list.insertBack(current[x][i]);
				break; 
			}
		}
		return list;
	}
    /**
	 * find possiable connected chips leftRightdiagonal given by the given move
	 * @param given: a given chip 
	 * @return dlist which contains pissible connect chips
	 */
	private DList leftRightDiagonalConnect(Cell given){
		DList list = new DList();
		int color = given.getCellKind();
		int opposite = given.opposite();
		int y = given.getY()-1;
		int x = given.getX()-1;
    
		if (!given.isActive()){
			return list;
		}	
		while (x >= 0 && y >= 0){
			if (current[x][y].isColor(opposite)){
				break; 
			} else if (current[x][y].isColor(color)){
				list.insertBack(current[x][y]);
				break; 
			}
			x--; 
			y--; 
		}
		x = given.getX() + 1; 
		y = given.getY() + 1;
		while (x <= 7 && y <= 7){
			if (current[x][y].isColor(opposite)){
				break; 
			} else if (current[x][y].isColor(color)){
				list.insertBack(current[x][y]);
				break; 
			}
			x++; 
			y++; 
		}
		return list;
	}
    /**
	 * find possiable connected chips leftRightdiagonal given by the given move
	 * @param given: a given chip 
	 * @return dlist which contains pissible connect chips
	 */
	private DList rightLeftDiagonalConnect(Cell given){
		DList list = new DList();
		int color = given.getCellKind();
		int opposite = given.opposite();
		int y = given.getY()-1;
		int x = given.getX()+1;
		
		if (!given.isActive()){
			return list;
		}
		while (x <= 7 && y >= 0){
			if (current[x][y].isColor(opposite)){
				break; 
			} else if (current[x][y].isColor(color)){
				list.insertBack(current[x][y]);
				break;
    	}
			x++; 
			y--; 
		} 
		x = given.getX()-1; 
		y = given.getY()+1;
		while (x >= 0 && y <= 7){
			if (current[x][y].isColor(opposite)){
				break; 
			} else if (current[x][y].isColor(color)){
				list.insertBack(current[x][y]);
				break; 
			}
			x--; 
			y++; 
		}
		return list;
	}
	/**
	 * merge two given dlist together. 
	 * @param dlist1: a given list1  
	 * @param dlist1: a given list2
	 * @return dlist which contains merge two lists. 
	 */
	
	private static DList merge(DList list1 , DList list2){	
		try{
			DListNode pointer = ((DListNode)list2.front()); 
			for (int i = 0 ; i < list2.length(); i++){
				list1.insertBack(pointer.item());
				pointer = (DListNode) pointer.next();
			}
			return list1; 
		} catch (InvalidNodeException e){
			return list1; 
		}
	}

	/**
	 * returns true if this board has a winning network for the given color; 
	 * false otherwise
	 * @param color: the given color that is checked for a winning network
	 * @return: true if this board has a winning network for the given color; 
	 * false otherwise
	 */
	protected boolean networkId(int color){
		if (color == Cell.BLACK){
			return blackId(); 
		} else if (color == Cell.WHITE){
			return whiteId(); 
		} else {
			return false; 
		}
	}
	/**
	 * returns true if this board has a winning network for blackId; 
	 * false otherwise
	 * @return: true if this board has a winning network for black; 
	 * false otherwise
	 */

	private boolean blackId(){
		DList network = new DList(); 
		if (numOfBlack < 6) {
			return false; 
		}  
		for (int i =1 ; i < 7 ; i ++){
			if (current[i][0].isBlack()){
				network.insertBack(current[i][0]); 
				if (getConnect(network)){
					return true; 
				} else {
					network = new DList(); 
				}
			} 
		}
		return false; 
	}
    /**
	 * returns true if this board has a winning network for white; 
	 * false otherwise
	 * @return: true if this board has a winning network for white; 
	 * false otherwise
	 */
	private boolean whiteId(){
		DList network = new DList(); 
		if (numOfWhite < 6){
			return false; 
		}  
		for (int i =1; i < 7; i++){
			if (current[0][i].isWhite()){
				network.insertBack(current[0][i]); 
				if (getConnect(network)){
					return true; 
				} else {
					network = new DList(); 
				}
			} 
		}
		return false; 
	}

	/**
	 * recursively to call with a longer dlist.
	 * @param dlist which contains possible chips to form a network 
	 * @return: true if this board has a winning network; 
	 * false otherwise
	 */

	private boolean getConnect(DList sofar){
		try{
			Cell last = ((Cell)sofar.back().item()); 
			if(checkNetwork(sofar)){
				return true; 
			} else if (sofar.length() > 1 && isGoal(last)){
				return false; 
			} else {
				DList possibleConnect = findChips(last); 
				if (possibleConnect.length() == 0) {
					return false; 
				} 
				DListNode pointer = (DListNode)possibleConnect.front(); 
				for (int i = 0; i < possibleConnect.length() ; i++){
					Cell cur = (Cell)pointer.item(); 
					if (!hasCell(sofar, cur)){
						sofar.insertBack(cur);
						if(getConnect(sofar)){
							return true; 
						} else {
							((DListNode)sofar.back()).remove();
						}
					}	
					pointer = (DListNode) pointer.next(); 
				}
				return false; 
			} 
		} catch (InvalidNodeException e){
			return false; 
		}
	}
	/**
	 * @return returns true if  dlist already contains the given chip
	 * false otherwise
	 * @param dlist which contains possible chips to form a network
	 */
	private static boolean hasCell (DList list, Cell next){
		try {
			DListNode pointer = (DListNode) list.front(); 
			for (int i= 0; i < list.length(); i++){
				if(next == pointer.item()){
					return true; 
				}
				pointer = (DListNode) pointer.next(); 
			}
			return false; 
		} catch (InvalidNodeException e){
			return true; 
		}
	}

	/**
	 * check possiable dlist is valid network
	 * @param dlist which contains possible chips to form a network 
	 * @return: true if this board has a winning network; 
	 * false otherwise
	 */
	private static boolean checkNetwork (DList sofar){
		try{
			Cell last = ((Cell)sofar.back().item()); 
			if(sofar.length() < 6){
				return false; 
			} else if ( last.getY() != 7 && last.isBlack()) {
				return false; 
			} else if (last.getX() != 7 && last.isWhite()) {
				return false; 
			} else {
				DListNode pointer = (DListNode) sofar.front(); 
				while(true){
					Cell first = (Cell) pointer.item(); 
					Cell second = (Cell) pointer.next().item(); 
					Cell third = (Cell) pointer.next().next().item(); 
					if (isLine(first, second, third)){
						return false; 
					}
					pointer = (DListNode)pointer.next(); 
				}
			}
		} catch (InvalidNodeException e){ }
		return true; 
	}
    /**
	 * check whether three chips form a line or not
	 * @param three different chips(represented by cells)
	 * @return: true if three chips is in one line; 
	 * false otherwise
	 */
	private static boolean isLine(Cell a, Cell b, Cell c){
		int x1 = a.getX(); 
		int y1 = a.getY(); 
		int x2 = b.getX(); 
		int y2 = b.getY(); 
		int x3 = c.getX(); 
		int y3 = c.getY();
		if (x1*(y2 - y3) + x2*(y3 - y1) + x3*(y1 - y2) == 0){
			return true; 

		} else {
			return false; 
		}  
	}

	/**
	 * returns a list of chips (of the same color) that form connections with a chip
	 * @param chip: the given chip
	 * @return: a list of chips of the same color that form connections with a given chip
	 */
	private DList findChips(Cell chip) {
		DList list1 = horizontallyConnect(chip);
		DList list2 = verticallyConnnect(chip);
		DList list3 = leftRightDiagonalConnect(chip); 
		DList list4 = rightLeftDiagonalConnect(chip); 
		DList list = merge(list1 , merge(list2 , merge(list3,list4)));
		return list; 
	}

	/**
	 * check the given chip is in goal area or not
	 * @param given: the given chip
	 * @return: true if given chip is in goal area. 
	 */

 
	private static boolean isGoal(Cell given){
		int color = given.getCellKind(); 
		int x = given.getX(); 
		int y = given.getY();  
		if(color == Cell.BLACK){
			if (y == 0 || y == 7){
				return true; 
			} else {
				return false; 
			} 
		} else if (color == Cell.WHITE){
			if (x == 0 || x == 0){
				return true; 
			} else {
				return false; 
			}
		} else {
			return false; 
		}
	}
	
	/**
	 * Assign a maximum positive score to a win by
	 * your MachinePlayer, and a minimum negative score to a win by the opponent
	 * @param color: the chip's color of the current turn's player 
	 * @param stepsTaken: the total number of steps that have been taken by the players 
	 * @return: an int--a board's score--an estimation of odds of winning for the board 
	 */
	protected int evaluation(int color, int stepsTaken){
		int score = 0;
		int max = Integer.MAX_VALUE;
		int min = Integer.MIN_VALUE;
		
		if (networkId(color)) {
			if (stepsTaken == 0){
				return max;
			}
			else{
				return (max / stepsTaken);
			}
		}
		if (networkId(oppositeColor(color))){
			if (stepsTaken == 0){
				return min;
			}
			else{
				return (min / stepsTaken);
			}
		}
		for (int i = 0; i <= 7; i++){
			for (int j = 0; j <= 7; j++){
				if (current[i][j].isColor(color)){
					if ((i == 0)  || (i == 7) || (j == 0) || (j == 7)){
						score += numConnectedChips(current[i][j]) * 1.5;
					}
					else{
						score += numConnectedChips(current[i][j]);
					}
				}
				if (current[i][j].isColor(oppositeColor(color))){
					if ((i == 0)  || (i == 7) || (j == 0) || (j == 7)){
						score -= numConnectedChips(current[i][j]) * 1.5;
					}
					else{
						score -= numConnectedChips(current[i][j]);
					}
				}
			}
		}
		return score;
	}
	
	/**
	 * Helper method: returns color BLACK if the given color is WHITE; returns WHITE if given BLACK
	 * @param color: the given color 
	 * @return color BLACK if the given color is WHITE; returns WHITE if given BLACK
	 */
	private int oppositeColor(int color){
		if (color == 0){
			return 1;
		}
		else if (color == 1){
			return 0;
		}
		return 0;
	}
  
  
public static void main(String args[]){
	final int BLACK = 0; 
	final int WHITE = 1; 
	
	Board b1 = new Board();
	Move m1 = new Move(0, 1);
	Move m2 = new Move(2, 1);
	Move m3 = new Move(2, 3);
	Move m4 = new Move(5, 3);
	Move m5 = new Move(2, 6);
	Move m6 = new Move(7, 6);
	Move m7 = new Move(4, 3); 
	//Move m7 = new Move(1, 6);
	//Move m8 = new Move(4, 7);
	//Move m9 = new Move(2,1,3,1);

	//Board b2 = new Board(b1 , m9, 0); 

	b1.action(m1, WHITE);
	b1.action(m2, WHITE);
	b1.action(m3, WHITE);
	b1.action(m4, WHITE);
	b1.action(m5, WHITE);
	b1.action(m6, WHITE);
	//b1.action(m7, WHITE); 
	//b1.action(m7, BLACK);
	//b1.action(m8, BLACK);
    //b1.printBoard();
    //for (int i =0 ; i < 8 ; i++){
    	//for (int j = 0; i< 8 ; i++){
    		//if (b1.current[i][j] == null){
    			//System.out.println(i);
    		//}
    	//}
    //}
    //System.out.println(b1.findChips(b1.current[3][0]));
    //DList list = b1.findChips(b1.current[2][3]); 
    //System.out.println(b1.numOfWhite);
    //System.out.println(b1.numOfBlack);
    //System.out.println(isGoal(b1.current[2][6]));
    // System.out.println(hasCell(list, b1.current[2][7]));
    DList list1 = new DList(); 
    list1. insertBack(b1.current[3][0]);
    list1. insertBack(b1.current[2][1]);
    list1. insertBack(b1.current[2][3]);
    list1. insertBack(b1.current[5][3]);
    list1. insertBack(b1.current[2][6]);
    list1. insertBack(b1.current[5][7]);
    System.out.println(b1.networkId(WHITE));
    System.out.println(checkNetwork(list1));
    //System.out.println(list1);
    //System.out.println(isLine(b1.current[2][1],b1.current[2][3],b1.current[5][3]));

    //System.out.println(list1.length());
    //System.out.println(isLine(b1.current[3][0], b1.current[2][0],b1.current[4][0]));


	Board b2 = new Board();
	Move m21 = new Move(1, 0);
	Move m22 = new Move(2, 1);
	Move m23 = new Move(6, 1);
	//Move m24 = new Move(6, 3);
	Move m25 = new Move(4, 5);
	Move m27 = new Move(6, 7);
	
	b2.action(m21, BLACK);
	b2.action(m22, BLACK);
	b2.action(m23, BLACK);
	//b2.action(m24, BLACK);
	b2.action(m25, BLACK);
	b2.action(m27, BLACK);
	
	Move w1 = new Move(1, 1);
	Move w2 = new Move(1, 3);
	Move w3 = new Move(0, 3);
	Move w4 = new Move(7, 2);
	Move w5 = new Move(2, 5);
	Move w6 = new Move(7, 6);
	
	b2.action(w1, WHITE);
	b2.action(w2, WHITE);
	b2.action(w3, WHITE);
	b2.action(w4, WHITE);
	b2.action(w5, WHITE);
	b2.action(w6, WHITE);
	

	//Test findchips length
	Move t1 = new Move(1, 4);
	b2.action(t1, BLACK);
	List l222 = b2.findChips(b2.current[t1.x1][t1.y1]);
	System.out.println("findchips #: " + l222.length());
	int s1 = b2.evaluation(BLACK, 1);
	System.out.println("b2 score: " + s1);
	
	System.out.println("if hasnetwork: should false: " + b2.networkId(BLACK));
	b2.printBoard();	
	
	

    // problems !!! Board b2 = new Board(b1, m9, BLACK); 
    
  }

}

