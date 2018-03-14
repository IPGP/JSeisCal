
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jlib330.Data;
import jlib330.DataChannel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
//import org.jfree.ui.RectangleAnchor;

/**
 * A demo application showing a dynamically updated chart that displays the
 * current JVM memory usage.
 * <p>
 * IMPORTANT NOTE: THIS DEMO IS DOCUMENTED IN THE JFREECHART DEVELOPER GUIDE. DO
 * NOT MAKE CHANGES WITHOUT UPDATING THE GUIDE ALSO!!
 */
public class DisplayChannelPOS extends JPanel implements Observer, ActionListener {

	/** Time series for total memory used. */
	private TimeSeries chan;
	
	private DataChannel dc = null;
	
	private SamplingXYLineRenderer renderer = null;
	private XYPlot plot = null;
	private JFreeChart chart = null;
	private ChartPanel chartPanel = null;
	
    private JPanel toolsPanel = new JPanel();
    private JPanel resultPanel = new JPanel();
    private JPanel maxPanel = new JPanel();
    private JPanel minPanel = new JPanel();
    private JPanel meanPanel = new JPanel();

	public JButton range20mBtn = new JButton("20 Min");
	public JButton range5mBtn = new JButton("5 Min");
	public JButton range30sBtn = new JButton("30 s");
	public JButton zoomInBtn = new JButton("Zoom In");
	public JButton zoomOutBtn = new JButton("Zoom Out");
	public JButton clearBtn = new JButton("Clear");
	
    public JLabel     minLb   = new JLabel("Min : ");
    public JTextField minTf   = new JTextField(7);
    public JTextField minPercTf   = new JTextField(5);
    
    public JLabel     maxLb   = new JLabel("Max : ");
    public JTextField maxTf   = new JTextField(7);
    public JTextField maxPercTf   = new JTextField(5);

    public JLabel     meanLb   = new JLabel("Mean : ");
    public JTextField meanTf   = new JTextField(5);
    
	DateAxis domain = new DateAxis("Time");
	NumberAxis range = null;
	
	double minold=Math.pow(2, 25);;
	double maxold=-Math.pow(2, 25);

	double max = maxold;
	double min = minold;

	double maxi = 0;
	double mini = 0;
	int iMin = 0;
	int iMax = 0;
	int iMinOld = 0;
	int iMaxOld = 0;
	double maxiOld = maxold;
	double miniOld = minold;
	double miniPerc = 0;
	double maxiPerc = 0;
	
	int timer = 0;
	TimeSeries mav = null;
	TimeSeries mavCalc = null;
	double value = 0;
	Millisecond milli =null;
	double[] meanArray = null;
	int meanArrayIndex = 0;
	int averagePeriod = 20;
	int averagePeriodSamples = 400;
	
	LinkedList<ValueMarker> markersMax = new LinkedList();
	LinkedList<ValueMarker> markersMin = new LinkedList();
	
	
	
	/**
	 * Creates a new application.
	 * 
	 * @param maxAge
	 *            the maximum age (in milliseconds).
	 */
	public DisplayChannelPOS(DataChannel channel, int maxAge) {

		//super();
		
		dc = channel; 
		averagePeriodSamples = averagePeriod * dc.sampleRate;
		meanArray = new double[averagePeriodSamples];  // 20s average

		chan = new TimeSeries(channel.name, Millisecond.class);
		chan.setMaximumItemAge(maxAge);
		
		mavCalc = new TimeSeries("20s average Calc" , Millisecond.class);
		mavCalc.setMaximumItemAge(20000);
		
		mav = new TimeSeries("20s average" , Millisecond.class);
		mav.setMaximumItemAge(200000);
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(chan);
		dataset.addSeries(mav);

		//domain.setAutoRange(true);
		domain.setFixedAutoRange(30000);
		
		range = new NumberAxis(channel.name);
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);
		
		renderer = new SamplingXYLineRenderer();
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.blue);
		renderer.setSeriesStroke(0, new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		
		
		plot = new XYPlot(dataset, domain, range, renderer);
		plot.setBackgroundAlpha(0.5f);
		plot.setBackgroundPaint(null);
				
		chart = new JFreeChart(channel.name, new Font("SansSerif", Font.BOLD, 16), plot, true);
		chart.removeLegend();
		chart.setBackgroundImageAlpha(0.0f);
		chart.setBackgroundPaint(null);
		chart.setBackgroundImage(null);
		chart.setAntiAlias(false);		
		
		chartPanel = new ChartPanel(chart, true);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(4, 4, 4, 4), BorderFactory
				.createLineBorder(Color.black)));
		chartPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"RealTime"));


		zoomInBtn.addActionListener(this);
		zoomOutBtn.addActionListener(this);
		clearBtn.addActionListener(this);
		range5mBtn.addActionListener(this);
		range30sBtn.addActionListener(this);
		range20mBtn.addActionListener(this);

		toolsPanel.setLayout(new FlowLayout());
		toolsPanel.add(range30sBtn);
		toolsPanel.add(range5mBtn);
		toolsPanel.add(range20mBtn);
		toolsPanel.add(zoomInBtn);
		toolsPanel.add(zoomOutBtn);
		toolsPanel.add(clearBtn);
        toolsPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Options"));
        
        maxPanel.setLayout(new BoxLayout(maxPanel, BoxLayout.Y_AXIS));
        maxTf.setEditable(false);
        maxTf.setBorder(BorderFactory.createEmptyBorder());      
        maxPercTf.setEditable(false);
        maxPercTf.setFont(new Font("SansSerif",	Font.BOLD, 16));
        maxPercTf.setBorder(BorderFactory.createEmptyBorder());
        maxPanel.add(maxPercTf);
        maxPanel.add(maxTf);
        maxPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Max"));
        
        minPanel.setLayout(new BoxLayout(minPanel, BoxLayout.Y_AXIS));
        minTf.setEditable(false);
        minTf.setBorder(BorderFactory.createEmptyBorder());
        minPercTf.setEditable(false);
        minPercTf.setFont(new Font("SansSerif",	Font.BOLD, 16));
        minPercTf.setBorder(BorderFactory.createEmptyBorder());
        minPanel.add(minPercTf);
        minPanel.add(minTf);
        minPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Min"));
        
        meanPanel.setLayout(new BoxLayout(meanPanel, BoxLayout.Y_AXIS));
        meanTf.setEditable(false);
        meanTf.setBorder(BorderFactory.createEmptyBorder());
        meanTf.setFont(new Font("SansSerif",	Font.BOLD, 16));
        meanPanel.add(meanTf);
        meanPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Mean"));
        
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(maxPanel, BorderLayout.NORTH);
        resultPanel.add(minPanel, BorderLayout.SOUTH);
        resultPanel.setOpaque(false);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(chartPanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        //gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;       
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(resultPanel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(meanPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(toolsPanel, gbc);


	}
	
	public void clear() {
		chan.clear();
		mav.clear();
		mavCalc.clear();
		plot.clearDomainMarkers();
		minold=(int)Math.pow(2, 25);;
		maxold=-(int)Math.pow(2, 25);

		max = maxold;
		min = minold;

		maxi = 0;
		mini = 0;
		iMin = 0;
		iMax = 0;
		iMinOld = 0;
		iMaxOld = 0;
		maxiOld = maxold;
		miniOld = minold;
		miniPerc = 0;
		maxiPerc = 0;

	}

	public void add(Millisecond ms, double y) {
		chan.add(ms, y, true);
	}


	public void update(Observable arg0, Object arg1) {

		if (this.dc.channelOn) {

			Date now = new Date();
			int sr = dc.sampleRate;
			int offset = dc.index - sr;
			double sample;
			long ms = 0;
			int startoffset = 0;
			int end = dc.index;
			double tempValue = 0;

			chan.setNotify(false);
			mav.setNotify(false);
			if (offset >= 0) {
				for (int k = 0; k < sr; k++) {
					ms = dc.times[offset + k];
					now.setTime(ms);
					sample=dc.samples[offset + k];
					min=Math.min(min, sample);
					max=Math.max(max, sample);
					milli = new Millisecond(now);
					chan.addOrUpdate(milli, (double) sample);

					meanArray[meanArrayIndex] = (double) sample;								
					if (end > averagePeriodSamples+1) {
						value += meanArray[meanArrayIndex];
						if (meanArrayIndex == meanArray.length-1) {
							value -= meanArray[0];
						} else {
							value -= meanArray[meanArrayIndex+1];	
						}		
						tempValue = value / ((double) averagePeriodSamples );					
						mav.addOrUpdate(milli, tempValue);
						tempValue = (tempValue / 16777216.) * 40.;
						String s = String.format( "%.3f V", tempValue );
						meanTf.setText(s);
					} else {				
						value = 0;					
						for (int i = 0; i < meanArray.length; i++) {
							value += meanArray[i];
							//System.out.println("MeanArray length = " + meanArray.length);
						}
						//System.out.println("value = " + (value/ (double) averagePeriodSamples));
						//value = value / (double) averagePeriodSamples;
					}

					if (meanArrayIndex >= averagePeriodSamples-1) {
						meanArrayIndex = 0;
					} else {
						meanArrayIndex++;
					}



				}	

				chan.setNotify(true);
				mav.setNotify(true);

				chan.fireSeriesChanged();
				mav.fireSeriesChanged();


			}

			int lenght = sr*10;	

			if (end > lenght) {
				startoffset = end - lenght;
				maxi = dc.samples[startoffset];
				iMin = startoffset;
				iMax = startoffset;
				mini = dc.samples[startoffset];
				for (int i = startoffset; i<end; i++) {
					if (dc.samples[i] > maxi) {
						maxi = dc.samples[i];
						iMax = i;
					} else if (dc.samples[i] < mini) {
						mini = dc.samples[i];
						iMin = i;
					}
				}
			}

			if ((iMin != end - 1) && (iMin != startoffset) && (maxiOld != miniOld) && (iMin != iMinOld)) {			
				miniPerc = (double)(maxiOld - mini) / (double)(maxiOld - miniOld) * 100. - 100.;
				addMinMarker(mini, iMin, miniPerc);
				miniOld = mini;
				iMinOld = iMin;
			}

			if ((iMax != end - 1) && (iMax != startoffset) && (maxiOld != miniOld) && (iMax != iMaxOld)) {
				maxiPerc = (double)(maxi - miniOld) / (double)(maxiOld - miniOld) * 100. - 100.;
				addMaxMarker(maxi, iMax, maxiPerc);
				maxiOld = maxi;
				iMaxOld = iMax;
			}

		}
		
	}
	
	
	
	public void addMaxMarker(double value, int index, double percent) {
		Date now = new Date();
		now.setTime(dc.times[index]);
		double millis = new Millisecond(now).getMiddleMillisecond();
		ValueMarker current = new ValueMarker(millis);		
		markersMax.add(current);
		String s = String.format( "%.2f %%", percent );
		current.setLabel(s+"%");
		current.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
		current.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        current.setLabelPaint(Color.blue);
        current.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		plot.addDomainMarker(current);
		if (markersMax.size() >= 10) { 
			plot.removeDomainMarker((Marker)markersMax.getFirst());
			markersMax.removeFirst();
			}
		maxPercTf.setText(s);
		double tempValue = (value / 16777216.) * 40.;
		s = String.format( "%.2f V", tempValue );		
		maxTf.setText(s);
		
	}
	
	public void addMinMarker(double value, int index, double percent)  {
		Date now = new Date();
		now.setTime(dc.times[index]);
		double millis = new Millisecond(now).getMiddleMillisecond();
		ValueMarker current = new ValueMarker(millis);
		markersMin.add(current);
		String s = String.format( "%.2f %%", percent );
		current.setLabel(s+"%");
		current.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
		current.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        current.setLabelPaint(Color.magenta);
        current.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		plot.addDomainMarker(current);
		if (markersMin.size() >= 10) { 
			plot.removeDomainMarker((Marker)markersMin.getFirst());
			markersMin.removeFirst();
			}
		
		minPercTf.setText(s);
		double tempValue = (value / 16777216.) * 40.;
		s = String.format( "%.2f V", tempValue );		
		minTf.setText(s);
		
	}
	
	public void setMaxAge(long age) {
		chan.setMaximumItemAge(age);		
	}
	public long getMaxAge() {
		return chan.getMaximumItemAge();
	}
	

	public void actionPerformed(ActionEvent e) {
	    if (e.getActionCommand().equals("Zoom In")) {
	        System.out.println("Zoom In");
	        domain.setFixedAutoRange(domain.getFixedAutoRange() / 1.5);
	      }
	    if (e.getActionCommand().equals("Zoom Out")) {
	        System.out.println("Zoom Out");
	        domain.setFixedAutoRange(domain.getFixedAutoRange() * 1.5);
	      }
	    if (e.getActionCommand().equals("Clear")) {
	        System.out.println("Clear");
	        chan.clear();
	        mav.clear();
	      }
	    if (e.getActionCommand().equals("5 Min")) {
	        System.out.println("5 Min");
	        domain.setFixedAutoRange(300000.);
	      }
	    if (e.getActionCommand().equals("20 Min")) {
	        System.out.println("20 Min");
	        domain.setFixedAutoRange(1200000.);
	      }
	    if (e.getActionCommand().equals("30 s")) {
	        System.out.println("30 s");
	        domain.setFixedAutoRange(30000);	      
	      }
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		DataChannel bmz = new DataChannel("BMZ", 20);
        frame.add(new DisplayChannelPOS(bmz, 5000));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

}

