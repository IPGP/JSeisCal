import java.awt.Checkbox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jlib330.Data;


public class ChannelsOverview extends JPanel implements Observer {
	
    JLabel     bhzLb   = new JLabel("BHZ : ");
    JTextField bhzTf   = new JTextField(15);
    
    JLabel     bhnLb   = new JLabel("BHN : ");
    JTextField bhnTf   = new JTextField(15);
    
    JLabel     bheLb   = new JLabel("BHE : ");
    JTextField bheTf   = new JTextField(15);
    
    JLabel     hhzLb   = new JLabel("HHZ : ");
    JTextField hhzTf   = new JTextField(15);
    JCheckBox  hhzCb   = new JCheckBox();
    
    JLabel     hhnLb   = new JLabel("HHN : ");
    JTextField hhnTf   = new JTextField(15);
    JCheckBox  hhnCb   = new JCheckBox();
    
    JLabel     hheLb   = new JLabel("HHE : ");
    JTextField hheTf   = new JTextField(15);
    JCheckBox  hheCb   = new JCheckBox();
    
    JLabel     bmzLb   = new JLabel("BMZ : ");
    JTextField bmzTf   = new JTextField(15);
    JCheckBox  bmzCb   = new JCheckBox();
    
    JLabel     bmnLb   = new JLabel("BMN : ");
    JTextField bmnTf   = new JTextField(15);
    JCheckBox  bmnCb   = new JCheckBox();
    
    JLabel     bmeLb   = new JLabel("BME : ");
    JTextField bmeTf   = new JTextField(15);
    JCheckBox  bmeCb   = new JCheckBox();
       
    
    JButton    clearAllBtn = new JButton("Clear All Buffers");
	
    Data bufs = null;
	
	ChannelsOverview(Data buffers) {
     
		this.bufs = buffers;
		
	    this.setLayout(new GridBagLayout());
	    
	    bhzTf.setEditable(false);
	    bhnTf.setEditable(false);
	    bheTf.setEditable(false);
	    hhzTf.setEditable(false);
	    hhnTf.setEditable(false);
	    hheTf.setEditable(false);
	    bmzTf.setEditable(false);
	    bmnTf.setEditable(false);
	    bmeTf.setEditable(false);
	    
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(bhzLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(bhzTf, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(bhnLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(bhnTf, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(bheLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(bheTf, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(hhzLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(hhzTf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        this.add(hhzCb, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(hhnLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(hhnTf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        this.add(hhnCb, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(hheLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(hheTf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 5;
        this.add(hheCb, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(bmzLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        this.add(bmzTf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 6;
        this.add(bmzCb, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        this.add(bmnLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        this.add(bmnTf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 7;
        this.add(bmnCb, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        this.add(bmeLb, gbc);
        gbc.gridx = 1;
        gbc.gridy = 8;
        this.add(bmeTf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 8;
        this.add(bmeCb, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 10;        
        //gbc.weightx = 1.0;
        //gbc.gridwidth = 2;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(clearAllBtn, gbc);
        //gbc.fill = GridBagConstraints.NONE;
        
        this.hhzCb.setSelected(true);
        this.hhzCb.setActionCommand("hhzCb");
        this.hhnCb.setSelected(true);
        this.hhnCb.setActionCommand("hhnCb");
        this.hheCb.setSelected(true);
        this.hheCb.setActionCommand("hheCb");
        this.bmzCb.setSelected(true);
        this.bmzCb.setActionCommand("bmzCb");
        this.bmnCb.setSelected(true);
        this.bmnCb.setActionCommand("bmnCb");
        this.bmeCb.setSelected(true);
        this.bmeCb.setActionCommand("bmeCb");
        
	}
	
	
	public void update(Observable arg0, Object arg1) {
		this.bhzTf.setText(Integer.toString(this.bufs.bhz.index));
		this.bhnTf.setText(Integer.toString(this.bufs.bhn.index));
		this.bheTf.setText(Integer.toString(this.bufs.bhe.index));
		
		this.hhzTf.setText(Integer.toString(this.bufs.hhz.index));
		this.hhnTf.setText(Integer.toString(this.bufs.hhn.index));
		this.hheTf.setText(Integer.toString(this.bufs.hhe.index));
		
		this.bmzTf.setText(Integer.toString(this.bufs.bmz.index));
		this.bmnTf.setText(Integer.toString(this.bufs.bmn.index));
		this.bmeTf.setText(Integer.toString(this.bufs.bme.index));
		
		
	}
	
	public void addChannelOverviewListener(ActionListener mal) {
		this.clearAllBtn.addActionListener(mal);
		this.hhzCb.addActionListener(mal);
		this.hhnCb.addActionListener(mal);
		this.hheCb.addActionListener(mal);
		this.bmzCb.addActionListener(mal);
		this.bmnCb.addActionListener(mal);
		this.bmeCb.addActionListener(mal);
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Data buffers = new Data();
        frame.add(new ChannelsOverview(buffers));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
