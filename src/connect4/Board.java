package connect4;
import javax.swing.*;
import java.util.Arrays;

public class Board extends JComponent {
    private int winner;
    private int [][] board;
    
    private boolean outOfBounds;
    private Turn previous;
    private int previousTurn;
    static int rows = 6;
    static int cols = 7;
    static int sequence = 4;
    private int currentTurn;
    private boolean done;
    
    public Board() {
    	this.board = new int[7][8];
    	this.outOfBounds = false;
    	this.winner = 0;
    	this.previous = new Turn();
    	this.previousTurn = 2;
    	this.done = false;
    	this.currentTurn = 0;
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < cols; j++) {
    		    board[i][j] = 0;
    		}
    	}
    }
    
    public Board(Board board) {
        previous = board.getPrevious();
        previousTurn = board.getPreviousTurn();
    	winner = board.getWinner();
    	
    	this.outOfBounds = board.isOutOfBounds();
    	this.done = board.isDone();
    	this.currentTurn = board.getCurrentTurn();
    	
    	int imax = board.getBoard().length;
    	int jmax = board.getBoard()[0].length;
    	this.board = new int[imax][jmax];
    	for(int i = 0; i < imax; i++) {
    		for(int j=0; j < jmax; j++) {
    			this.board[i][j] = board.getBoard()[i][j];
    		}
    	}
    }
    
    public void nextTurn(int col, int player) {
    	try {
    		this.previous = new Turn(firstAvailable(col), col);
    		this.previousTurn = player;
    		this.board[firstAvailable(col)][col] = player;
    		this.currentTurn++;
    	} catch (ArrayIndexOutOfBoundsException e) {
    		System.err.println("Column " + (col+1) + " is full! Try a different column!");
    		setOutOfBounds(true);
    	}
    }
    
    public boolean turnPossible(int row, int col) {
    	if ((row <= -1) || (col <= -1) || (row > rows-1) || (col > cols-1)) {
    		return false;
    	}
    	return true;
    }
    
    
    public boolean isFilled(int c) {
        return board[0][c] != 0;
    }
    
    public int firstAvailable(int col) {
    	int rowPosition = -1;
    	for (int row=0; row<rows; row++) {
    		if (board[row][col] == 0) {
    			rowPosition = row;
    		}
    	}
    	return rowPosition;
    }
    
    public boolean winnerExists() {
    	
    	int seq1 = getSequence(sequence, 1);
    	if (seq1 > 0) {
    		setWinner(1);
    		return true;
    	}
    	
    	int seq2 = getSequence(sequence, 2);
    	if (seq2 > 0) {
    		setWinner(2);
    		return true;
    	}
    	
    	setWinner(0);
    	return false;
    }
    
    
    public boolean gameDone() {
    	if (winnerExists()) {
    		return true;
    	}
    	return drawExists();
    }
    
    public boolean drawExists() {
    	if (done)
    		return false;
    	
    	for(int row=0; row<rows; row++) {
    		for(int col=0; col<cols; col++) {
    			if(board[row][col] == 0) {
                    return false;
                }
            }
        }    	
    	return true;
    }
    public int getSequence(int len, int player) {
    	int times = 0;
    	for (int a = 0; a < rows; a++) {
    		for (int b = 0; b < cols; b++) {
    			if (turnPossible(a, b + sequence - 1)) {
    				int n = 0;
    				while (board[a][b + n] == player && n < len) {
    					n++;
    				}
    				if (n == len) {
    					while (n < sequence && (board[a][b + n] == player || board[a][b + n] == 0)) {
    						n++;
    					}
    					if (n == sequence) {
    					    times++;
    					}
    				}
    				
    			}
    		}
    	}		
    	for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
    			if (turnPossible(a - sequence + 1, b)) {
    				int n = 0;
    				while (n < len && board[a - n][b] == player) {
    					n++;
    				}
    				if (n == sequence) {
    					while (n < sequence && (board[a - n][b] == player || board[a - n][b] == 0)) {
    						n++;
    					}
    					if (n == sequence) {
    					    times++;
    					}
    				}
    				
    			}
    		}
    	}
    
    	for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
    			if (turnPossible(a + sequence - 1, b + sequence - 1)) {					
    				int n = 0;
    				while (n < len && board[a + n][b + n] == player) {
    					n++;
    				}
    				if (n == sequence) {
    					while (n < sequence && (board[a + n][b + n] == player || board[a + n][b + n] == 0)) {
    						n++;
    					}
    					if (n == sequence) times++;
    				}
    				
    			}
    		}
    	}
    	
    	for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
    			if (turnPossible(a - sequence + 1, b + sequence - 1)) {
    				int n = 0;
    				while (n < len && board[a - n][b + n] == player) {
    					n++;
    				}
    				if (n == sequence) {
    					while (n < sequence && (board[a - n][b + n] == player || board[a - n][b + n] == 0)) {
    						n++;
    					}
    					if (n==sequence) {
    					    times++;
    					}
    				}				
    			}
    		}
    	}
    	return times;		
    }
    
    public Turn getPrevious() {
    	return new Turn(previous.getRow(), previous.getCol());
    }
    
    public void setPrevious(Turn previous) { 
    	this.previous.setRow(previous.getRow());
    	this.previous.setNum(previous.getNum());
    	this.previous.setCol(previous.getCol());
    }
    
    
    public int getPreviousTurn() {
    	return previousTurn;
    }
    
    
    public void setPreviousTurn(int previousTurn) {
    	this.previousTurn = previousTurn;
    }
    
    
    public int[][] getBoard() {
    	//return board;
    	int[][] copyOfBoard = new int[board.length][];
    	for (int i = 0; i < board.length; i++) {
    	    copyOfBoard[i] = Arrays.copyOf(board[i], board[i].length);
    	}
    	return copyOfBoard;
    }
    
    
    public void setBoard(int[][] board) {
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < cols; j++) {
    			this.board[i][j] = board[i][j];
    		}
    	}
    }
    
    public int getWinner() {
    	return winner;
    }
    
    
    public void setWinner(int winner) {
    	this.winner = winner;
    }
    
    
    public int getCurrentTurn() {
    	return currentTurn;
    }
    
    
    public void setCurrentTurn(int currentTurn) {
    	this.currentTurn = currentTurn;
    }
    
    
    public boolean isDone() {
    	return done;
    }
    
    
    public void setDone(boolean done) {
    	this.done = done;
    }
    
    
    public boolean isOutOfBounds() { 
    	return outOfBounds;
    }
    
    public void setOutOfBounds(boolean outOfBounds) {
    	this.outOfBounds = outOfBounds;
    }
}
