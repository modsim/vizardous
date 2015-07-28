/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.delegate.dataFilter.ComparableFilter;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Forest;

/**
 * TODO Documentation
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de
 * @version 1.0
 * 
 */
public class CellLengthDistributionChart2D extends DistributionChart2D {

	private static final long serialVersionUID = 1L;

	/** The clades list. */
    private List<Clade> cladesList = null;

	/**
	 * Constructs a {@link CellLengthDistributionChart2D} from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
    public CellLengthDistributionChart2D(Forest forest) {
    	this(forest.getAllCellsInMetaXML());
    }
    
	/**
	 * Constructs a {@link CellLengthDistributionChart2D} from a list of
	 * {@link Cell}s.
	 * 
	 * @param cells
	 *            The underlying cells for this chart
	 */
    public CellLengthDistributionChart2D(Collection<Cell> cells) {
        super(cells);
    	
        this.tabName = "cell length distribution";
        
    	if( cells != null ) {
            this.setLayout(new BorderLayout());
            this.histogramDataset = createCellLengthHistogramDataset(cells);
            this.chart = createCellLengthHistogram(this.histogramDataset);
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
            dumpDataItem = new JMenuItem("Dump data to file");
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
    private HistogramDataset createCellLengthHistogramDataset(Collection<Cell> cells) {
        final HistogramDataset dataset = new HistogramDataset();
        
        Collection<Double> values = new LinkedList<Double>();
 		for(Cell cell : cells) {
            Double lengthValue = cell.getLength();
 			
            if (lengthValue == null) {
            	lengthValue = Cell.noLength;
            }
            
            values.add(lengthValue);
        }
        
 		double[] datasetValues = ArrayUtils.toPrimitive(values.toArray(new Double[values.size()]));

        // Filter data
 		datasetValues = ComparableFilter.filter(datasetValues, Cell.noLength);
        
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
    private JFreeChart createCellLengthHistogram(HistogramDataset dataset) {
    	String lengthUnit = "μm"; // Set default value
		
		if (forest != null) {
			lengthUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getLengthUnit();
		}
    	
    	final JFreeChart chart = ChartFactory.createHistogram(
                "Cell length distribution",
                String.format("Lenght [%s]", lengthUnit),
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
