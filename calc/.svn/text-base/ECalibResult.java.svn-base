package calc;



import java.awt.FlowLayout;
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
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jfree.ui.RefineryUtilities;
import Console.Console;

//import org.jfree.experimental.chart.plot.CombinedXYPlot;


public class ECalibResult extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6813074495248857279L;

	private JTabbedPane tab 	= new JTabbedPane();

	private JButton ffts	= new JButton("FFTs");

	private JMenuBar bar = new JMenuBar();
	private JMenu menuSave = new JMenu("Save");
	private JMenuItem menuItemSaveAsDataset = new JMenuItem("Save All As Dataset");
	private JMenuItem menuItemSaveAsSeife = new JMenuItem("Save As Seife file");
	private JMenu menuOpen = new JMenu("Open");
	private JMenuItem menuItemOpenDataset = new JMenuItem("Open Dataset");
	private JMenuItem menuItemOpenSeife = new JMenuItem("Open Seife file");
	private JMenuItem menuItemOpenCalex = new JMenuItem("Open Calex-EW File");


	public JPanel      paramsPanel = new JPanel();

	private ECalibDataset dataset;

	private JFileChooser fc; 

	public Console console = null;

	public ECalibResult(ECalibDataset ds, boolean display) {

		/*
		 * Set values
		 */
		dataset = ds;

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
		paramsPanel.add(ffts);
		ffts.addActionListener(this);

		paramsPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Parameters"));


		/*
		 * Build tabbed panels
		 */
		tab.add("Channel 1",new ECalibResultPanel("Data", ds.times, ds.samples[0]));
		tab.add("Channel 2",new ECalibResultPanel("Data", ds.times, ds.samples[1]));
		tab.add("Channel 3",new ECalibResultPanel("Data", ds.times, ds.samples[2]));


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

		 menuSave.add(menuItemSaveAsDataset);
		 menuSave.add(menuItemSaveAsSeife);
		 menuItemSaveAsSeife.addActionListener(this);
		 bar.add(menuSave);
		 menuOpen.add(menuItemOpenDataset);
		 menuOpen.add(menuItemOpenSeife);
		 menuItemOpenSeife.addActionListener(this);
		 menuOpen.add(menuItemOpenCalex);
		 menuItemOpenCalex.addActionListener(this);
		 bar.add(menuOpen);
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
						dataset.samples[0][i++] = Double.valueOf(tok.nextToken());
						//System.out.println(dataset.samples[i-1]);
					}

				}
				//Close the input stream
				in.close();			    	
				this.remove(tab);
				tab = new JTabbedPane();
				tab.add("Data",new ECalibResultPanel("Data", dataset.times, dataset.samples[0]));

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
	 * Open Calex-EW format file
	 */

	public void OpenCalex() {
		//FileInputStream fstream = new FileInputStream("dispcal/cal7.dat");
		try {
			//Create a file chooser
			fc = new JFileChooser();

			//In response to a button click:
			int returnVal = fc.showOpenDialog(getParent());

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				Object[] possibilities = {"ham", "spam", "yam"};
				String s = (String)JOptionPane.showInputDialog(
						null,
						"Sample Rate ?",
						"Customized Dialog",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						null);


				dataset.samplerate = Integer.valueOf(s);
				System.out.println(dataset.samplerate);



				FileInputStream fstream = new FileInputStream(fc.getSelectedFile().getAbsolutePath());
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader  br = new BufferedReader(new InputStreamReader(in));

				String strLine;
				int ch = 0;
				int i = 0;
				double [][] buffers = new double[3][1440000];

				while (((strLine = br.readLine()) != null) && (i<1440000)) {
					StringTokenizer tok = new StringTokenizer(strLine);
					ch = 0;
					while(tok.hasMoreTokens() && (ch < 3)) {
						buffers[ch++][i] = Double.valueOf(tok.nextToken());
					}

					// next line
					i++;	    	    	
				}

				dataset.samples = new double[3][i];
				dataset.setData(0, i, buffers[0], buffers[1], buffers[2]);

				dataset.times = new long[i];

				for (int j =0; j<i ;j++) {
					double index = (double) j;
					double sr = (double) dataset.samplerate;
					double temp = 1000*(index/sr);
					dataset.times[j] = (long)temp;
				}


				//Close the input stream
				in.close();			    	
				this.remove(tab);
				tab = new JTabbedPane();
				tab.add("Channel 1",new ECalibResultPanel("Data", dataset.times, dataset.samples[0]));
				tab.add("Channel 2",new ECalibResultPanel("Data", dataset.times, dataset.samples[1]));
				tab.add("Channel 3",new ECalibResultPanel("Data", dataset.times, dataset.samples[2]));

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
			WriteFile(dataset.samples[0],fc.getSelectedFile().getAbsolutePath(),"%13.0f%13.0f%13.0f%13.0f%13.0f", dataformat  );

		} 
	}

	public void FFTs() {
		System.out.println("Calc FFTs");

		// find power of two >= to num of points in fdata
		int nPointsPow2 = 1;
		int l = this.dataset.samples[0].length;		
		while (nPointsPow2 < l) {
			nPointsPow2 *= 2;  
		}

		// Calculate spectrograms
		double[] spec0 = Cmplx.Spectro(this.dataset.samples[0], nPointsPow2);
		double[] spec1 = Cmplx.Spectro(this.dataset.samples[1], nPointsPow2);
		double[] spec2 = Cmplx.Spectro(this.dataset.samples[2], nPointsPow2);

		// get length of spectrograms
		int size = spec0.length;
		
		// set frequencies for spectrograms
		double df = 1. / (2*spec1.length / this.dataset.samplerate);
		double[] freqz = new double[size];
		double f = df;
		for (int i=0; i<size;i++) {
			freqz[i] = Math.log10(f);
			f += df;			
		}

		// set frequencies for smoothed spectrograms
		double xlogmin = Math.log10(0.001);
		double xlogmax = Math.log10(10);
		int size_smooth = 100;
		double xlogstep = (xlogmax - xlogmin)/size_smooth;
		double[] freqz_smooth = new double [size_smooth]; 

		for (int i=0;i<size_smooth;i++) {
			freqz_smooth[i] = xlogmin+i*xlogstep;
		}
		
		// Smooth Spectrograms
		double [] spec_smooth0;
		double [] spec_smooth1;
		double [] spec_smooth2;
		spec_smooth0 = smooth_fft(freqz, freqz_smooth, spec0);
		spec_smooth1 = smooth_fft(freqz, freqz_smooth, spec1);
		spec_smooth2 = smooth_fft(freqz, freqz_smooth, spec2);
		
		// Make spectral division (substraction in log space) - use spec2 as reference
		double[][] display_div0 = new double [2][size];
		double[][] display_div1 = new double [2][size];
		double[][] display_div2 = new double [2][size];
		System.arraycopy(freqz, 0, display_div0[0], 0, size);
		System.arraycopy(freqz, 0, display_div1[0], 0, size);
		System.arraycopy(freqz, 0, display_div2[0], 0, size);	
		for (int i=0; i<size;i++) {
			display_div0[1][i] = (spec0[i] - spec2[i]) + 20*freqz[i];
			display_div1[1][i] = (spec1[i] - spec2[i]) + 20*freqz[i];
			display_div2[1][i] = (spec2[i] - spec2[i]) + 20*freqz[i];
		}
		
		// smooth spectral division specs
		double [][] div_smooth0 = new double [2][size_smooth];
		double [][] div_smooth1 = new double [2][size_smooth];
		double [][] div_smooth2 = new double [2][size_smooth];
		System.arraycopy(freqz_smooth, 0, div_smooth0[0], 0, size_smooth);
		System.arraycopy(freqz_smooth, 0, div_smooth1[0], 0, size_smooth);
		System.arraycopy(freqz_smooth, 0, div_smooth2[0], 0, size_smooth);	
//		div_smooth0[1] = smooth_fft(freqz, freqz_smooth, display_div0[1]);
//		div_smooth1[1] = smooth_fft(freqz, freqz_smooth, display_div1[1]);
//		div_smooth2[1] = smooth_fft(freqz, freqz_smooth, display_div2[1]);
		for (int i=0; i<size_smooth;i++) {
			div_smooth0[1][i] = (spec_smooth0[i] - spec_smooth2[i]) + 20*freqz_smooth[i];
			div_smooth1[1][i] = (spec_smooth1[i] - spec_smooth2[i]) + 20*freqz_smooth[i];
			div_smooth2[1][i] = (spec_smooth2[i] - spec_smooth2[i]) + 20*freqz_smooth[i];
		}
		
		// set display spec
		double[][] display_spec0 = new double [2][size];
		double[][] display_spec1 = new double [2][size];
		double[][] display_spec2 = new double [2][size];
		System.arraycopy(freqz, 0, display_spec0[0], 0, size);
		System.arraycopy(freqz, 0, display_spec1[0], 0, size);
		System.arraycopy(freqz, 0, display_spec2[0], 0, size);		
		System.arraycopy(spec0, 0, display_spec0[1], 0, size);
		System.arraycopy(spec1, 0, display_spec1[1], 0, size);
		System.arraycopy(spec2, 0, display_spec2[1], 0, size);
		
		// set display spec smooth
		double[][] display_spec_smooth_0 = new double [2][size_smooth];
		double[][] display_spec_smooth_1 = new double [2][size_smooth];
		double[][] display_spec_smooth_2 = new double [2][size_smooth];
		System.arraycopy(freqz_smooth, 0, display_spec_smooth_0[0], 0, size_smooth);
		System.arraycopy(freqz_smooth, 0, display_spec_smooth_1[0], 0, size_smooth);
		System.arraycopy(freqz_smooth, 0, display_spec_smooth_2[0], 0, size_smooth);		
		System.arraycopy(spec_smooth0, 0, display_spec_smooth_0[1], 0, size_smooth);
		System.arraycopy(spec_smooth1, 0, display_spec_smooth_1[1], 0, size_smooth);
		System.arraycopy(spec_smooth2, 0, display_spec_smooth_2[1], 0, size_smooth);		

		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());	

		FastScatterPlotDemo spec0Panel = new FastScatterPlotDemo(display_spec0, "Spectro 0 ");
		frame.getContentPane().add(spec0Panel);
		FastScatterPlotDemo spec1Panel = new FastScatterPlotDemo(display_spec1, "Spectro 1 ");
		frame.getContentPane().add(spec1Panel);
		FastScatterPlotDemo spec2Panel = new FastScatterPlotDemo(display_spec2, "Spectro 2 ");
		frame.getContentPane().add(spec2Panel);
		
		FastScatterPlotDemo specSmooth0Panel = new FastScatterPlotDemo(display_spec_smooth_0, "Spectro 0 Smoothed");
		frame.getContentPane().add(specSmooth0Panel);
		FastScatterPlotDemo specSmooth1Panel = new FastScatterPlotDemo(display_spec_smooth_1, "Spectro 1 Smoothed");
		frame.getContentPane().add(specSmooth1Panel);
		FastScatterPlotDemo specSmooth2Panel = new FastScatterPlotDemo(display_spec_smooth_2, "Spectro 2 Smoothed");
		frame.getContentPane().add(specSmooth2Panel);
		
		FastScatterPlotDemo div0Panel = new FastScatterPlotDemo(display_div0, "Div0");		
		frame.getContentPane().add(div0Panel);
		FastScatterPlotDemo div1Panel = new FastScatterPlotDemo(display_div1, "Div1");
		frame.getContentPane().add(div1Panel);
		FastScatterPlotDemo div2Panel = new FastScatterPlotDemo(display_div2, "Div2");
		frame.getContentPane().add(div2Panel);
		
		FastScatterPlotDemo divSmooth0Panel = new FastScatterPlotDemo(div_smooth0, "Div smooth 0");		
		frame.getContentPane().add(divSmooth0Panel);
		FastScatterPlotDemo divSmooth1Panel = new FastScatterPlotDemo(div_smooth1, "Div smooth 1");
		frame.getContentPane().add(divSmooth1Panel);
		FastScatterPlotDemo divSmooth2Panel = new FastScatterPlotDemo(div_smooth2, "Div smooth 2");
		frame.getContentPane().add(divSmooth2Panel);
		
		frame.pack();
		frame.setVisible(true);


	}
	
	
	public double[] smooth_fft(double[] freqzin, double[] freqzout, double[] spec) {
		int npts = spec.length;
		double xlogstep = (freqzout[1] - freqzout[0]) / 2;
		int size = freqzout.length;
		double[] spec_smoothed = new double[size];
		
		double temp = 0;
		int j =0;
		int n = 0;

		// jump to first point of interest
		while ((j < npts / 2 ) && (freqzin[j] < (freqzout[0]-xlogstep))){
			j++;
		}


		for(int i = 0; i < size ; i++) {            	
			while ( ((j+n) < npts-1 ) && (freqzin[j+n] < (freqzout[i]+xlogstep))  ){
				temp += spec[j+n];
				n++;
			}

			if ((n==0) || (n==1)) {
				if (((j+n)>1) && (n==0)) {
					double ax = freqzin[j+n-1];
					double bx = freqzin[j+n];
					double ay = spec[j+n-1];
					double by = spec[j+n];            	
					double cx = freqzout[i];
					double valuetemp = (cx-ax)*(by-ay)/(bx-ax) + ay;
					spec_smoothed[i] = valuetemp;
				}
				if (n==1) {
					spec_smoothed[i] = temp;
				}

			} else {
				// median
				double [] medianArray = new double[n];
				System.arraycopy(spec, j, medianArray, 0, n);
				Arrays.sort(medianArray);                       
				if ((n % 2 == 0) && (n>1)) {
					//this.spectro_smoothed[1][i] = ((medianArray[(n / 2) - 1] + medianArray[(n / 2)]) / 2.0);                	
					spec_smoothed[i] = medianArray[(n / 2) - 1];
				} else {
					spec_smoothed[i] = medianArray[(n-1) / 2];                	                	
				}
				// mean            
//				this.spectro_smoothed[1][i] = temp / n;
			}

			temp = 0;            
			j = j + n ;
			n = 0;
		}
		
		return spec_smoothed;



	}




	/**
	 * Menu and buttons actions
	 */

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Save As Seife file") {
			SaveSeife();
		} else if (arg0.getActionCommand() == "Open Seife file") {
			OpenSeife();
		} else if (arg0.getActionCommand() == "Open Calex-EW File") {
			OpenCalex();
		} else if (arg0.getActionCommand() == "FFTs") {
			FFTs();
		}



	}



	/**
	 * Main test function
	 * @param args
	 */

	public static void main(String[] args) {
		ECalibDataset ds = new ECalibDataset(20);
		ECalibResult cr = new ECalibResult(ds, true);
		cr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

