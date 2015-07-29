/**
 * 
 */
package vizardous.delegate.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.LinkedList;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import vizardous.model.Forest;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public abstract class CorrelationChart2D extends AbstractChart2D {

	private static final long serialVersionUID = 1L;

	public CorrelationChart2D(Forest forest) {
		if (forest != null) {
			colorPalette = new LinkedList<Color>();
			this.setLayout(new BorderLayout());
			this.dataset = createDataset(forest);
			chart = createJFreeChart(dataset);
			final ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(800, 250));
			chartPanel.setMouseZoomable(true, false);
			chartPanel.setBorder(BorderFactory
					.createLineBorder(new java.awt.Color(214, 214, 214)));
			legendPanel = createLegendPanel(chart);

			this.add(BorderLayout.CENTER, chartPanel);
			this.add(BorderLayout.EAST, legendPanel);
			this.setBackground(Color.white);

		} else {
			throw new IllegalArgumentException("Error");
		}
	}

	/* (non-Javadoc)
	 * @see vizardous.delegate.impl.graphics.AbstractChart2D#createJFreeChart(org.jfree.data.category.CategoryDataset)
	 */
	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportToCSV(File f) {
		// TODO Implement
		
	}

	@Override
	public void exportToExcel(File f) {
		// TODO Implement
		
	}

	/**
	 * TODO Documentation
	 * 
	 * @param forest
	 * @return
	 */
	protected abstract XYDataset createDataset(final Forest forest);
	
	/** 
	 * TODO Documentation
	 * 
	 * @param chart
	 * @return
	 */
	protected JFreeChart styleChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setVisible(false);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);      

        final LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0,true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setBaseLinesVisible(true);
        renderer.setBaseShapesFilled(true);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRenderer(renderer);
        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setStandardTickUnits(rangeAxis.getStandardTickUnits());
        
        return chart;
	}
}
