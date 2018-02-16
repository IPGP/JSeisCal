
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class MetrozetTabPane extends JPanel {
	
	private String axis = null;
	public JButton dampBtn = null;
	public JButton undampBtn = null;
	public JButton spBtn = null;
	public JButton lpBtn = null;
	public JButton movePlusBtn = null;
	public JButton moveMinusBtn = null;
	public JButton stopBtn = null;
	
	public JButton boomAdcBtn = null;
	

    
	MetrozetTabPane(String ax) {
		this.axis = ax;
		this.dampBtn = new JButton(this.axis + "DAMP");
		this.undampBtn = new JButton(this.axis + "UNDAMP");
		this.spBtn = new JButton(this.axis + "10");
		this.lpBtn = new JButton(this.axis + "360");
		this.movePlusBtn = new JButton(this.axis + "MOVE+");
		this.moveMinusBtn = new JButton(this.axis + "MOVE-");

		this.boomAdcBtn = new JButton(this.axis + "BOOMADC");
		
		this.stopBtn = new JButton("STOP");
		
        this.setLayout(new GridLayout(4,2));
		this.add(dampBtn);
        this.add(undampBtn);
        this.add(spBtn);
        this.add(lpBtn);
        this.add(movePlusBtn);
        this.add(moveMinusBtn);
        this.add(stopBtn);
        this.add(boomAdcBtn);
		
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
        frame.add(new MetrozetTabPane("TEST"));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
