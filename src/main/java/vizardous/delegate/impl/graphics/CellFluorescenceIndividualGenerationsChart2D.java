/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.delegate.impl.dataFilter.ComparableFilter;
import vizardous.delegate.impl.table.CellInformationTableDeleteEvent;
import vizardous.delegate.impl.table.CellsInformationTable;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Forest;
import vizardous.model.impl.MIFrame;

/**
 * This chart shows traces of fluorescences over time. Which traces are shown is
 * determined by the selected cells in {@link CellsInformationTable}.
 * 
 * When selected cells have common ancestors there will not be data duplication
 * in this chart.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellFluorescenceIndividualGenerationsChart2D extends CellFluorescenceChart2D {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link CellFluorescenceIndividualGenerationsChart2D} from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public CellFluorescenceIndividualGenerationsChart2D(Forest forest, String fluorescence) {
		super(forest, fluorescence);
		
		this.chart.setTitle("Single cell traces of "+this.fluorescence+" Individual");
		
		this.tabName = this.fluorescence + " traces individual";
	}
	
	@Override
    public JFreeChart createJFreeChart(XYDataset dataset) {
    	String timeUnit = forest.getMetaxml().getAllFrames().get(0).getElapsedTimeUnit();
		String fluorescenceUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getFluorescenceUnit();
        
    	// create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Single cell traces of "+this.fluorescence,       // chart title
            String.format("Elapsed time [%s]", timeUnit), // domain axis label
			String.format("Fluorescence [%s]", fluorescenceUnit), // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );
        
        this.styleChart(chart);

        return chart;
    }
	
	@Override
	public void tableChanged(TableModelEvent e) {
		if (this.dataset instanceof DefaultXYDataset) {
			DefaultXYDataset ds = (DefaultXYDataset) this.dataset;
			
			// Row removed
			if (e.getType() == TableModelEvent.DELETE) {		
				// Note: CellInformationTableDeleteEvent provides the cellId
				if (e instanceof CellInformationTableDeleteEvent) {
					String cellId = ((CellInformationTableDeleteEvent) e).getCellId();
					String rowKey = "Cell ID " + cellId;
					ds.removeSeries(rowKey);
				}
				
			}

			// Row added
			if (e.getType() == TableModelEvent.INSERT) {
				TableModel tm = null;
				if (e.getSource() instanceof TableModel) {
					tm = (TableModel) e.getSource();
				}
				
				String cellId = (String) tm.getValueAt(e.getFirstRow(), 0);
				Cell cell = this.forest.getCellById(cellId);
				String datasetCellId = "Cell ID " + cell.getId();
				Map<Double, Double> dataMap = new TreeMap<Double, Double>();
				
				Iterator<Cell> rootIter = cell.reverseIterator();
				while (rootIter.hasNext()) {
					Cell parentCell = rootIter.next();
					
					if (datasetCellId != null) {					
						MIFrame miframe = parentCell.getMIFrameObject();
						if (miframe != null) {
							double frameElapsedTime = miframe.getElapsedTime();
							double fluorescenceValue = parentCell.getFluorescences().get(fluorescence);
							
							dataMap.put(frameElapsedTime, fluorescenceValue);
						} else {
							throw new IllegalArgumentException("MIFrameObject is null!");
						}
					}
					
					// TODO remove hard coded zero filter (but hard coded non fluorescence data is zero at the moment)
					// Filter data
					ComparableFilter.filter(dataMap, 0.0);
					ComparableFilter.filter(dataMap, Cell.noFluorescence);
					
					// Update the cell to which the value is assigned
					if (parentCell.hasSiblings() || parentCell.isRoot()) {
						if (datasetCellId != null) {
							if(!dataMap.isEmpty()) {
								double[][] data = convertMapToArray(dataMap);
								ds.addSeries(datasetCellId, data);
							}
						}
						
						// If we are at the root, don't update this anymore
						if (!parentCell.isRoot()) {
							datasetCellId = "Cell ID " + parentCell.getParentCell().getId();
							dataMap.clear();
							
							// Check if data already exists
							if (ds.indexOf(datasetCellId) > 0) {
								datasetCellId = null;
							}
						}
					}
				}	
			}
			
			// TODO Row changed (minor importance, will it ever happen?)
		}
		
		this.updateLegend();
	}
    
	public static double[][] convertMapToArray(Map<Double, Double> map) {
		double[][] array = new double[2][map.size()];
		int i = 0;
		
		for (Entry<Double, Double> entry : map.entrySet()) {
			array[0][i] = entry.getKey();
			array[1][i] = entry.getValue();
			
			i++;
		}
		
		return array;
	}
	
}
