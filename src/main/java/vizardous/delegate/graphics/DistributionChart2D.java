package vizardous.delegate.graphics;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPopupMenu;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import vizardous.model.Cell;
import vizardous.model.Forest;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * This class serves as an abstraction point for charts that generate
 * distributions of whole {@link Forest}s or specific cells.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class DistributionChart2D extends AbstractChart2D {

	private static final long serialVersionUID = 1L;

	/**
	 * A {@link Collection} of cells that backs this distribution chart in the
	 * case that not all cells of a forest are taken into account.
	 */
	private Collection<Cell> cells;

	/** TODO Documentation */
	private Map<Comparable, Collection<? extends Number>> data = new TreeMap<Comparable, Collection<? extends Number>>();
	
    /** TODO Documentation */
    protected HistogramDataset histogramDataset;
    
	/**
	 * Constructs a DistributionChart2D that is based on all cells of a
	 * provided {@link Forest}.
	 * 
	 * @param forest
	 *            The {@link Forest} that backs this distribution chart.
	 */
	public DistributionChart2D(Forest forest) {
		this(forest.getAllCellsInMetaXML());
	}

	/**
	 * Constructs a DistributionChart2D that only takes into account selected
	 * cells, e.g. from one frame of an image sequence.
	 * 
	 * @param cells
	 *            The {@link Collection} that backs this distribution chart.
	 */
	public DistributionChart2D(Collection<Cell> cells) {
		this.cells = cells;
	}

	/**
	 * @return The cells that back this chart.
	 */
	public Collection<Cell> getCells() {
		return cells;
	}
	
	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportToCSV(File f) {
		/*
		 * Row = Series
		 * Columns = Datapoints
		 */
		try {
			/* Instantiate the CSVWriter */
			CSVWriter csvWriter = new CSVWriter(new FileWriter(f), ',',	CSVWriter.NO_QUOTE_CHARACTER);
			List<String[]> forExport = new LinkedList<String[]>();	
			
			/* Gather data */
			Map<Comparable, Collection<? extends Number>> data = getData();
			
			for (int seriesIndex=0; seriesIndex<histogramDataset.getSeriesCount(); seriesIndex++) {			
				Collection<? extends Number> datapoints = data.get(seriesIndex);			
				Collection<String> values = new LinkedList<String>();
				
				/* Add information about series */
				Comparable seriesKey = histogramDataset.getSeriesKey(seriesIndex);
				values.add(seriesKey.toString());
				
				/* Convert data */
				for (Number datapoint : datapoints) {			
					/* Add information per datapoint */
					values.add(datapoint.toString());
				}
				
				forExport.add(values.toArray(new String[values.size()]));
			}
			
			/* Write content to file */
			csvWriter.writeAll(forExport);
			csvWriter.close();
		} catch (IOException e) {
			logger.error("There was a problem with writing the file", e);
		}
	}

	@Override
    public void exportToExcel(File f) {
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        
        /* Gather data */
        Set<WritableCell> exportValues = new HashSet<WritableCell>();
        
        /* Gather data */
		Map<Comparable, Collection<? extends Number>> data = getData();
		
		for (int seriesIndex=0; seriesIndex<histogramDataset.getSeriesCount(); seriesIndex++) {			
			Collection<? extends Number> datapoints = data.get(seriesIndex);			
			
			/* Convert data */
			int datapointIndex = 1; // Index shift because of the row title
			for (Number datapoint : datapoints) {			
				/* Add information per datapoint */			
				exportValues.add(new Label(seriesIndex, datapointIndex, datapoint.toString()));
				datapointIndex++;
			}
		}
            
        /*
         * Export data
         */
        WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(f, wbSettings);
			
			/* Create sheet in workbook */
			String chartTitle = this.chart.getTitle().getText();   			
			WritableSheet excelSheet = workbook.createSheet(chartTitle, 0);
	        
			/* Generate header */
	        for (int seriesIndex=0; seriesIndex<this.histogramDataset.getSeriesCount(); seriesIndex++) {
				Comparable seriesKey = this.histogramDataset.getSeriesKey(seriesIndex);
							
				excelSheet.addCell(new Label(seriesIndex, 0, seriesKey.toString()));
			}  
	        
	        /* Write data */
	        for (WritableCell c : exportValues) {
				excelSheet.addCell(c);
	        }
	        
	        workbook.write();
	        workbook.close();
		} catch (IOException ioException) {
			logger.error("", ioException);
		} catch (WriteException writeException) {
			logger.error("", writeException);
		}
    }

	/**
	 * TODO Documentation
	 * 
	 * @param chart
	 * @return
	 */
	public JFreeChart styleChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setVisible(false);
        
        XYPlot chartPlot = chart.getXYPlot();
        chartPlot.setBackgroundPaint(Color.white);
        chartPlot.setForegroundAlpha(0.7F);

        XYItemRenderer brRenderer = chartPlot.getRenderer();
        chartPlot.setDomainGridlinesVisible(true);
        chartPlot.setDomainGridlinePaint(Color.GRAY);
        chartPlot.setRangeGridlinePaint(Color.GRAY);
        brRenderer.setSeriesPaint(0, new Color(0,0,139));

        // customise the range axis...
        final ValueAxis domainAxis = chartPlot.getDomainAxis();
        domainAxis.setLowerMargin(0.0);
        
        return chart;
	}
	
	/**
	 * @return the data
	 */
	public Map<Comparable, Collection<? extends Number>> getData() {
		return data;
	}
}
