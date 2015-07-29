package vizardous.delegate.graphics;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Forest;
import vizardous.model.MIFrame;
import vizardous.model.Phylogeny;

/**
 * TODO Documentation
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public final class PopulationGrowthCurveChart2D extends TraceChart2D {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a {@link PopulationGrowthCurveChart2D} from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public PopulationGrowthCurveChart2D(Forest forest) {
        super(forest);
    }

	/**
	 * Creates an XYDataset of cell number over elapsed time.
	 * 
	 * @return an XYDataset with as many series as there are phylogenies
	 */
	public XYDataset createDataset() {
		// The count is used as pseudo-id for a phylogeny
		int count = 1;
		
        DefaultXYDataset ds = new DefaultXYDataset();

		List<Phylogeny> phyloList = forest.getPhyloxml().getPhylogenies();
		for (Phylogeny phylo : phyloList) {
			Clade rootClade = phylo.getRootClade();
			Cell rootCell = rootClade.getCellObject();

			// Gather information for each phylogeny individually
			Map<Double, Double> growthCurve = new TreeMap<Double, Double>();			
			
			Iterator<Cell> iter = rootCell.iterator();
			while (iter.hasNext()) {
				Cell cell = iter.next();

				MIFrame miframe = cell.getMIFrameObject();
				if (miframe != null) {
					double elapsedTime = miframe.getElapsedTime();
					double currentCellNumber = growthCurve.get(elapsedTime) != null ? growthCurve.get(elapsedTime) : 0;
					
					growthCurve.put(elapsedTime, currentCellNumber+1);
				}
			}

			// Create the data series that is needed for the chart
			double[][] data = CellFluorescenceIndividualGenerationsChart2D.convertMapToArray(growthCurve);			
			ds.addSeries("Population-"+count, data);
			
			count++;
		}

		return ds;
	}
    
    /**
     * Creates a Growth rate Of Population chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return The chart.
     */
    @Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
    	String timeUnit = forest.getMetaxml().getAllFrames().get(0).getElapsedTimeUnit();  
    	
    	final JFreeChart chart = ChartFactory.createXYLineChart(
                 "Growth curve of several populations", 
                 String.format("Elapsed time [%s]", timeUnit), 
                 "Cell Number [-]", 
                 dataset, 
                 PlotOrientation.VERTICAL, 
                 true, 
                 true, 
                 false);
        
    	this.styleChart(chart);
            
        return chart;
    }

}
