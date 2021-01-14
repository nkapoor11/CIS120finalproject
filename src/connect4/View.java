/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing

package connect4;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;

import java.util.LinkedList; 

public class View {
	
	
	
	static Board currentBoard;
	static JFrame primaryFrame; 
	static int rows = 6;
    static int cols = 7;
    static int sequence = 4;
	static JButton[] listOfButtons;
	public static JLabel label = null;
	static JPanel primaryPanel;
    static JPanel PositionPanel;
    static JLayeredPane pane;
    static int width = 550;
    static int height = 525;
	static LinkedList<Board> undo = new LinkedList<Board>();
	static LinkedList<JLabel> undoLabels = new LinkedList<JLabel>();
	static LinkedList<Board> redo = new LinkedList<Board>();
	static LinkedList<JLabel> redoLabels = new LinkedList<JLabel>();

	static JMenuBar bar;
	static JMenu menu;
	static JMenuItem startNew;
	static JMenuItem undoMenuItem;
	static JMenuItem redoMenuItem;

	static JMenuItem exit;
	static JMenuItem save;
	static JMenuItem loadMenuItem;
	static JMenu info;
	static JMenuItem instructions;
	
	static String filename = "board.txt";
	public static void save() throws IOException {
		try {
			File f = new File(filename);
			if (!f.exists()) { 
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
		    try (BufferedWriter bw = new BufferedWriter(fw)) {
		        int[][] arrBoard = currentBoard.getBoard();
		        String s = currentBoard.getPreviousTurn() + ""; // store the sequence of integers from arrBoard.
		        for (int row = 0; row < arrBoard.length; row++) {
		        	for (int col = 0; col < arrBoard[0].length; col++) {
		        		int entry = arrBoard[row][col];
		        		s += Integer.toString(entry);
		        	}
		        }
		    	bw.write(s, 0, s.length());
		    	bw.close(); 
		    }

		    System.out.println("Game successfully saved!");
		} catch (IOException e) {
			System.out.println("error writing out board state.");
		}
	}
	
	public static void load() throws IOException {
		FileInputStream fstream = new FileInputStream(filename);
		
		if (filename == null) { // deleted for compiler
            throw new IllegalArgumentException();
        } 
		File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found!");
        }
        
		String ln = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		if ((ln = br.readLine()) != null) {
			System.out.println("right here");
			System.out.println("line is: " + ln);
			int[][] arrBoardToLoad = new int[rows][cols];
			int startIndex = 1;
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {				
					String str = ln.substring(startIndex, startIndex + 1);
					arrBoardToLoad[r][c] = Integer.valueOf(str);
					if (c == 6) {
						startIndex++;
					}					
					startIndex++;
				}
			}
			for (int i = 0; i < arrBoardToLoad.length; i++) {
				for (int j = 0; j < arrBoardToLoad[0].length; j++) {
					System.out.println("[" + i + "][" + j + "] is:" + arrBoardToLoad[i][j]);
				}
			}			
			startGame();
			for (int i = 0; i < arrBoardToLoad.length; i++) {
				for (int j = 0; j < arrBoardToLoad[0].length; j++) {
					if (arrBoardToLoad[i][j] == 1 || arrBoardToLoad[i][j] == 2) {
					    showToken(arrBoardToLoad[i][j], i, j);
					}
				}
			}
			currentBoard.setBoard(arrBoardToLoad);
			
			int previousTurn = Integer.valueOf(ln.substring(0, 1));
			int currentTurn;
			if (previousTurn == 0) {
				currentTurn = 1;
			} else {
				currentTurn = 0;
			}
			currentBoard.setCurrentTurn(currentTurn); 
			currentBoard.setPreviousTurn(previousTurn);
			
			System.out.println("Game successfully loaded!");
			
		} else {
			System.out.println("File is empty.");
		}
		
		
				
	}
	
	public View() {
	    listOfButtons = new JButton[cols];
		for (int i = 0; i < cols; i++) {
		    String display = i + 1 + "";
		    listOfButtons[i] = new JButton(display);
		    listOfButtons[i].setFocusable(false);
		}
	}
	
	private static void setUpFrame() {		
		bar = new JMenuBar();
		
		menu = new JMenu("Menu");
		startNew = new JMenuItem("New game");
		undoMenuItem = new JMenuItem("Undo    Ctrl+Z");
		redoMenuItem = new JMenuItem("Redo    Ctrl+Y");
		save = new JMenuItem("Save");
		loadMenuItem = new JMenuItem("Load");
		exit = new JMenuItem("End game");
		info = new JMenu("Info");
		instructions = new JMenuItem("Instructions");		
		undoMenuItem.setEnabled(false);
		redoMenuItem.setEnabled(false);

		startNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    startGame();
			}
		});
		
		undoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		
		redoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"To place a checker, click on the buttons numbered 1-" + cols + ".\nWhoever places 4 checkers of their color in a row first wins." + 
						"\n'in a row' could be along the row or column or along a diagonal.",
						"Instructions", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		menu.add(startNew);
		menu.add(undoMenuItem);
		menu.add(redoMenuItem);
		menu.add(save);
		menu.add(loadMenuItem);
		menu.add(exit);
		
		info.add(instructions);

		bar.add(menu);
		bar.add(info);
		
		primaryFrame.setJMenuBar(bar);
		primaryFrame.setVisible(true);
		
	}
	
	public static JLayeredPane makeBoard() {
	    pane = new JLayeredPane();
	    pane.setPreferredSize(new Dimension(width, height));

		ImageIcon imageBoard = new ImageIcon("files/Boardconnect4.v3.png");
		JLabel imageBoardLabel = new JLabel(imageBoard);

		imageBoardLabel.setBounds(30, 20, imageBoard.getIconWidth(), imageBoard.getIconHeight());
		pane.add(imageBoardLabel, 0, 1);
		
		return pane;
	}
	
	
	public static KeyListener listener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			String s = KeyEvent.getKeyText(e.getKeyCode());
			
			for (int i=0; i<7; i++) {
				if (s.equals(i+1+"")) {
				    nextTurn(i);
					
					if (!currentBoard.isOutOfBounds()) {
						boolean isGameOver = resume();
					}
					break;
				}
			}
			if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) &&
					(e.getKeyCode() == KeyEvent.VK_Z)) {
                undo();
            }
			else if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) &&
					(e.getKeyCode() == KeyEvent.VK_Y)) {
				redo();
            }
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	};
	
	public static void startGame() {
		
	    try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e2) {
                e2.printStackTrace();   
            }
        }
		enable(true);

		
		currentBoard = new Board();
		
		undo.clear();
		undoLabels.clear();
		
		redo.clear();
		redoLabels.clear();
		
		if (primaryFrame != null) primaryFrame.dispose();
		primaryFrame = new JFrame("Connect4 Game CIS 120 Neil Kapoor");
		align(primaryFrame, width, height);
		Component compMainWindowContents = createContentComponents();
		primaryFrame.getContentPane().add(compMainWindowContents, BorderLayout.CENTER);
		
		primaryFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		if (primaryFrame.getKeyListeners().length == 0) {
		    primaryFrame.addKeyListener(listener);
		}
		
		primaryFrame.setFocusable(true);
		
		
		primaryFrame.pack();
		
		JToolBar t = new JToolBar();
        t.setFloatable(false);
        primaryFrame.add(t, BorderLayout.PAGE_END);
		
        setUpFrame();
	}
	
	
	
	public static void align(Window w, int width, int height) {
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    int xpos = (int) (d.getWidth() - w.getWidth() - width) / 2;
	    int ypos = (int) (d.getHeight() - w.getHeight() - height) / 2;
	    w.setLocation(xpos, ypos);
	}
	
	
	
	public static void nextTurn(int col) {
	    currentBoard.setOutOfBounds(false);
		
		int row = currentBoard.getPrevious().getRow();
		int column = currentBoard.getPrevious().getCol();
		int turn = currentBoard.getPreviousTurn();
		
		if (currentBoard.getPreviousTurn() == 2) {
		    currentBoard.nextTurn(col, 1);
		} else {
		    currentBoard.nextTurn(col, 2);
		}
		
		if (currentBoard.isOutOfBounds()) {
		    currentBoard.getPrevious().setRow(row);
		    currentBoard.getPrevious().setCol(column);
		    currentBoard.setPreviousTurn(turn);
			
			undo.pop();
		}

	}
	
	public static void showToken(int color, int row, int col) {
		String str = getColorNameByNumber(color);
		int dx = 75 * col;
		int dy = 75 * row;
		ImageIcon i = new ImageIcon("files/" + str + ".png");
		
		JLabel label = new JLabel(i);
		label.setBounds(37 + dx, 27 + dy, i.getIconWidth(),i.getIconHeight());
		pane.add(label, 0, 0);
		
		undoLabels.push(label);
	}
	
	public static final String getColorNameByNumber(int number) {
        switch (number) {
            case 1:
                return "Red";
            case 2:
                return "Green";
            default:
                return "Red";
        }
    }
	
	public static boolean resume() {
		int r = currentBoard.getPrevious().getRow();
		int c = currentBoard.getPrevious().getCol();
		int turn = currentBoard.getPreviousTurn();
		
		if (turn == 1) {
		    showToken(1, r, c);
		}
		
		if (turn == 2) {
		    showToken(2, r, c);
		}

		boolean isDone = currentBoard.gameDone(); 
		if (isDone) {
			done();
		}
		
		undoMenuItem.setEnabled(true);
		
		redo.clear();
		redoLabels.clear();
		redoMenuItem.setEnabled(false);

		return isDone;
	}
	
	public static void enable(boolean b) {
		if (b) {
			
			for (int i=0; i<listOfButtons.length; i++) {
				JButton button = listOfButtons[i];
				int column = i;
				
				if (button.getActionListeners().length == 0) { 
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
					        undo.push(new Board(currentBoard));
					        nextTurn(column);
							
							if (!currentBoard.isOutOfBounds()) {
								boolean isGameOver = resume();
							}
							primaryFrame.requestFocusInWindow();
						}
					});
				}
			}
		
		} else {
			
			for (JButton button: listOfButtons) {
				for (ActionListener actionListener: button.getActionListeners()) {
					button.removeActionListener(actionListener);
				}
			}
		
		}
	}
	
	
	/**
	 * It returns a component to be drawn by main window.
	 * This function creates the main window components.
	 * It calls the "actionListener" function, when a click on a button is made.
	 */
	public static Component createContentComponents() {
		
	    PositionPanel = new JPanel();
	    PositionPanel.setLayout(new GridLayout(1, cols, rows, 4));
	    PositionPanel.setBorder(BorderFactory.createEmptyBorder(2, 22, 2, 22));
		
		for (JButton button: listOfButtons) {
		    PositionPanel.add(button);
		}
		
		pane = makeBoard();
		
		primaryPanel = new JPanel();
		primaryPanel.setLayout(new BorderLayout());
		primaryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		primaryPanel.add(PositionPanel, BorderLayout.NORTH);
		primaryPanel.add(pane, BorderLayout.CENTER);

		primaryFrame.setResizable(false);
		return primaryPanel;
	}
	
	
	public static void done() {
	    currentBoard.setDone(true);
		
		int choice = 0;
		if (currentBoard.getWinner() == 1) {
				choice = JOptionPane.showConfirmDialog(null,
						"Player 1 wins! Start a new game?",
						"Game Over", JOptionPane.YES_NO_OPTION);
		} else if (currentBoard.getWinner() == 2) {
				choice = JOptionPane.showConfirmDialog(null,
						"Player 2 wins! Start a new game?",
						"Game Over", JOptionPane.YES_NO_OPTION);
		} else if (currentBoard.drawExists()) {
			choice = JOptionPane.showConfirmDialog(null,
					"It's a draw! Start a new game?",
					"Game Over", JOptionPane.YES_NO_OPTION);
		}
		
		enable(false);

		for (KeyListener keyListener: primaryFrame.getKeyListeners()) {
		    primaryFrame.removeKeyListener(keyListener);
		}
		
		if (choice == JOptionPane.YES_OPTION) {
		    startGame();
		}

	}
	
	
	@SuppressWarnings("static-access")
	public static void main(String[] args){
		View connect4 = new View();
		connect4.startGame();
	}
	private static void undo() {
        if (!undo.isEmpty()) {
        	try {
        	    currentBoard.setDone(false);
        		
        		enable(true);
        		
        		if (primaryFrame.getKeyListeners().length == 0) {
        		    primaryFrame.addKeyListener(listener);
        		}
        		
        		JLabel l = undoLabels.pop();
        		
        		redo.push(new Board(currentBoard));
        		redoLabels.push(l);
        
        		currentBoard = undo.pop();
        		pane.remove(l);
        		primaryFrame.paint(primaryFrame.getGraphics());
        	} catch (ArrayIndexOutOfBoundsException ex) {
        		System.err.println("No move has been made yet!");
        		System.err.flush();
        	}
        
        	if (undo.isEmpty()) {
        	    undoMenuItem.setEnabled(false);
        	}
        	
        	redoMenuItem.setEnabled(true);
        
        }
	}


    private static void redo() {
    	if (!redo.isEmpty()) {
    		try {
    		    currentBoard.setDone(false);
    			
    			enable(true);
    			
    			if (primaryFrame.getKeyListeners().length == 0) {
    			    primaryFrame.addKeyListener(listener);
    			}
    			
    			JLabel redoCheckerLabel = redoLabels.pop();
    			
    			undo.push(new Board(currentBoard));
    			undoLabels.push(redoCheckerLabel);
    			
    			currentBoard = new Board(redo.pop());
    			pane.add(redoCheckerLabel, 0, 0);
    			
    			primaryFrame.paint(primaryFrame.getGraphics());
    			
    			boolean isGameOver = currentBoard.gameDone(); 
    			if (isGameOver) {
    				done();
    			}
    		} catch (ArrayIndexOutOfBoundsException ex) {
    			System.err.println("There is no move to redo!");
    			System.err.flush();
    		}
    		if (redo.isEmpty())
    		    redoMenuItem.setEnabled(false);
    		
    		undoMenuItem.setEnabled(true);
    	}
    }
	
}