package vizardous.delegate.impl.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import au.com.bytecode.opencsv.CSVWriter;
import vizardous.delegate.impl.MainView;
import vizardous.delegate.impl.table.CellsInformationTable;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Clade;
import vizardous.model.impl.Forest;

/**
 * This class serves as an abstraction point for charts that work with selected
 * cells (aka traces). Traces are defined by a single cell (the endpoint of a
 * trace) and the values of a property, e.g. fluorescence, that are encountered
 * when iterating the tree.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public abstract class TraceChart2D extends AbstractChart2D implements TableModelListener {

	private static final long serialVersionUID = 1L;
	
	/** A list of {@link Clade}s that denote the endpoints of traces. */
    protected List<Clade> selectedClades = null;
	
    /** A {@link XYLineAndShapeRenderer} that is used for rendering the plot. */
    protected XYLineAndShapeRenderer renderer;
    
	/**
	 * Constructor.
	 * 
	 * @param forest
	 *            The forest from which the predecessors of the selected cells
	 *            are extracted by iteration to the root.
	 * @param selectedClades
	 *            The last clades (endpoints) of the traces.
	 */
    @Deprecated
    public TraceChart2D (Forest forest, List<Clade> selectedClades) {
    	super(forest);
    	
    	this.selectedClades = selectedClades;
    }
    
	/**
	 * Constructor.
	 * 
	 * @param forest
	 *            The forest from which the predecessors of the selected cells
	 *            are extracted by iteration to the root.
	 */
    public TraceChart2D (Forest forest) {
    	super(forest);
    	
    	this.setLayout(new BorderLayout());
		colorPalette = new LinkedList<Color>();
		dataset = createDataset();
		chart = createJFreeChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 250));
		chartPanel.setMouseZoomable(true, false);
		chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214, 214, 214)));
		legendPanel = createLegendPanel(chart);

		this.add(BorderLayout.CENTER, chartPanel);
		this.add(BorderLayout.EAST, legendPanel);
		this.setBackground(Color.white);

		// Add this chart as ActionLister for the click in the PopupMenu
		JPopupMenu popupMenu = chartPanel.getPopupMenu();
		popupMenu.add(dumpDataItem);
		dumpDataItem.addActionListener(this);
		popupMenu.add(saveSVGItem);
		saveSVGItem.addActionListener(this);
    }
    
	/**
	 * Subclasses can overwrite this method if they are controlled by the
	 * content of the {@link CellsInformationTable}. For an example on how to
	 * handle changes see
	 * {@link CellFluorescenceChart2D#tableChanged(TableModelEvent)}.
	 */
	public void tableChanged(TableModelEvent e) { }
	
	/**
	 * This method has to be called when the underlying dataset of a
	 * TraceChart2D has changed and the legend needs to be updated.
	 */
	public void updateLegend() {
		/* Remove outdated legend panel */		 
		this.remove(legendPanel);
		
		/* Add new legend panel */
		legendPanel = createLegendPanel(chart);		
		this.add(BorderLayout.EAST, legendPanel);
	}
	
	/**
	 * Creates an empty {@link DefaultXYDataset} that backs this chart.
	 * Because the charts that are representing traces are controlled by the
	 * {@link CellsInformationTable} this dataset is empty.
	 * 
	 * @return An empty {@link DefaultXYDataset}.
	 */
    public XYDataset createDataset() {              
        return new DefaultXYDataset();
    }
    
	/**
	 * Changes various appearance settings that are common to all the trace
	 * charts.
	 * 
	 * @param chart
	 *            A {@link JFreeChart} for which the common trace style is to be
	 *            applied.
	 * @return A {@link JFreeChart} that has the common trace style.
	 */
	protected JFreeChart styleChart(JFreeChart chart) {
		super.styleChart(chart);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.lightGray);

		renderer = new XYLineAndShapeRenderer();

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRenderer(renderer);

		LegendItemCollection legendItems = plot.getLegendItems();
		for (int i = 0; i < legendItems.getItemCount(); i++) {
			int seriesIndex = legendItems.get(i).getSeriesIndex();
			renderer.setSeriesLinesVisible(seriesIndex, true);
			renderer.setSeriesShapesVisible(seriesIndex, true);
			Color seriesColor = (Color) renderer.lookupSeriesPaint(seriesIndex);
			colorPalette.add(seriesColor);
		}

		// Customize the domain axis
		final ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickMarksVisible(true);
		domainAxis.setAxisLineVisible(true);
		domainAxis.setLowerMargin(0.0);
		
		// Customize the range axis
		final ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(rangeAxis.getStandardTickUnits());

		return chart;
	}
	
	@Override
	public void exportToCSV(File f) {
		/*
		 * Row = Cell ID XX
		 * Column = Timepoint
		 */
		try {
			/* Instantiate the CSVWriter */
			CSVWriter csvWriter = new CSVWriter(new FileWriter(f), ',',	CSVWriter.NO_QUOTE_CHARACTER);

			List<String[]> forExport = new LinkedList<String[]>();

			/* Generate header */
			List<String> header = new ArrayList<String>();
			header.add("Time");
			for (int seriesIndex=0; seriesIndex<this.dataset.getSeriesCount(); seriesIndex++) {
				Comparable seriesKey = this.dataset.getSeriesKey(seriesIndex);
				
				if (seriesKey instanceof String) {					
					header.add((String) seriesKey);
				}
			}
			forExport.add((String[]) header.toArray(new String[header.size()]));

			// Keep track of the timepoints
			Set<Double> timepoints = new TreeSet<Double>();			
			
			/* Gather data */
			Map<Integer, Map<Double, Double>> data = new TreeMap<Integer, Map<Double, Double>>();
			
			for (int seriesIndex=0; seriesIndex<this.dataset.getSeriesCount(); seriesIndex++) {			
				Map<Double, Double> seriesMap = new TreeMap<Double, Double>();
				
				for (int i=0; i<this.dataset.getItemCount(seriesIndex); i++) {				
					Number x = this.dataset.getX(seriesIndex, i);
					Number y = this.dataset.getY(seriesIndex, i);
					
					seriesMap.put(x.doubleValue(), y.doubleValue());
					timepoints.add(x.doubleValue());
				}
				
				data.put(seriesIndex, seriesMap);
			}
				
			/* Convert data */
			for (Double timepoint : timepoints) {
				String[] values = new String[data.size()+1];
				
				/* Add information about timepoint */
				values[0] = timepoint.toString();
				
				/* Add information per timepoint */
				for(int seriesIndex=0; seriesIndex<data.size(); seriesIndex++) {
					Map<Double, Double> seriesData = data.get(seriesIndex);
					Double value = seriesData.get(timepoint);
					
					if (value != null) {
						values[seriesIndex+1] = value.toString(); // Index shift because of time column
					} else {
						values[seriesIndex+1] = ""; // Index shift because of time column
					}
				}
					
				forExport.add(values);
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
        Map<Integer, Map<Double, Double>> data = new TreeMap<Integer, Map<Double, Double>>();
        
        // Keep track of the timepoints
        List<Double> timepoints = new ArrayList<Double>();     	
        Collections.sort(timepoints);
        
        for (int seriesIndex=0; seriesIndex<this.dataset.getSeriesCount(); seriesIndex++) {			
			Map<Double, Double> seriesMap = new TreeMap<Double, Double>();
			
			for (int i=0; i<this.dataset.getItemCount(seriesIndex); i++) {				
				Number x = this.dataset.getX(seriesIndex, i);
				Number y = this.dataset.getY(seriesIndex, i);
				
				seriesMap.put(x.doubleValue(), y.doubleValue());
				timepoints.add(x.doubleValue());
			}
			
			data.put(seriesIndex, seriesMap);
		}
        
        /* Convert data */
        for (int timeIndex=0; timeIndex<timepoints.size(); timeIndex++) {		       	
        	Double timepoint = timepoints.get(timeIndex);
        	
        	/* Add information about timepoint */
			exportValues.add(new Label(0, timeIndex+1, timepoint.toString())); // INDEX SHIFT
			
			/* Add information per timepoint */
			for(int seriesIndex=0; seriesIndex<data.size(); seriesIndex++) {
				Map<Double, Double> seriesData = data.get(seriesIndex);
				Double value = seriesData.get(timepoint);
				
				if (value != null) {
					exportValues.add(new Label(seriesIndex+1, timeIndex+1, value.toString())); // INDEX SHIFT
				} else {
					exportValues.add(new Label(seriesIndex+1, timeIndex+1, "")); // INDEX SHIFT
				}
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
	        excelSheet.addCell(new Label(0, 0, "Time"));
	        for (int seriesIndex=0; seriesIndex<this.dataset.getSeriesCount(); seriesIndex++) {
				Comparable seriesKey = this.dataset.getSeriesKey(seriesIndex);
				
				if (seriesKey instanceof String) {					
					excelSheet.addCell(new Label(seriesIndex+1, 0, (String) seriesKey));
				}
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
	
}
