package vizardous.delegate.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;

import org.apache.commons.math3.stat.regression.SimpleRegression;
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
public class PopulationElongationrateDistributionChart2D extends DistributionChart2D {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a {@link PopulationElongationrateDistributionChart2D} from a
	 * {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public PopulationElongationrateDistributionChart2D(Forest forest) {
		super(forest);
		
		if(forest != null) {
			this.setLayout(new BorderLayout());
            colorPalette = new LinkedList<Color>();
            final IntervalXYDataset dataset = createPopElongationrateDataset(forest);
            final JFreeChart chart = createPopElongationrateChart(dataset);
            final ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 250));
            chartPanel.setFillZoomRectangle(true);
            
            chartPanel.setMouseZoomable(true, false);
            chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214, 214, 214)));
            legendPanel = createLegendPanel(chart);

            this.add(BorderLayout.CENTER, chartPanel);
            this.add(BorderLayout.EAST, legendPanel);
            this.setBackground(Color.white);
            
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
	private HistogramDataset createPopElongationrateDataset(Forest forest) {
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
             

             double elapsedtime = cellsInPhylogeny.get(1).getMIFrameObject().getElapsedTime() ;
             
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
             		lastCell = new Node(generation,c);
             		generation--;
             	}
             	
             	//logger.info("\n");
             	Traces tempTraces = new Traces(tracesCollection);
             	tracesGeneration.add(tempTraces);
             }
             
             /*
              * Elongation rate [um/min]
              */
             
             logger.info("Elongation rate \n");
             
             
             //List of checked parent cells
             List<Double[]> cellLengths = new LinkedList<Double[]>();
             List<Double[]> cellLengthsTime = new LinkedList<Double[]>();
             List<Double> elongationRates = new LinkedList<Double>();
             
             Map<Integer, List<Cell>> cellsElongation = new HashMap<Integer, List<Cell>>();
             List<Integer> cellsElongationGeneration = new LinkedList<Integer>();
             
             int n = 0;
             for(int k = 0; k < tracesGeneration.size(); k++) {
             	Traces traces2 = tracesGeneration.get(k);
             	List<Trace> traces_ = traces2.getTraces();
             	for(Trace t_ : traces_) {
             		
             		List<Cell> cells = new LinkedList<Cell>();
             		
             		if(cellsElongation.isEmpty()) {
             			Cell parentCell = t_.getParentNode().getCell();
                 		Iterator<Cell> childrenIterator = parentCell.iterator();
                 		Cell lastCell = t_.getChildNode().getCellBeforeDivision();
                 		while(childrenIterator.hasNext()) {
                 			Cell nextCell = childrenIterator.next();
                 			if(nextCell == lastCell) break;
                 			cells.add(nextCell);
                 		}
                 		cells.add(lastCell);
                 		cellsElongation.put(n, cells);
                 		cellsElongationGeneration.add(t_.getParentNode().getGeneration());
                 		n++;
                 		
             		} else {
                    		Cell parentCell = t_.getParentNode().getCell();
                     	Iterator<Cell> childrenIterator = parentCell.iterator();
                     	Cell lastCell = t_.getChildNode().getCellBeforeDivision();
                     	while(childrenIterator.hasNext()) {
                     		Cell nextCell = childrenIterator.next();
                     		if(nextCell == lastCell) break;
                     			cells.add(nextCell);
                     		}
                     		cells.add(lastCell);
                     		if(cellsElongation.containsValue(cells)) {
                     			continue;
                     		}

                     		cellsElongation.put(n, cells);
                     		cellsElongationGeneration.add(t_.getParentNode().getGeneration());
                     		n++;
             
             		}
             	}
             }                    
             
             Map<Integer, List<Cell>> cellsSisterOne = new HashMap<Integer, List<Cell>>();
             Map<Integer, List<Cell>> cellsSisterTwo = new HashMap<Integer, List<Cell>>();
             List<Double> elongationRatesSisterOne = new LinkedList<Double>();
             List<Double> elongationRatesSisterTwo = new LinkedList<Double>();
             List<Double> elongationRatesMotherCellLength = new LinkedList<Double>();
             
             
             for(int m = 0; m < cellsElongation.size(); m++) {
             	SimpleRegression regression = new SimpleRegression();
             	
             	List<Cell> cells = cellsElongation.get(m);
             	
             	Double lengths[] = new Double[cells.size()];
             	Double time[] = new Double[cells.size()];
             	int i=0;
             	
             	Cell firstCell = cells.get(0);
             	Cell motherCell = firstCell.getParentCell();
             	
             	if(firstCell.getParentCell() != null) {
             		elongationRatesMotherCellLength.add(motherCell.getLength());
             		logger.info("Mother cell id: "+firstCell.getParentCell().getId()+"\n");
             		if(!cellsSisterOne.containsKey(Integer.parseInt(motherCell.getId()))) {
             			cellsSisterOne.put(Integer.parseInt(motherCell.getId()), cells);
             		} else {
             			cellsSisterTwo.put(Integer.parseInt(motherCell.getId()), cells);
             		}
             	}
             	
             	for(Cell c:cells) {
             		
             		lengths[i] = c.getLength();
             		
             		time[i] = c.getMIFrameObject().getElapsedTime();

             		regression.addData(time[i], lengths[i]);
             		logger.info("Length: "+Double.toString(lengths[i])+", time:"+Double.toString(time[i])+" "+c.getId()+"\n");
             		i++;
             	}
             	
             	cellLengths.add(lengths);
             	cellLengthsTime.add(time);
             	elongationRates.add(regression.getSlope());
             	logger.info("Elongation rate: "+regression.getSlope()+"("+cellsElongationGeneration.get(m)+")"+"\n");
             	logger.info("\n");
             }
             
             
             Map<Integer, List<Cell>> treeMap = new TreeMap<Integer,List<Cell>>(cellsSisterOne);
             
             int i=1;

             
             for(List<Cell> value : treeMap.values()) {
            	 Cell firstCell = value.get(0);
            	 elongationRatesSisterOne.add(calculateElongationRate(value));
            	 logger.info(Integer.toString(i)+" First sister: "+firstCell.getId()+"Elongation rate: "+Double.toString(calculateElongationRate(value))+"\n");
            	 i++;
             }
             
             
             treeMap = new TreeMap<Integer,List<Cell>>(cellsSisterTwo);
             i=1;
             for(List<Cell> value : treeMap.values()) {
            	 Cell firstCell = value.get(0);
            	 logger.info(Integer.toString(i)+" Second sister: "+firstCell.getId()+"Elongation rate: "+Double.toString(calculateElongationRate(value))+"\n");
            	 elongationRatesSisterTwo.add(calculateElongationRate(value));
            	 i++;
             }
             
             //exportElongationRatesToCSV(elongationRatesSisterOne, forest.getMetaxml().getProjectName(), populationIndex, 1);
             //exportElongationRatesToCSV(elongationRatesSisterTwo, forest.getMetaxml().getProjectName(), populationIndex, 2);
//             exportElongationRatesSistersToCSV(elongationRatesMotherCellLength, elongationRatesSisterOne, elongationRatesSisterTwo,forest.getMetaxml().getProjectName(), populationIndex);
             if (elongationRates.size() > 0) {
            	 int binNumber =  (int) Math.floor(Math.sqrt(elongationRates.size())); // #bins = sqrt(N)
            	 histogramDataset.addSeries("Elongation rate: ("+ populationIndex+")", listToDouble(elongationRates), binNumber);
             }
             
			// Jump to next tree
             populationIndex++;
		 }
		 return histogramDataset;
	}
	
	/**
	 * TODO Documentation
	 * 
	 * @param cells
	 * @return
	 */
	private Double calculateElongationRate(List<Cell> cells) {
		Double lengths[] = new Double[cells.size()];
     	Double time[] = new Double[cells.size()];
     	int i=0;
     	SimpleRegression regression = new SimpleRegression();
		for(Cell c:cells) {
     		
     		lengths[i] = c.getLength();
     		
     		time[i] = c.getMIFrameObject().getElapsedTime();

     		regression.addData(time[i], lengths[i]);
     		i++;
     	}
		return regression.getSlope();
	}
	
	/**
	 * TODO Documentation
	 * 
	 * @param dataset
	 * @return
	 */
	private JFreeChart createPopElongationrateChart(IntervalXYDataset dataset) {
		final JFreeChart chartLocal = ChartFactory.createHistogram(
                "Cell elongatrion rate",
                "Elongation rate [mu/min]",
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
	 * @param elongationRates
	 * @param name
	 * @param population
	 * @param sister
	 */
    private void exportElongationRatesToCSV(List<Double> elongationRates, String name, int population, int sister) {
    	try {
        	FileWriter handle = new FileWriter("CellElongationRates-"+name+"-"+Integer.toString(population)+"-sister-"+Integer.toString(sister)+".csv");
        	for(int i = 0; i < elongationRates.size(); i++) {
        		handle.append(Double.toString(elongationRates.get(i)));
        		handle.append("\n");
        	}
        	
    		handle.flush();
    		handle.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * TODO Documentation
     * 
     * @param elongationRatesMotherCellLength
     * @param elongationRatesSisterOne
     * @param elongationRatesSisterTwo
     * @param name
     * @param population
     */
    private void exportElongationRatesSistersToCSV(List<Double> elongationRatesMotherCellLength, List<Double> elongationRatesSisterOne, List<Double> elongationRatesSisterTwo, String name, int population) {
    	try {
        	FileWriter handle = new FileWriter("CellElongationRates-Sisters-"+name+"-"+Integer.toString(population)+".csv");

    		if(elongationRatesSisterOne.size() > elongationRatesSisterTwo.size()){
    			for(int i = 0; i < elongationRatesSisterOne.size(); i++) {
    				handle.append(Double.toString(elongationRatesSisterOne.get(i)));
    				handle.append(",");
    				if(i < elongationRatesSisterTwo.size()-1){
    					handle.append(Double.toString(elongationRatesSisterTwo.get(i)));
    				} else {
    					handle.append("");
    				}
    				handle.append(",");
    				handle.append(Double.toString(elongationRatesMotherCellLength.get(i)));
    				handle.append("\n");
    			}
    		} else {
    			for(int i = 0; i < elongationRatesSisterTwo.size(); i++) {
    				
    				if(i < elongationRatesSisterOne.size()-1){
    					handle.append(Double.toString(elongationRatesSisterOne.get(i)));
    				} else {
    					handle.append("");
    				}
    				handle.append(",");
    				handle.append(Double.toString(elongationRatesSisterTwo.get(i)));
    				handle.append(",");
    				handle.append(Double.toString(elongationRatesMotherCellLength.get(i)));
    				handle.append("\n");
    			}
    		}
    		
    		handle.flush();
    		handle.close();
    		
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
	
    /**
     * TODO Documentation
     * 
     * @param list
     * @return
     */
	private double[] listToDouble(List<Double> list) {
		double[] listOfDoubles = new double[list.size()];
		int i=0;
		for(Double d : list) {
			listOfDoubles[i] = d;
			i++;
		}
		return listOfDoubles;
	}

	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

}