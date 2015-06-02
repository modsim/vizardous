/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.SortOrder;

import vizardous.delegate.impl.dataFilter.ComparableFilter;
import vizardous.delegate.impl.table.CellInformationTableDeleteEvent;
import vizardous.delegate.impl.table.CellsInformationTable;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Forest;
import vizardous.model.impl.MIFrame;

/**
 * This chart shows traces of cell area over time. Which traces are shown is
 * determined by the selected cells in {@link CellsInformationTable}.
 * 
 * When selected cells have common ancestors there will not be data duplication
 * in this chart.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellAreaIndividualGenerationsChart2D extends TraceChart2D {
    
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link CellAreaIndividualGenerationsChart2D} from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public CellAreaIndividualGenerationsChart2D(Forest forest) {
		super(forest);
		
		this.tabName = "cell area individual";
	}
    
	@Override
	public JFreeChart createJFreeChart(final XYDataset dataset) {
		String timeUnit = forest.getMetaxml().getAllFrames().get(0).getElapsedTimeUnit();
		String areaUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getAreaUnit();
		
		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Single cell traces of area per generation", // chart title
				String.format("Elapsed time [%s]", timeUnit), // domain axis label
				String.format("Area [%s]", areaUnit), // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
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
							double areaValue = parentCell.getArea();
							
							dataMap.put(frameElapsedTime, areaValue);
						} else {
							throw new IllegalArgumentException("MIFrameObject is null!");
						}
					}
					
					// Filter data
					ComparableFilter.filter(dataMap, Cell.noArea);

					// Update the cell to which the value is assigned
					if (parentCell.hasSiblings() || parentCell.isRoot()) {
						if (datasetCellId != null) {
							if(!dataMap.isEmpty()) {
								double[][] data = CellFluorescenceIndividualGenerationsChart2D.convertMapToArray(dataMap);
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
				
				// Filter data
				ComparableFilter.filter(dataMap, Cell.noArea);
				
				if(!dataMap.isEmpty()) {
					double[][] data = CellFluorescenceIndividualGenerationsChart2D.convertMapToArray(dataMap);
					ds.addSeries("Cell ID "+cell.getId(), data);
				}
			}
			
			// TODO Row changed (minor importance, will it ever happen?)
		}
		
		this.updateLegend();
	}
    
}
