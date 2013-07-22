import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/* 
 * Class: LightsOut
 * ----------------
 * Stores information related to the current game of Lights Out.
 * Acts as the main window for the game as well.
 */
public class LightsOut extends JFrame implements ActionListener {
    private static final int MIN_SIZE = 2;
    private static final int MAX_SIZE = 10;
    private static final int GRID_SIZE = 250;
    private int buttonSize = 50; 
    private JToggleButton[][] bArray; 
    private Random rgen;
    private int size = 5; 

    /*
     * Constructor
     * -----------
     * Initializes the game window. Arranges ToggleButtons in a
     * grid.
     */
    public LightsOut() {
	setTitle("Lights Out");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	while(!setUserSize()) {}
	buttonSize = GRID_SIZE/size;
	rgen = new Random();
	bArray = new JToggleButton[size][size];
	Container ca = getContentPane();
	ca.setLayout(new GridLayout(size,size));
	Dimension d = new Dimension(buttonSize,buttonSize);
	for(int i = 0; i < size; i++) {
	    for(int j = 0; j < size; j++) {
		JToggleButton b = new JToggleButton();
		//button's name contains its row and column
		String name = i + " " + j;
		b.setActionCommand(name);
		b.addActionListener(this);
		b.setPreferredSize(d);
		b.setBackground(Color.WHITE);
		bArray[i][j] = b;
		ca.add(b);
	    }
	}
	shuffleButtons();
	pack();
	setVisible(true);
    }
    
    /*
     * setUserSize
     * -----------
     * Asks the user to provide the size (number of buttons per side).
     * Sets the class's size equal to that size.
     * Returns true when a valid size is entered. Returns false otherwise.
     */
    private boolean setUserSize() {
	try {
	    String sizeString = JOptionPane.showInputDialog(this, "Enter size of grid.", "Enter Size", JOptionPane.QUESTION_MESSAGE);
	    int parsedSize = Integer.parseInt(sizeString);
	    size = parsedSize;
	    return parsedSize >= MIN_SIZE && parsedSize <= MAX_SIZE;
	} catch(NumberFormatException nfe) {
	    return false;
	}
    }

    /*
     * actionPerformed
     * ---------------
     * Responds to user's click. Toggles the clicked button
     * and the four buttons it shares an edge with.
     */
    public void actionPerformed(ActionEvent ie) {
	try {
	    //get button's name, extract position
	    String name = ie.getActionCommand();
	    String[] rc = name.split(" ");
	    int r = Integer.parseInt(rc[0]);
	    int c = Integer.parseInt(rc[1]);
	    
	    JToggleButton b = bArray[r][c];
	    toggleButton(r+1,c);
	    toggleButton(r,c+1);
	    toggleButton(r-1,c);
	    toggleButton(r,c-1);
	    testVictory();
	} catch(NumberFormatException nfe) {
	    System.err.println("Bad parse!");
	} catch(ClassCastException cce) {
	    System.err.println("Bad cast!");
	} catch(Exception e) {
	    System.err.println("Bad!");
	}
    }
    
    /*
     * toggleButton
     * ------------
     * If arguments r and c are within the confines of the grid,
     * toggle the button at that position.
     */
    private void toggleButton(int r, int c) {
	if((r >= 0 && r < size) && (c >=0 && c < size)) {
	    bArray[r][c].setSelected(!bArray[r][c].isSelected());
	}
    }
    
    /*
     * testVictory
     * -----------
     * Determines if the user has solved the puzzle.
     */
    private void testVictory() {
	boolean result = true;
	for(int i = 0; i < size; i++) {
	    for(int j = 0; j < size; j++) {
		if(!bArray[i][j].isSelected()) {
		    result = false;
		}
	    }
	}
	// if result is still true, than all the "lights" are out
	if(result) {
	    // alert the user, restart a new puzzle if wanted, else close the window
	    int option = JOptionPane.showConfirmDialog(this, "You solved it! Play again?", "Victory!", JOptionPane.YES_NO_OPTION);
	    if(option == JOptionPane.YES_OPTION) {
		shuffleButtons();
	    } else {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	    }
	}
	
    }
    
    /*
     * shuffleButtons
     * --------------
     * Randomly create a solvable puzzle. The board begins with all
     * lights out, but this method performs clicks on random squares
     * to turn some lights on.
     */
    private void shuffleButtons() {
	for(int i = 0; i < size; i++) {
	    for(int j = 0; j < size; j++) {
		bArray[i][j].setSelected(true);
	    }
	}
	boolean[][] clicked = new boolean[size][size];
	int minClicks = rgen.nextInt(size*size) + 1;
	int clickCount = 0;
	while(clickCount < minClicks) {
	    int r = rgen.nextInt(size);
	    int c = rgen.nextInt(size);
	    if(!clicked[r][c]) {
		bArray[r][c].doClick();
		clickCount++;
	    }
	}
    }
    
    /*
     * Main Method
     * -----------
     * Creates a new LightsOut game.
     */
    public static void main(String[] args) {
	try {
	    // use Java's cross platform look and feel
	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    LightsOut l = new LightsOut();
	} catch(Exception e) {
	    System.err.println("Look and feel not found");
	}
    }
}