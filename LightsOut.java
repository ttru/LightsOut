import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LightsOut extends JFrame implements ActionListener {
    
    private int size = 5;
    private JCheckBox[][] cbArray;
    private Random rgen;

    public LightsOut() {
	setTitle("Lights Out");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	rgen = new Random();
	cbArray = new JCheckBox[size][size];
	Container ca = getContentPane();
	ca.setLayout(new GridLayout(size,size));
	for(int i = 0; i < size; i++) {
	    for(int j = 0; j < size; j++) {
		JCheckBox cb = new JCheckBox();
		cb.setSelected(rgen.nextBoolean());
		String name = i + " " + j;
		cb.setActionCommand(name);
		cb.addActionListener(this);
		cbArray[i][j] = cb;
		ca.add(cb);
	    }
	}
	pack();
	setVisible(true);
    }

    public void actionPerformed(ActionEvent ie) {
	try {
	    String name = ie.getActionCommand();
	    String[] rc = name.split(" ");
	    int r = Integer.parseInt(rc[0]);
	    int c = Integer.parseInt(rc[1]);
	    JCheckBox cb = cbArray[r][c];
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

    private void toggleButton(int r, int c) {
	if((r >= 0 && r < size) && (c >=0 && c < size)) {
	    cbArray[r][c].setSelected(!cbArray[r][c].isSelected());
	}
    }
    
    private void testVictory() {
	boolean result = true;
	for(int i = 0; i < size; i++) {
	    for(int j = 0; j < size; j++) {
		if(!cbArray[i][j].isSelected()) {
		    result = false;
		}
	    }
	}
	if(result) {
	    int option = JOptionPane.showConfirmDialog(this, "You solved it! Play again?", "Victory!", JOptionPane.YES_NO_OPTION);
	    if(option == JOptionPane.YES_OPTION) {
		shuffleCheckBoxes();
	    } else {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	    }
	}
	
    }
    
    private void shuffleCheckBoxes() {
	for(int i = 0; i < size; i++) {
	    for(int j = 0; j < size; j++) {
		cbArray[i][j].setSelected(rgen.nextBoolean());
	    }
	}
    }
    
    public static void main(String[] args) {
	LightsOut l = new LightsOut();
    }
}