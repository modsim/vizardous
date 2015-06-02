package vizardous.delegate.impl.graphics;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import vizardous.model.impl.Cell;
import vizardous.model.impl.Clade;
import vizardous.model.impl.Forest;
import vizardous.model.impl.Node;
import vizardous.model.impl.Phylogeny;
import vizardous.model.impl.Trace;
import vizardous.model.impl.Traces;

/**
 * TODO Documentation
 * 
 * @author Christopher Probst <c.probst@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellElongationRateSistesCorrelation extends CorrelationChart2D {
	
	private static final long serialVersionUID = 1L;
	
    /**
	 * Constructs a CellElongationRateSistesCorrelation from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public CellElongationRateSistesCorrelation(Forest forest) {
		super(forest);
	}
	
	@Override
	public XYDataset createDataset(Forest forest) {
		DefaultXYDataset dataSet = new DefaultXYDataset();
		List<Phylogeny> phyloList = forest.getPhyloxml().getPhylogenies();
		int populationIndex = 1;
		for( Phylogeny phylo: phyloList ) {
			List<Clade> cladesList = phylo.getAllCladeInPhylogeny();
            List<Cell> cellsInPhylogeny = forest.getCellsListFromCladesList(cladesList);
            
            /*
             * Traces
             */
            
            Map<Integer, List<Cell>> traces = new HashMap<Integer, List<Cell>>();
            List<Traces> tracesGeneration = new LinkedList<Traces>();
            
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
            	
            	//System.out.print("\n");
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
            
//            if(elongationRatesSisterOne.size() < elongationRatesSisterTwo.size()) {
//            	for(int k = 0; k < elongationRatesSisterOne.size(); k++) {
//            		dataSet.addValue(elongationRatesSisterOne.get(k), "Population-"+Integer.toString(populationIndex), elongationRatesSisterTwo.get(k));
//            	}
//            } else {
//            	for(int k = 0; k < elongationRatesSisterTwo.size(); k++) {
//            		dataSet.addValue(elongationRatesSisterOne.get(k), "Population-"+Integer.toString(populationIndex), elongationRatesSisterTwo.get(k));
//            	}
//            }
            
			// Jump to next tree
            populationIndex++;
		}
		
		
		return dataSet;
	}

	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		final JFreeChart chart = ChartFactory.createXYLineChart(
                "Growth curve of several populations", 
                "Time t [min]", 
                "Cell Number N", 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false);
       
           styleChart(chart);
          
           return chart;
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

}