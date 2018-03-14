package calc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jlib330.Data;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class CalcResultPanel extends JPanel{
	

	CalcResultPanel (String name, long[] times, double[] data) {		
		//super();
		
		TimeSeries ts = new TimeSeries(name, Millisecond.class);
		Date now = new Date();
		for (int i = 0 ; i < times.length ; i++ ) {
			now.setTime(times[i]);
			ts.addOrUpdate(new Millisecond(now), data[i]);
		}
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(ts);

		DateAxis domain = new DateAxis("Time");
		domain.setAutoRange(true);
		
		NumberAxis range = new NumberAxis(name);
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);
		
		//XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true,false);
		SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesStroke(0, new BasicStroke(1f,
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setBasePaint(null);
		
		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		//plot.setBackgroundPaint(Color.black);
		plot.setBackgroundAlpha(0.5f);
		plot.setBackgroundPaint(null);
		
		
		JFreeChart chart = new JFreeChart(name, new Font("SansSerif",
				Font.BOLD, 24), plot, true);
		chart.setBackgroundImageAlpha(0.0f);
		//chart.setBackgroundPaint(null);
		chart.setBackgroundImage(null);
		
		ChartPanel chartPanel = new ChartPanel(chart, true);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(4, 4, 4, 4), BorderFactory
				.createLineBorder(Color.black)));
		
		chartPanel.setOpaque(false);		
		chartPanel.setBackground(null);
		chartPanel.setForeground(null);
		
		chartPanel.setPreferredSize(new Dimension(640, 480));
		
        
		
		
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(chartPanel, gbc);
		
		
	}


	
}
