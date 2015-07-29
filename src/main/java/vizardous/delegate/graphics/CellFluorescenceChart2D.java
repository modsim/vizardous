package vizardous.delegate.graphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.delegate.dataFilter.ComparableFilter;
import vizardous.delegate.table.CellInformationTableDeleteEvent;
import vizardous.delegate.table.CellsInformationTable;
import vizardous.model.Cell;
import vizardous.model.Forest;
import vizardous.model.MIFrame;

/**
 * This chart shows traces of fluorescence signals over time. Which traces are
 * shown is determined by the selected cells.
 * 
 * When selected cells have common ancestors there will be data duplication in
 * this chart. More precisely, common ancestors will be shown multiple times in
 * case you want to extract individual traces and end up with all the
 * information.
 * 
 * Additionally, this chart is used as a central point for obtaining the color
 * palette for the highlighting of traces in the lineage tree. Hence, the color
 * palette is updated every time this charts palette is changed.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0
 * 
 */
public class CellFluorescenceChart2D extends TraceChart2D {
    
	private static final long serialVersionUID = 1L;
	
	/** The String identifier of the fluorescence channel that is used. */
	protected String fluorescence = null;
	
	/**
	 * Constructs a {@link CellFluorescenceChart2D} from {@link Forest} for the
	 * provided fluorescence.
	 * 
	 * @param forest
	 *            The forest from which the predecessors of the selected cells
	 *            are extracted by iteration to the root.
	 * @param fluorescence
	 *            Am identifier of the fluorescence channel that will be used.
	 */
    public CellFluorescenceChart2D (Forest forest, String fluorescence) {
        super(forest);
    	
        this.fluorescence = fluorescence; 
        this.tabName = this.fluorescence + " traces";
        
		/*
		 * We have to set the correction title after the chart creation. The
		 * title contains null because the superclass constructor calls
		 * createJFreeChart() before the fluorescence field is set.
		 */
        this.chart.setTitle("Single cell traces of "+this.fluorescence);
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
				Map<Double, Double> dataMap = new TreeMap<Double, Double>();

				ArrayList<Cell> parentsCellList = forest.getCellParentsByCellId(cell.getId());
				// TODO Try to use Collectins.reverse() method to order the list of clades
				for (int idx = parentsCellList.size() - 1; idx >= 0; idx--) {
					MIFrame miframe = parentsCellList.get(idx).getMIFrameObject();
					if (miframe != null) {
						double frameElapsedTime = miframe.getElapsedTime();
						double fluorescenceValue = parentsCellList.get(idx).getFluorescences().get(fluorescence);
						
						dataMap.put(frameElapsedTime, fluorescenceValue);
					} else
						throw new IllegalArgumentException("MIFrameObject is null!");
				}
				
				// TODO remove hard coded zero filter (but non fluorescence data is zero at the moment) 
				// Filter data
				ComparableFilter.filter(dataMap, 0.0);
				ComparableFilter.filter(dataMap, Cell.noFluorescence);
				
				if(!dataMap.isEmpty()) {
					double[][] data = CellFluorescenceIndividualGenerationsChart2D.convertMapToArray(dataMap);
					ds.addSeries("Cell ID "+cell.getId(), data);
				}
			}
			
			// TODO Row changed (minor importance, will it ever happen?)
		}
		
		this.updateLegend();
		
		this.updateColorPalette();
	}
	
	/**
	 * Convenience method that computes a series index for a series key.
	 * 
	 * @param seriesKey
	 *            A {@link String} that denotes the key of the series.
	 * @return A seriesIndex.
	 */
	public int getSeriesIndex(String seriesKey) {
		return this.dataset.indexOf(seriesKey);
	}
    
	/**
	 * Updates the {@link AbstractChart2D#colorPalette} when the
	 * {@link CellsInformationTable} has changed.
	 */
	public void updateColorPalette() {
		colorPalette.clear();
		
		LegendItemCollection legendItems = chart.getPlot().getLegendItems();
        for(int i=0; i<legendItems.getItemCount(); i++) {
            int seriesIndex = legendItems.get(i).getSeriesIndex();
            Color seriesColor = (Color) renderer.lookupSeriesPaint(seriesIndex);
            colorPalette.add(seriesColor);
        }
	}
	
}