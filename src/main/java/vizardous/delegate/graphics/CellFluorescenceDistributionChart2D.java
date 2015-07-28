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
import vizardous.model.Constants;
import vizardous.model.Forest;

/**
 * This chart shows a distribution of fluorescence intensities for selected
 * cells or a complete {@link Forest}.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de?
 */
public class CellFluorescenceDistributionChart2D extends DistributionChart2D {
    
	private static final long serialVersionUID = 1L;

	/** The clades list. */
    private List<Clade> cladesList = null;
	
    /** The String identifier of the fluorescence channel that is used. */
	protected String fluorescence = null;
	
	/**
	 * Constructs a distribution of fluorescence intensities from the default
	 * channel for a complete {@link Forest}.
	 * 
	 * @param forest
	 *            a {@link Forest} from which the fluorescence intensities are
	 *            obtained
	 */
    @Deprecated
	public CellFluorescenceDistributionChart2D(Forest forest) {
        this(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS);
    }
    
	/**
	 * Constructs a distribution of fluorescence intensities for a complete
	 * {@link Forest} and the provided fluorescence channel.
	 * 
	 * @param forest
	 *            a {@link Forest} from which the fluorescence intensities are
	 *            obtained
	 * @param channel
	 *            the identifier of the fluorescence channel
	 */
    public CellFluorescenceDistributionChart2D(Forest forest, String channel) {
        this(forest.getAllCellsInMetaXML(), channel);
    }

	/**
	 * Constructs a distribution of fluorescence intensities for a list of
	 * {@link Cell}s and the provided fluorescence channel.
	 * 
	 * @param cells
	 *            a list of {@link Cell}s from which the fluorescence
	 *            intensities are obtained
	 * @param channel
	 *            the identifier of the fluorescence channel
	 */
    public CellFluorescenceDistributionChart2D(Collection<Cell> cells, String channel) {
        super(cells);
        
        this.fluorescence = channel; 
        this.tabName = this.fluorescence + " distribution";
        
    	if( cells != null ) {
            this.setLayout(new BorderLayout());
            this.histogramDataset = createCellFluorescencesHistogramDataset(cells, channel);
            this.chart = createCellFluorescencesHistogram(histogramDataset, channel);
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
         } else
            throw new IllegalArgumentException("Error");
    }

	/**
	 * Creates the {@link HistogramDataset} that backs this fluorescence
	 * intensity distribution chart.
	 * 
	 * Values are filter according to some rules (exclude 0.0 values and ignore
	 * nonexistent fluorescence intensities).
	 * 
	 * @param cells
	 *            a list of {@link Cell}s from which the fluorescence
	 *            intensities are obtained
	 * @param channel
	 *            the identifier of the fluorescence channel
	 * @return a {@link HistogramDataset} that can be used to create a
	 *         {@link JFreeChart}
	 */
    private HistogramDataset createCellFluorescencesHistogramDataset(Collection<Cell> cells, String channel) {
    	logger.debug(String.format("Constructing fluorescence distribution for %s", channel));
    	final HistogramDataset dataset = new HistogramDataset();
            
    	Collection<Double> values = new LinkedList<Double>();
 		for(Cell cell : cells) {
            Double fluorescenceValue = cell.getFluorescences().get(channel);
 			
            if (fluorescenceValue == null) {
            	fluorescenceValue = Cell.noFluorescence;
            }
            
            values.add(fluorescenceValue);
        }
         
 		double[] datasetValues = ArrayUtils.toPrimitive(values.toArray(new Double[values.size()]));
 		
         // TODO remove hard coded zero filter (but non fluorescence data is zero at the moment)
         // filter zeros
         datasetValues = ComparableFilter.filter(datasetValues, 0.0);
         // filter no set fluorescences
         datasetValues = ComparableFilter.filter(datasetValues, Cell.noFluorescence);
         
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
	 * Creates the {@link JFreeChart} that this chart shows.
	 * 
	 * @param dataset
	 *            The {@link HistogramDataset} that backs the created
	 *            {@link JFreeChart}.
	 * @param channel
	 *            the identifier of the fluorescence channel
	 * 
	 * @return A {@link JFreeChart} instance that is shown by this chart.
	 */
    private JFreeChart createCellFluorescencesHistogram(HistogramDataset dataset, String channel) {
    	String fluorescenceUnit = "au"; // Set default value
		
		if (forest != null) {
			fluorescenceUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getFluorescenceUnit();
		}
    	
    	final JFreeChart chart = ChartFactory.createHistogram(
        		channel +"-Fluorescence Distribution",
        		String.format("Fluorescence [%s]", fluorescenceUnit),
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
