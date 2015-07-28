package vizardous.delegate.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Forest;
import vizardous.model.Node;
import vizardous.model.Phylogeny;
import vizardous.model.Trace;
import vizardous.model.Traces;

/**
 * TODO Documentation
 * 
 * @author Christopher Probst <c.probst@fz-juelich.de>
 */
public class PopulationCellLengthBeforeAfterDivisionDistribution extends DistributionChart2D {

	private static final long serialVersionUID = 1L;
		
    /**
     * TODO Documentation
     * 
     * @param forest
     */
	public PopulationCellLengthBeforeAfterDivisionDistribution(Forest forest) {
		super(forest);
		
		this.tabName = "cell length before and after";
		
		if(forest != null) {
			this.setLayout(new BorderLayout());
            colorPalette = new LinkedList<Color>();
            this.histogramDataset = createPopCellLengthBeforAfterDivisionDataset(forest);
            this.chart = createPopCellLengthBeforAfterDivisionChart(this.histogramDataset);
			final ChartPanel chartPanel = new ChartPanel(this.chart);
            chartPanel.setPreferredSize(new Dimension(800, 250));
            chartPanel.setFillZoomRectangle(true);
            
            chartPanel.setMouseZoomable(true, false);
            chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214, 214, 214)));
            legendPanel = createLegendPanel(this.chart);

            this.add(BorderLayout.CENTER, chartPanel);
            this.add(BorderLayout.EAST, legendPanel);
            this.setBackground(Color.white);

            // Add this chart as ActionLister for the click in the PopupMenu
            JPopupMenu popupMenu = chartPanel.getPopupMenu();
            popupMenu.add(dumpDataItem);
            dumpDataItem.addActionListener(this);
            popupMenu.add(saveSVGItem);
            saveSVGItem.addActionListener(this);
		} else {
			throw new IllegalArgumentException("Error");
		}	
	}
	
	/**
	 * TODO Documentation
	 * 
	 * @param forest
	 * @return
	 */
	private HistogramDataset createPopCellLengthBeforAfterDivisionDataset(Forest forest) {
		 final HistogramDataset histogramDataset = new HistogramDataset();
		 
		 int populationIndex = 1;
		 List<Phylogeny> phyloList = forest.getPhyloxml().getPhylogenies();
		 for( Phylogeny phylo: phyloList ) {
			 
			 List<Clade> cladesList = phylo.getAllCladeInPhylogeny();
             List<Cell> cellsInPhylogeny = forest.getCellsListFromCladesList(cladesList);
			 
			 
			 /*
              * Traces
              */
             
             Map<Integer, List<Cell>> traces = new HashMap<Integer, List<Cell>>();
             List<Traces> tracesGeneration = new LinkedList<Traces>();
             
           
             /*
              * Create data set from cells and their predecessors.
              */

             int t = 0;
             
             for (Cell cell : cellsInPhylogeny) {
                 List<Cell> parentsCellList = forest.getPredecessors(cell.getId());
                 // Iterate in reverse order
                 Iterator<Cell> predecessorIterator = parentsCellList.iterator();
                 
                 /* 
                  * Trace of multiple parent node cells
                  */
                 List<Cell> trace = new LinkedList<Cell>();
                 
                 
                 if(cell.isLeaf()) {
                     
                 	while (predecessorIterator.hasNext()) {
                 		Cell predecessor = predecessorIterator.next();
                 		
                 		if (predecessor.getParentCell() != null && predecessor.getParentCell().getChildren().size() <= 1)
                             continue;
                 		
                 		trace.add(predecessor);
                 	}
                 	
                 	// Add trace to collection of traces
                 	traces.put(t, trace);

                 	t++;
                 }
             }
             
             for(int j = 0; j < traces.size(); j++) {
             	
             	Node lastCell = null;
             	Node firstCell;
             	
             	List<Cell> trace_ = traces.get(j);
             	Iterator<Cell> traceIterator = trace_.iterator();
             	List<Trace> tracesCollection = new LinkedList<Trace>();

             	int generation = trace_.size();
             	
             	while(traceIterator.hasNext()) {
             		Cell c = traceIterator.next();
             		if(lastCell == null) {
             			lastCell = new Node(generation,c);
             			generation--;
             			continue;
             		}
             	
             		List<Node> tempTrace = new LinkedList<Node>();
             	
             		firstCell = new Node(generation,c);
             		
             		
             	
             		tempTrace.add(firstCell);
             		tempTrace.add(lastCell);
             		Trace tempTraces = new Trace(tempTrace);
             		tracesCollection.add(tempTraces);
             		//logger.info(Double.toString(tempTraces.getDivisionTime())+"from traces"+firstCell.getId()+","+lastCell.getId()+"\n");
             	
             		lastCell = new Node(generation,c);
             		generation--;
             	}
             	
             	//logger.info("\n");
             	Traces tempTraces = new Traces(tracesCollection);
             	tracesGeneration.add(tempTraces);
             }
             
             /*
              *  Determination of cell length before and after division
              */
             
             List<Cell> cellsBeforeDivision = new LinkedList<Cell>();
             List<Cell> cellsAfterDivision = new LinkedList<Cell>();
             
             List<Double> elapsedTimeBeforeDivision = new LinkedList<Double>();
             List<Double> elapsedTimeAfterDivision = new LinkedList<Double>();
             List<Double> cellLengthsBeforeDivision = new LinkedList<Double>();
             List<Double> cellLengthsAfterDivision = new LinkedList<Double>();
             
             List<Double> cellAreasBeforeDivision = new LinkedList<Double>();
             List<Double> cellAreasAfterDivision = new LinkedList<Double>();
             

             for(int k = 0; k < tracesGeneration.size(); k++) {
             	List<Trace> traces_ = tracesGeneration.get(k).getTraces();
             	
             	for(Trace t_ : traces_) {
             		
             		if(cellsBeforeDivision.isEmpty()) {
             				
             			cellLengthsBeforeDivision.add(t_.getChildNode().getCellLengthBeforeDivision());
             			cellLengthsAfterDivision.add(t_.getChildNode().getCellLengthAfterDivision());
             				
             			cellAreasBeforeDivision.add(t_.getChildNode().getCellAreaBeforeDivision());
             			cellAreasAfterDivision.add(t_.getChildNode().getCellAreaAfterDivision());
             			elapsedTimeAfterDivision.add(t_.getChildNode().getCell().getMIFrameObject().getElapsedTime());
             			elapsedTimeBeforeDivision.add(t_.getChildNode().getCellBeforeDivision().getMIFrameObject().getElapsedTime());
             				
             			cellsBeforeDivision.add(t_.getChildNode().getCellBeforeDivision());
             			cellsAfterDivision.add(t_.getChildNode().getCell());
             			continue;
             		} else {
             			if(!cellsBeforeDivision.contains(t_.getChildNode().getCellBeforeDivision())) {
             					
             				cellLengthsBeforeDivision.add(t_.getChildNode().getCellLengthBeforeDivision());
             				cellAreasBeforeDivision.add(t_.getChildNode().getCellAreaBeforeDivision());
             					
             				cellsBeforeDivision.add(t_.getChildNode().getCellBeforeDivision());
                 			elapsedTimeBeforeDivision.add(t_.getChildNode().getCellBeforeDivision().getMIFrameObject().getElapsedTime());
             			}
             				
             			if(!cellsAfterDivision.contains(t_.getChildNode().getCell())) {
             					
             				cellLengthsAfterDivision.add(t_.getChildNode().getCellLengthAfterDivision());
             				cellAreasAfterDivision.add(t_.getChildNode().getCellAreaAfterDivision());
             					
             				cellsAfterDivision.add(t_.getChildNode().getCell());
                 			elapsedTimeAfterDivision.add(t_.getChildNode().getCell().getMIFrameObject().getElapsedTime());
             			}
             		}
             	}
             	
             }
             
             // TODO Integrate filters
             
             if (cellLengthsBeforeDivision.size() > 0) {
            	 String seriesKey = String.format("Cell length before division (%s)", populationIndex);
            	 
            	 int binNumber = (int) Math.floor(Math.sqrt(cellLengthsBeforeDivision.size())); // #bins = sqrt(N)
            	 histogramDataset.addSeries(seriesKey, listToDouble(cellLengthsBeforeDivision), binNumber);
            	 
            	 Comparable seriesIndex = histogramDataset.indexOf(seriesKey);           	 
            	 getData().put(seriesIndex, cellLengthsBeforeDivision);
             }
             
             if (cellLengthsAfterDivision.size() > 0) {
            	 String seriesKey = String.format("Cell length after division (%s)", populationIndex);            	 
            	 
            	 int binNumber = (int) Math.floor(Math.sqrt(cellLengthsAfterDivision.size())); // #bins = sqrt(N)
            	 histogramDataset.addSeries(seriesKey, listToDouble(cellLengthsAfterDivision), binNumber);
            	 
            	 Comparable seriesIndex = histogramDataset.indexOf(seriesKey); 
            	 getData().put(seriesIndex, cellLengthsAfterDivision);
             }
             
             // Jump to next tree
             populationIndex++;
		 }
		 return histogramDataset;
	}
	
	/**
	 * TODO Documentation
	 * TODO Refactor to fit DistributionChart2D
	 * 
	 * @param dataset
	 * @return
	 */
	private JFreeChart createPopCellLengthBeforAfterDivisionChart(IntervalXYDataset dataset) {
		String lengthUnit = "μm"; // Set default value
		
		if (forest != null) {
			lengthUnit = forest.getMetaxml().getAllCellsInMetaXML().get(0).getLengthUnit();
		}
		
		final JFreeChart chartLocal = ChartFactory.createHistogram(
                "Cell length before and after division",
                String.format("Cell length [%s]", lengthUnit),
                "Distribution",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
         );

		styleChart(chartLocal);
               
        return chartLocal;
	}
	
	/**
	 * TODO Documentation
	 * 
	 * @param list
	 * @return
	 */
	private double[] listToDouble(List<Double> list) {
		return ArrayUtils.toPrimitive(list.toArray(new Double[list.size()]));
	}

	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

}