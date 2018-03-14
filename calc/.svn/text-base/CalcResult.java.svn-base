package calc;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/*
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;*/

import Console.Console;

//import org.jfree.experimental.chart.plot.CombinedXYPlot;


public class CalcResult extends JFrame implements ActionListener{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6813074495248857279L;
	
	private JTabbedPane tab 	= new JTabbedPane();
    private JButton recalcHBtn	= new JButton("ReCalc Horizontal");
    private JButton recalcVBtn	= new JButton("ReCalc Vertical");
    
    private JMenuBar bar = new JMenuBar();
    
    private JMenu menuFile = new JMenu("File");
    private JMenu menuSave = new JMenu("Save");
    private JMenuItem menuItemSaveAsDataset = new JMenuItem("Save All As Dataset");
    private JMenuItem menuItemSaveAsSeife = new JMenuItem("Save As Seife file");
    private JMenu menuOpen = new JMenu("Open");
    private JMenuItem menuItemOpenDataset = new JMenuItem("Open Dataset");
    private JMenuItem menuItemOpenSeife = new JMenuItem("Open Seife file");
    
    private JLabel     displacementLb = new JLabel("Disp : ");
    public  JTextField displacementTf = new JTextField(15);
    
    private JLabel     freePeriodLb   = new JLabel("Free Period : ");
    public  JTextField freePeriodTf   = new JTextField(15);
    private JLabel     dampingLb   = new JLabel("Damping : ");
    public  JTextField dampingTf   = new JTextField(15);
    
    private JLabel     bitsLb   = new JLabel("Bits : ");
    public  JTextField bitsTf   = new JTextField(15);
    
    private JLabel     voltsLb   = new JLabel("Volts : ");
    public  JTextField voltsTf   = new JTextField(15);
    
    public JPanel      paramsPanel = new JPanel();
    
    private CalcDataset dataset;
    
    private JFileChooser fc; 
    
    public Console console = null;
    
	public CalcResult(CalcDataset ds, boolean display) {
		
		/*
		 * Set values
		 */
		dataset = ds;
		displacementTf.setText(String.valueOf(dataset.displacement));
		freePeriodTf.setText(String.valueOf(dataset.freeperiod));
		dampingTf.setText(String.valueOf(dataset.damping));
		bitsTf.setText(String.valueOf(dataset.bits));
		voltsTf.setText(String.valueOf(dataset.volts));
		
		this.setLayout(new GridBagLayout());
		try {
			console = new Console();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Build Parameters panel
		 */
		paramsPanel.setLayout(new GridBagLayout());
	    GridBagConstraints gbcParams = new GridBagConstraints();
	    gbcParams.gridwidth = 1;
	    gbcParams.gridheight = 1;
	    gbcParams.weightx = 0.;
	    gbcParams.weighty = 0.;
	    gbcParams.fill = GridBagConstraints.HORIZONTAL;
        
	    gbcParams.gridx = 0;
	    gbcParams.gridy = 0;
        paramsPanel.add(displacementLb, gbcParams);
        gbcParams.gridx = 1;
        gbcParams.gridy = 0;
        gbcParams.gridwidth = 2;
        paramsPanel.add(displacementTf, gbcParams);
        displacementTf.addActionListener(new DisplacementListener());
        
        gbcParams.gridx = 0;
        gbcParams.gridy = 1;
        gbcParams.gridwidth = 1;
        paramsPanel.add(freePeriodLb, gbcParams);
        gbcParams.gridx = 1;
        gbcParams.gridy = 1;
        gbcParams.gridwidth = 2;
        paramsPanel.add(freePeriodTf, gbcParams);   
        freePeriodTf.addActionListener(new FreePeriodListener());
        
        gbcParams.gridx = 0;
        gbcParams.gridy = 2;
        gbcParams.gridwidth = 1;
        paramsPanel.add(dampingLb, gbcParams);
        gbcParams.gridx = 1;
        gbcParams.gridy = 2;
        gbcParams.gridwidth = 2;
        paramsPanel.add(dampingTf, gbcParams);
        dampingTf.addActionListener(new DampingListener());
        
        gbcParams.gridx = 0;
        gbcParams.gridy = 3;
        gbcParams.gridwidth = 1;
        paramsPanel.add(bitsLb, gbcParams);
        gbcParams.gridx = 1;
        gbcParams.gridy = 3;
        gbcParams.gridwidth = 2;
        paramsPanel.add(bitsTf, gbcParams);
        bitsTf.addActionListener(new BitsListener());
        
        gbcParams.gridx = 0;
        gbcParams.gridy = 4;
        gbcParams.gridwidth = 1;
        paramsPanel.add(voltsLb, gbcParams);
        gbcParams.gridx = 1;
        gbcParams.gridy = 4;
        gbcParams.gridwidth = 2;
        paramsPanel.add(voltsTf, gbcParams);
        voltsTf.addActionListener(new VoltsListener());
        
        gbcParams.gridx = 0;
        gbcParams.gridy = 5;
        gbcParams.gridwidth = 3;
        paramsPanel.add(recalcHBtn, gbcParams);
        recalcHBtn.addActionListener(this);
        gbcParams.gridx = 0;
        gbcParams.gridy = 6;
        gbcParams.gridwidth = 3;
        recalcVBtn.addActionListener(this);
        paramsPanel.add(recalcVBtn, gbcParams);
        
        paramsPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Parameters"));
        
        
        /*
         * Build tabbed panels
         */
		tab.add("Data",new CalcResultPanel("Data", ds.times, ds.samples));
        
		/*if (display) {
    		tab.add("Filtered", new CalcResultPanel("Filtered", ds.times, ds.samplesAfterSez));
    		tab.add("Deconvolved",new CalcResultPanel("Deconvolved", ds.times, ds.samplesDeconvolved));
    		tab.add("Quiet Sections",new CalcResultPanel("Quiet Sections", ds.times, ds.samplesAfterPol2));
    		tab.add("Detrended",new CalcResultPanel("Detrended", ds.times, ds.samplesDetrended));
    		tab.add("Residual", new CalcResultPanel("Residual", ds.times, ds.samplesResidual));
        }*/

		
		/*
		 * Build main panel
		 */
		
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
		gbc.gridx = 0;
		gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    	add(paramsPanel, gbc);
    	
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(console, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;        
		gbc.gridx = 1;
		gbc.gridy = 0;
        gbc.gridheight = 2;
		gbc.weightx = 1.;
		gbc.weighty = 1.;
		add(tab, gbc);
		
		
		/*
		 * Build Menus
		 */
		
//		menuSave.add(menuItemSaveAsDataset);
//		menuSave.add(menuItemSaveAsSeife);
//		menuItemSaveAsSeife.addActionListener(this);
//		bar.add(menuSave);
//		menuOpen.add(menuItemOpenDataset);
//		menuOpen.add(menuItemOpenSeife);
//		menuItemOpenSeife.addActionListener(this);
//		bar.add(menuOpen);
		menuFile.add(menuItemOpenSeife);
		menuItemOpenSeife.addActionListener(this);
		menuFile.add(menuItemSaveAsSeife);
		menuItemSaveAsSeife.addActionListener(this);
		bar.add(menuFile);
		setJMenuBar(bar);
		
        setTitle("Calibration Results");

        pack();        
        this.setVisible(true);
	}
	
	

	
	/**
     * Write 'seife' format file 
     * 
     * @param buffer
     * @param filename
     * @param format
     * @param dataformat
     */
    public static void WriteFile(double[] buffer, String filename, String format, String dataformat) {
    	try{
    		String strLine="";
            // Create file 
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Geoscope Calibration");
            out.newLine();
            out.write(dataformat);
            out.newLine();
            
            for (int i=0;i<buffer.length;i=i+5) {            	
            	strLine = String.format(Locale.US, format,
            			buffer[i],
            			buffer[i+1],
            			buffer[i+2],
            			buffer[i+3],
            			buffer[i+4] );
                out.write(strLine);
                out.newLine();
            }
            
            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
          System.err.println("Error: " + e.getMessage());
        }
        
    }
    
	/**
	 * Open Seife format file
	 */
    
    public void OpenSeife() {
    	//FileInputStream fstream = new FileInputStream("dispcal/cal7.dat");
	    try {
			//Create a file chooser
		    fc = new JFileChooser();
			
			//In response to a button click:
			int returnVal = fc.showOpenDialog(getParent());
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	        
				FileInputStream fstream = new FileInputStream(fc.getSelectedFile().getAbsolutePath());
	    	    DataInputStream in = new DataInputStream(fstream);
	    	    BufferedReader  br = new BufferedReader(new InputStreamReader(in));
	    	    
	    	    String strLine = br.readLine();
	    	    String header = strLine;
	    	    System.out.println ("Header = " + header);
	    	    
	    	    strLine = br.readLine();	 
	    	    System.out.println ("Data Format = " + strLine);
	    	    
        	    StringTokenizer st = new StringTokenizer(strLine);
        	    int nbsamples = Integer.valueOf(st.nextToken());
        	    System.out.println ("nbsamples = " + nbsamples);
        	    String format = st.nextToken();
        	    System.out.println ("format = " + format);
        	    double sampleperiod = Double.valueOf(st.nextToken());
        	    System.out.println ("sampleperiod = " + sampleperiod);
        	    while (st.hasMoreTokens()) {
            	    st.nextToken();
        	    }
        	    // Init dataset with data size
        	    dataset.samplerate = (int) (1. /sampleperiod);
        	    dataset.Init(nbsamples);
        	    // Fill dataset with samples
        	    int i = 0;
	    	    while (((strLine = br.readLine()) != null) && (i<nbsamples) )  {
	    	    	StringTokenizer tok = new StringTokenizer(strLine);
	    	    	//for (int j=0;j<1;j++) {
	    	    	while(tok.hasMoreTokens()) {
	    	    		double temp = i * sampleperiod * 1000;
	    	    		dataset.times[i] = (long) temp;
	    	    		dataset.samples[i++] = Double.valueOf(tok.nextToken());
	    	    		//System.out.println(dataset.samples[i-1]);
	    	    	}
	    	    	
	    	    }
	    	    //Close the input stream
	    	    in.close();			    	
	    	    this.remove(tab);
	    	    tab = new JTabbedPane();
	    		tab.add("Data",new CalcResultPanel("Data", dataset.times, dataset.samples));
	    		
	    		GridBagConstraints gbc = new GridBagConstraints();
	            gbc.gridwidth = 1;	    		
	    		gbc.fill = GridBagConstraints.BOTH;        
	    		gbc.gridx = 1;
	    		gbc.gridy = 0;
	            gbc.gridheight = 2;
	    		gbc.weightx = 1.;
	    		gbc.weighty = 1.;
	    		add(tab, gbc);
	    		
	    		
	    		
	    		this.pack();
	    		this.setVisible(true);
			}
    	    }catch (Exception e){//Catch exception if any
    	      System.err.println("Error read: " + e.getMessage());
    	    }
    }

    
    /**
     * Save current samplesAverageRemoved as seife format file
     */
    
    public void SaveSeife() {
		String s = String.format( "%9d", dataset.nbsamples );
		String dataformat = s + " (5f13.0)                 ";
		s = String.format( Locale.US, "%1.3f", 1./dataset.samplerate );
		dataformat += s + "     0.000     0.000";
		
		//Create a file chooser
		fc = new JFileChooser();
		
		//In response to a button click:
		int returnVal = fc.showOpenDialog(getParent());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            WriteFile(dataset.samples,fc.getSelectedFile().getAbsolutePath(),"%13.0f%13.0f%13.0f%13.0f%13.0f", dataformat  );
	    	
        } 
    }
    
    public double CalcHorizontal() {
		double result = Calc.HorizontalSensitivity(dataset);
		System.out.println("Horizontal Sensitivity = " + result);
	    tab.removeAll();
		tab.add("Data",new CalcResultPanel("Data", dataset.times, dataset.samples));
		tab.add("Filtered", new CalcResultPanel("Filtered", dataset.times, dataset.samplesAfterSez));
		tab.add("Deconvolved",new CalcResultPanel("Deconvolved", dataset.times, dataset.samplesDeconvolved));
    	tab.add("Quiet Sections",new CalcResultPanel("Quiet Sections", dataset.times, dataset.samplesAfterPol2));
    	tab.add("Acceleration Steps",new CalcResultPanel("Acceleration Steps", dataset.times, dataset.samplesDetrended));
    	//tab.add("Residual", new CalcResultPanel("Residual", dataset.times, dataset.samplesResidual));
		this.setVisible(true);
		console.write(String.valueOf(result));
		return result;
    }
    
    public double CalcVertical() {
		double result = Calc.VerticalSensitivity(dataset);
		System.out.println("Vertical Sensitivity = " + result);
		tab.removeAll();
		tab.add("Data",new CalcResultPanel("Data", dataset.times, dataset.samples));
		tab.add("Filtered", new CalcResultPanel("Filtered", dataset.times, dataset.samplesAfterSez));
		tab.add("Deconvolved",new CalcResultPanel("Deconvolved", dataset.times, dataset.samplesDeconvolved));
    	tab.add("Quiet Sections",new CalcResultPanel("Quiet Sections", dataset.times, dataset.samplesAfterQuickSort));
    	tab.add("Displacement",new CalcResultPanel("Displacement", dataset.times, dataset.samplesDetrended));
    	tab.add("Residual", new CalcResultPanel("Residual", dataset.times, dataset.samplesResidual));
		this.setVisible(true);
		console.write(String.valueOf(result));
		return result;
    }

    /**
     * Menu and buttons actions
     */
    
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Save As Seife file") {
			SaveSeife();
		} else if (arg0.getActionCommand() == "Open Seife file") {
	    	OpenSeife();
		} else if (arg0.getActionCommand() == "ReCalc Horizontal") {
			CalcHorizontal();
		} else if (arg0.getActionCommand() == "ReCalc Vertical") {
			CalcVertical();
		}
		
		
			
	}
	
	class DisplacementListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			dataset.displacement = Double.valueOf(displacementTf.getText());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
        	dataset.displacement = Double.valueOf(displacementTf.getText());
        }

	}
	
	class FreePeriodListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			dataset.freeperiod = Double.valueOf(freePeriodTf.getText());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			dataset.freeperiod = Double.valueOf(freePeriodTf.getText());
		}

	}
	
	class DampingListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			dataset.damping = Double.valueOf(dampingTf.getText());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			dataset.damping = Double.valueOf(dampingTf.getText());
		}

	}
	
	class BitsListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			dataset.bits = Double.valueOf(bitsTf.getText());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			dataset.bits = Double.valueOf(bitsTf.getText());
		}

	}
	
	class VoltsListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			dataset.volts = Double.valueOf(voltsTf.getText());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			dataset.volts = Double.valueOf(voltsTf.getText());
		}

	}
	
	
	/**
	 * Main test function
	 * @param args
	 */
	
	public static void main(String[] args) {
		CalcDataset ds = new CalcDataset();
		CalcResult cr = new CalcResult(ds, true);
        cr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

