
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jlib330.DataChannel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.time.Millisecond;
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
public class DisplayChannel extends JPanel implements Observer, ActionListener {

	/** Time series for total memory used. */
	private TimeSeries chan;
	
	private DataChannel dc = null;
	
	private SamplingXYLineRenderer renderer = null;
	private XYPlot plot = null;
	private JFreeChart chart = null;
	private ChartPanel chartPanel = null;
	
    private JPanel toolsPanel = new JPanel();

	public JButton range20mBtn = new JButton("20 Min");
	public JButton range5mBtn = new JButton("5 Min");
	public JButton range30sBtn = new JButton("30 s");
	public JButton zoomInBtn = new JButton("Zoom In");
	public JButton zoomOutBtn = new JButton("Zoom Out");
	public JButton clearBtn = new JButton("Clear");
	
	DateAxis domain = new DateAxis("Time");
	NumberAxis range = null;
	
	double minold=Math.pow(2, 25);;
	double maxold=-Math.pow(2, 25);

	double max = maxold;
	double min = minold;
	
	int timer = 0;
	
	int sr;
	int offset;
	double sample;
	long ms = 0;
	Date now = new Date();
	
	/**
	 * Creates a new application.
	 * 
	 * @param maxAge
	 *            the maximum age (in milliseconds).
	 */
	public DisplayChannel(DataChannel channel, int maxAge) {

		//super();
		
		dc = channel; 
		this.sr = dc.sampleRate;

		// create series that automatically discard data more than 30
		// seconds old...
		chan = new TimeSeries(channel.name, Millisecond.class);
		chan.setMaximumItemAge(maxAge);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(this.chan);

		domain.setAutoRange(true);
		
		range = new NumberAxis(channel.name);
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);
		
		renderer = new SamplingXYLineRenderer();
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesStroke(0, new BasicStroke(1f,
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setBasePaint(null);
		
		plot = new XYPlot(dataset, domain, range, renderer);
		plot.setBackgroundAlpha(0.5f);
		plot.setBackgroundPaint(null);
		
		chart = new JFreeChart(channel.name, new Font("SansSerif", Font.BOLD, 24), plot, true);
		chart.setBackgroundImageAlpha(0.0f);
		chart.setBackgroundImage(null);
		
		chartPanel = new ChartPanel(chart, true);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(4, 4, 4, 4), BorderFactory
				.createLineBorder(Color.black)));
		
		chartPanel.setOpaque(false);		
		chartPanel.setBackground(null);
		chartPanel.setForeground(null);
		
		chart.setAntiAlias(false);

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
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(chartPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(toolsPanel, gbc);
        
	}

	public void clear() {
		chan.clear();
		plot.clearDomainMarkers();
	}

	
	public void add(Millisecond ms, double y) {
		chan.add(ms, y, true);
	}


	public void update(Observable arg0, Object arg1) {
    	long timer = System.currentTimeMillis();
		if (this.dc.channelOn) {
    	//if (false) {
	
			this.offset = dc.index - sr;
			chan.setNotify(false);
			if (offset > 0) {
				for (int k = 0; k < sr; k++) {
					ms = dc.times[offset + k];
					now.setTime(ms);
					sample=dc.samples[offset + k];
					min=Math.min(min, sample);
					max=Math.max(max, sample);
					chan.add(new Millisecond(now), (double) sample, false);		
					//chan.addOrUpdate(new Millisecond(now), (double) sample);				
				}	
				chan.setNotify(true);
				//chan.fireSeriesChanged();
			}

			// A finir !!
			/*domain.setAutoRange(false);
		Date now2 = new Date();
		ms = ms - 300000;
		now2.setTime(ms);
		domain.setRange(now2, now);*/
			if (min<max) {
				if ((max>maxold) && (min<minold)) {
					maxold=max;
					minold=min;
					range.setAutoRange(false);
					range.setRange((double)minold, (double)(maxold));
					//System.out.println("range");
				}else if (max>maxold) {
					maxold=max;
					range.setAutoRange(false);
					range.setRange((double)minold, (double)(maxold));
					//System.out.println("range");
				}else if (min<minold) {
					minold=min;
					range.setAutoRange(false);
					range.setRange((double)minold, (double)(maxold));
					//System.out.println("range");
				}
			}	
		}
    	timer = (System.currentTimeMillis() - timer);
    	System.out.println("ch : " + this.dc.name +" - timer : " + timer);
		
	}
	
	
	
	public void addMarker(String label) {
		Date now = new Date();
		if (dc.index>1) {
			now.setTime(dc.times[dc.index-1]);
			double millis = new Millisecond(now).getMiddleMillisecond();
			Marker current = new ValueMarker(millis);
	        current.setLabel("          "+label);
	        current.setLabelPaint(Color.blue);
	        current.setStroke(new BasicStroke(4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			plot.addDomainMarker(current);
			System.out.println(current.getLabel() + " " + dc.times[dc.index-1]);	
		}
		
	}
	
	public void addMarker(String label, int index) {
		Date now = new Date();
		if (dc.index>1) {
			now.setTime(dc.times[index]);
			double millis = new Millisecond(now).getMiddleMillisecond();
			ValueMarker current = new ValueMarker(millis);
			current.setLabel(label);
	        current.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
	        current.setLabelPaint(Color.blue);
	        current.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			plot.addDomainMarker(current);
			System.out.println(current.getLabel() + " " + dc.times[index]);
		}

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
	

}

