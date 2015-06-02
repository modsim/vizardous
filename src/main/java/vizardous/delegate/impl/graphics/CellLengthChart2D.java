/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import vizardous.delegate.impl.dataFilter.ComparableFilter;
import vizardous.delegate.impl.table.CellInformationTableDeleteEvent;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Forest;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * TODO Documentation
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * 
 * @version 1.0
 */
public class CellLengthChart2D extends TraceChart2D {
    
	private static final long serialVersionUID = 1L;

	public CellLengthChart2D(Forest forest) {   	
    	super(forest);
    	
    	this.tabName = "cell length";
    }
    
    /**
     * TODO Documentation
     * 
     * @param ds  a ds.
     * 
     * @return The jfChart.
     */
    @Override
    public JFreeChart createJFreeChart(XYDataset dataset) {
    	String timeUnit = forest.getMetaxml().getAllFrames().get(0).getElapsedTimeUnit();
		String lengthUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getAreaUnit();
    	
    	final JFreeChart chart = ChartFactory.createXYLineChart(
            "Single cell traces of length", // Title
            String.format("Elapsed time [%s]", timeUnit), // domain axis label
			String.format("Lenght [%s]", lengthUnit), // range axis label
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

				List<Cell> parentsCellList = forest.getPredecessors(cell.getId());
				parentsCellList.add(0, cell);
				ListIterator<Cell> predecessorIterator = parentsCellList.listIterator(parentsCellList.size());
				Map<Double, Double> dataMap = new TreeMap<Double, Double>();
				
				while (predecessorIterator.hasPrevious()) {
					Cell predecessor = predecessorIterator.previous();
					double length = predecessor.getLength();
					
					if (predecessor.getMIFrameObject() == null) {
						throw new IllegalArgumentException("MIFrameObject is null!");
					}
					double frameElapsedTime = predecessor.getMIFrameObject().getElapsedTime();
					
					dataMap.put(frameElapsedTime, length);
				}
				
				// Filter data
				ComparableFilter.filter(dataMap, Cell.noLength);
				
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