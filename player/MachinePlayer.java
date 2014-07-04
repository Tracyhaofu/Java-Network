/* MachinePlayer.java */

package player;
import list.DList;
import list.DListNode;
import list.InvalidNodeException;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
	private Board current;
	private int color; 
	private int searchDepth;
	public final static boolean MACHINE = true;
	public final static boolean OPPONENT = false;

	/**
	 * Helper method: if this MachinePlayer color is black, return white; otherwise,
	 * return black
	 * @return int: an integer that represents the opposite color: 1 if this
	 * MachinePlayer's color is 0; 0 if this MachinePlayer's color is 2
	 */
	private int oppositeColor(){
		if (color == 0){
			return 1;
		}
		else{
			return 0;
		}
	}
  
	/**
	 * Creates a machine player with the given color.  Color is either 0 (black)
	 * or 1 (white).  (White has the first move.)
	 * @param color: the given color for this MachinePlayer; either 0 or 1
	 */
	public MachinePlayer(int color) {
		this(color, 3);
	}

	/**
	 *  Creates a machine player with the given color and search depth.  Color is
	 *  either 0 (black) or 1 (white).  (White has the first move.)
	 * @param color: the given color for this MachinePlayer; either 0 or 1
	 * @param searchDepth: the max depth that the minimax method will search into 
	 */
	public MachinePlayer(int color, int searchDepth) {
		this.color = color;
		this.searchDepth = searchDepth;
		current = new Board();
	}

	/**
	 *  Returns a new move by "this" player.  Internally records the move (updates
	 *  the internal game board) as a move by "this" player.
	 */
	public Move chooseMove() {
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Best myBest = minimax(MACHINE, alpha, beta, searchDepth);
		Move bestMove = myBest.getMove();
		current.action(bestMove, color);
		return bestMove;
	} 

	/** 
	 * If the Move m is legal, records the move as a move by the opponent
	 * (updates the internal game board) and returns true.  If the move is
	 * illegal, returns false without modifying the internal state of "this"
	 * player.  This method allows your opponents to inform you of their moves.
	 * @param m: a given move that the opponent takes
	 */
	public boolean opponentMove(Move m) {
		if (current.isValidMove(m, oppositeColor())){
			current.action(m, oppositeColor());
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 *  If the Move m is legal, records the move as a move by "this" player
	 *  (updates the internal game board) and returns true.  If the move is
	 *  illegal, returns false without modifying the internal state of "this"
	 *  player.  This method is used to help set up "Network problems" for your
	 *  player to solve.
	 *  @param m: a given move that this MachinePlayer takes
	 */
	public boolean forceMove(Move m) {
		if (current.isValidMove(m, color)){
			current.action(m, color);
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * a helper function that finds the best move for MachinePlayer to take
	 * @param side: indicates whose turn it is: either MachinePlayer.MACHINE 
	 * or MachinePlayer.OPPONENT
	 * @param alpha: a score that MACHINE knows with certainty it can achieve
	 * @param beta: a score no more than what the OPPONENT can achieve
	 * @param searchDepth: the depth that this minimax will search into
	 * @return Best: a Best object that contains the best move for this MachinePlayer
	 * to take
	 */
	private Best minimax(boolean side, int alpha, int beta, int searchDepth) { 
		Best myBest = new Best(); 
		Best reply; 
		
		if (searchDepth == 0){
			myBest.setScore(current.evaluation(this.color, this.searchDepth-searchDepth));
			return myBest;
		}
		if (current.networkId(this.color) || current.networkId(oppositeColor())){
			myBest.setScore(current.evaluation(this.color, this.searchDepth-searchDepth));
			return myBest;
		}
		if (side == MachinePlayer.MACHINE) {
			myBest.setScore(alpha);
		} else {
			myBest.setScore(beta);
		}

		try{ 
			int colorUsed = 0;
			if (side == MachinePlayer.MACHINE){
				colorUsed = this.color;
			}
			else{
				colorUsed = oppositeColor();
			}
	  
			DList validMoves = current.listValidMove(colorUsed);
			DListNode curr = (DListNode) validMoves.front();
			myBest.setMove((Move) validMoves.front().item());
	  
			for (int i =0 ; i < validMoves.length() ; i++) {		  
				current.action( ((Move) curr.item()) , colorUsed);
				reply = minimax(!side, alpha, beta, searchDepth-1);
				current.undo(((Move) curr.item()), colorUsed);	      
				if (side == MACHINE && reply.getScore() > myBest.getScore()) {
					myBest.setMove((Move) curr.item());
					myBest.setScore(reply.getScore());
					alpha = reply.getScore();
				} else if (side == OPPONENT && reply.getScore() < myBest.getScore()) {
					myBest.setMove((Move) curr.item());
					myBest.setScore(reply.getScore());
					beta = reply.getScore();
				}
				if (alpha >= beta) { 
					return myBest; 
				}
				curr = (DListNode) curr.next();
			}
		}
		catch (InvalidNodeException e){
			System.out.println("invalid node");
		}
		return myBest;
	}

public static void main(String args[]){
	final int BLACK = 0; 
	final int WHITE = 1; 

	
	//NULL POINTER TEST!!!!!!
	MachinePlayer p2 = new MachinePlayer(WHITE);
	
	//White moves
	Move j1 = new Move(0, 1);
	Move j2 = new Move(6, 2);
	Move j3 = new Move(1, 2);
	Move j4 = new Move(3, 4);
	Move j5 = new Move(3, 2);
	Move j6 = new Move(4, 1);
	Move j7 = new Move(7, 1);
	
	//Make white moves
	p2.current.action(j1, WHITE);
	p2.current.action(j2, WHITE);
	p2.current.action(j3, WHITE);
	p2.current.action(j4, WHITE);
	p2.current.action(j5, WHITE);
	p2.current.action(j6, WHITE);
	p2.current.action(j7, WHITE);
	
	//Black moves
	Move j12 = new Move(1, 7);
	Move j13 = new Move(4, 4);
	Move j14 = new Move(1, 4);
	Move j15 = new Move(2, 2);
	Move j16 = new Move(4, 2);
	Move j17 = new Move(1, 0);
	
	//Make black moves
	p2.current.action(j12, BLACK);
	p2.current.action(j13, BLACK);
	p2.current.action(j14, BLACK);
	p2.current.action(j15, BLACK);
	p2.current.action(j16, BLACK);
	p2.current.action(j17, BLACK);
	
	p2.current.printBoard();
	
	System.out.println(p2.current.networkId(WHITE));

	System.out.println("Testing chooseMove2");
	p2.current.printBoard();
	System.out.println("end printing for tests");
	System.out.println("-----------------------------------------");
	Move bestMoveReturned2 = p2.chooseMove();
	System.out.println(bestMoveReturned2);
	System.out.println("-----------------------------------------");
	p2.current.printBoard();
	

	
	
}
}