/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.delegate.impl.dataFilter.ComparableFilter;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Forest;

/**
 * This chart shows a distribution of cell lengths over time. This can either be
 * done for all cells in a forest or for selected cells, e.g. of one selected
 * frame.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0
 * 
 */
public class CellAreaDistributionChart2D extends DistributionChart2D {
    
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link CellAreaDistributionChart2D} from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
    public CellAreaDistributionChart2D(Forest forest) {
        this(forest.getAllCellsInMetaXML());
    }
    
	/**
	 * Constructs a {@link CellAreaDistributionChart2D} from a list of
	 * {@link Cell}s.
	 * 
	 * @param cells
	 *            The underlying cells for this chart.
	 */
    public CellAreaDistributionChart2D(Collection<Cell> cells) {
        super(cells);
        
        this.tabName = "cell area distribution";
        
    	if( cells != null ) {
            this.setLayout(new BorderLayout());
            this.histogramDataset = createCellAreaHistogramDataset(cells);
            this.chart = createCellAreaHistogram(this.histogramDataset);
            final ChartPanel chartPanelLevelChart = new ChartPanel(this.chart);
            chartPanelLevelChart.setPreferredSize(new Dimension(500, 250));
            chartPanelLevelChart.setMouseZoomable(true, false);
            chartPanelLevelChart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214, 214, 214)));
            legendPanel = createLegendPanel(this.chart);
            legendPanel.setVisible(false);

            this.add(BorderLayout.CENTER, chartPanelLevelChart);
            this.add(BorderLayout.EAST, legendPanel);
            this.setBackground(Color.white);

            // Add this chart as ActionLister for the click in the PopupMenu
            JPopupMenu popupMenu = chartPanelLevelChart.getPopupMenu();
            popupMenu.add(dumpDataItem);
            dumpDataItem.addActionListener(this);
            popupMenu.add(saveSVGItem);
            saveSVGItem.addActionListener(this); 
         } else
            throw new IllegalArgumentException("Error");
    }

	/**
	 * TODO Documentation
	 * 
	 * @param cells
	 * @return
	 */
    private HistogramDataset createCellAreaHistogramDataset(Collection<Cell> cells) {
        final HistogramDataset dataset = new HistogramDataset();
        
        Collection<Double> values = new LinkedList<Double>();
 		for(Cell cell : cells) {
            Double areaValue = cell.getArea();
 			
            if (areaValue == null) {
            	areaValue = Cell.noArea;
            }
            
            values.add(areaValue);
        }
        
 		double[] datasetValues = ArrayUtils.toPrimitive(values.toArray(new Double[values.size()]));
 		
        // Filter values
 		datasetValues = ComparableFilter.filter(datasetValues, Cell.noArea);
        
        if(values.size() > 0) {
        	String seriesKey = "Whole Population";
        	int binNumber = (int) Math.floor(Math.sqrt(datasetValues.length)); // #bins = sqrt(N)
        	dataset.addSeries(seriesKey, datasetValues, binNumber);
        	
        	Comparable seriesIndex = dataset.indexOf(seriesKey); 
        	Collection<Double> datasetValuesList = new ArrayList<Double>(Arrays.asList(ArrayUtils.toObject(datasetValues)));        	
       	 	getData().put(seriesIndex, datasetValuesList);
        }
        
        return dataset;
    }

    /**
     * TODO Documentation
     * 
     * @param dataset
     * @return
     */
    private JFreeChart createCellAreaHistogram(HistogramDataset dataset) {   	
		String areaUnit = "μm^2"; // Set default value
		
		if (forest != null) {
			areaUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getAreaUnit();
		}
    	
    	final JFreeChart chart = ChartFactory.createHistogram(
                "Cell Area Distribution",
                String.format("Area [%s]", areaUnit),
                "Occurrence [-]",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
              
        styleChart(chart);
               
        return chart;
    }

	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

}
