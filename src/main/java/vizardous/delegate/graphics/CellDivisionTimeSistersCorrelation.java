package vizardous.delegate.graphics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
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
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellDivisionTimeSistersCorrelation extends CorrelationChart2D {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a CellDivisionTimeSistersCorrelation from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public CellDivisionTimeSistersCorrelation(Forest forest) {
		super(forest);
	}
	
	@Override
	protected XYDataset createDataset(Forest forest) {
		// TODO Auto-generated method stub
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
            	
            	//logger.info("\n");
            	Traces tempTraces = new Traces(tracesCollection);
            	tracesGeneration.add(tempTraces);
            }
            
            
            /*
             * Determination of division times
             */
            
            
            
            // List of checked parent cells
            List<Cell> nodes = new LinkedList<Cell>();
            Map<Integer, List<Cell>> cellsSisterOne = new HashMap<Integer, List<Cell>>();
            Map<Integer, List<Cell>> cellsSisterTwo = new HashMap<Integer, List<Cell>>();
            List<Double> divisionTimesSisterOne = new LinkedList<Double>();
            List<Double> divisionTimesSisterTwo = new LinkedList<Double>();
            List<Double> divisionTimesMotherCellLength = new LinkedList<Double>();
            
            logger.info("Divison times: \n");
            
            int n = 0;
            
            Map<Integer, List<Cell>> cellsDivisiontime = new HashMap<Integer, List<Cell>>();
            
            for(int k = 0; k < tracesGeneration.size(); k++) {
            	Traces traces2 = tracesGeneration.get(k);
            	List<Trace> traces_ = traces2.getTraces();
            	for(Trace t_ : traces_) {
            		
            		List<Cell> cells = new LinkedList<Cell>();
            		
            		if(cellsDivisiontime.isEmpty()) {
            			Cell parentCell = t_.getParentNode().getCell();
                		Iterator<Cell> childrenIterator = parentCell.iterator();
                		Cell lastCell = t_.getChildNode().getCellBeforeDivision();
                		while(childrenIterator.hasNext()) {
                			Cell nextCell = childrenIterator.next();
                			if(nextCell == lastCell) break;
                			cells.add(nextCell);
                		}
                		cells.add(lastCell);
                		cellsDivisiontime.put(n, cells);
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
                    		if(cellsDivisiontime.containsValue(cells)) {
                    			continue;
                    		}

                    		cellsDivisiontime.put(n, cells);
                    		n++;
            		}
            	}
            } 
            
           
            
            for(int m = 0; m < cellsDivisiontime.size(); m++) {
            	
            	List<Cell> cells = cellsDivisiontime.get(m);
            	Cell firstCell = cells.get(0);
            	Cell lastCell = cells.get(cells.size()-1);
            	Cell motherCell = firstCell.getParentCell();
            	Cell newMotherCell = lastCell.getChildren().get(0);
            	double divtime = 0;
            	
            	if(firstCell.getParentCell() != null) {
            		logger.info("Mother cell id: "+firstCell.getParentCell().getId()+"\n");
            		if(!cellsSisterOne.containsKey(Integer.parseInt(motherCell.getId()))) {
            			cellsSisterOne.put(Integer.parseInt(motherCell.getId()), cells);
            			
            			logger.info("Sister one first cell:"+firstCell.getId()+" last cell:"+lastCell.getId()+" new mother cell:"+newMotherCell.getId()+"div. time:"+Double.toString(divtime)+"\n");
            		} else {
            			cellsSisterTwo.put(Integer.parseInt(motherCell.getId()), cells);
            			
            			logger.info("Sister two first cell:"+firstCell.getId()+" last cell:"+lastCell.getId()+" new mother cell:"+newMotherCell.getId()+"div. time:"+Double.toString(divtime)+"\n");
            		}
            	}
            	logger.info("\n");
            }
            
            Map<Integer, List<Cell>> cellsSisterOneSorted  = new TreeMap<Integer,List<Cell>>(cellsSisterOne);
            Map<Integer, List<Cell>> cellsSisterTwoSorted = new TreeMap<Integer,List<Cell>>(cellsSisterTwo);
            
            int no = 1;
            for(List<Cell> value : cellsSisterOneSorted.values()) {
            	double divtime = 0;

            	Cell firstCell = value.get(0);
            	Cell lastCell = value.get(value.size()-1);
            	Cell motherCell = firstCell.getParentCell();
            	Cell newMotherCell = lastCell.getChildren().get(0);
            	
            	divtime = newMotherCell.getMIFrameObject().getElapsedTime()-firstCell.getMIFrameObject().getElapsedTime();
            	if(cellsSisterOneSorted.size() < cellsSisterTwoSorted.size() || cellsSisterOneSorted.size() == cellsSisterTwoSorted.size()) {
            		divisionTimesMotherCellLength.add(motherCell.getLength());
            	}
    			divisionTimesSisterOne.add(divtime);
    			logger.info(Integer.toString(no)+" Sister one first cell:"+firstCell.getId()+" last cell:"+lastCell.getId()+" new mother cell:"+newMotherCell.getId()+"div. time:"+Double.toString(divtime)+"\n");
    			no++;
            }
            
            no = 1;
            for(List<Cell> value : cellsSisterTwoSorted.values()) {
            	double divtime = 0;

            	Cell firstCell = value.get(0);
            	Cell lastCell = value.get(value.size()-1);
            	Cell motherCell = firstCell.getParentCell();
            	Cell newMotherCell = lastCell.getChildren().get(0);
            	
            	divtime = newMotherCell.getMIFrameObject().getElapsedTime()-firstCell.getMIFrameObject().getElapsedTime();
            	if(cellsSisterOneSorted.size() > cellsSisterTwoSorted.size()) {
            		divisionTimesMotherCellLength.add(motherCell.getLength());
            	}
            	divisionTimesSisterTwo.add(divtime);
    			logger.info(Integer.toString(no) +" Sister two first cell:"+firstCell.getId()+" last cell:"+lastCell.getId()+" new mother cell:"+newMotherCell.getId()+"div. time:"+Double.toString(divtime)+"\n");
    			no++;
            }
            
            logger.info("Size sister one cell array: "+ Integer.toString(divisionTimesSisterOne.size())+"\n");
            logger.info("Size sister two cell array: "+ Integer.toString(divisionTimesSisterTwo.size())+"\n");
            logger.info("Size mother cell length array: "+Integer.toString(divisionTimesMotherCellLength.size())+"\n");
            
//            if(divisionTimesSisterOne.size() < divisionTimesSisterTwo.size()) {
//            	for(int k = 0; k < divisionTimesSisterOne.size(); k++) {
//            	dataSet.addValue(divisionTimesSisterOne.get(k), "Population-"+Integer.toString(populationIndex), divisionTimesSisterTwo.get(k));
//            	}
//            }
//            
//            if(divisionTimesSisterOne.size() > divisionTimesSisterTwo.size()) {
//            	for(int k = 0; k < divisionTimesSisterTwo.size(); k++) {
//            		dataSet.addValue(divisionTimesSisterOne.get(k), "Population-"+Integer.toString(populationIndex), divisionTimesSisterTwo.get(k));
//            	}
//            } 
//            	 
//            if(divisionTimesSisterOne.size() == divisionTimesSisterTwo.size()) {
//            	for(int k = 0; k < divisionTimesSisterTwo.size(); k++) {
//            		dataSet.addValue(divisionTimesSisterOne.get(k), "Population-"+Integer.toString(populationIndex), divisionTimesSisterTwo.get(k));
//            	}
//            }
             
            
			// Jump to next tree
//            exportDivisionTimesSistersToCSV(divisionTimesMotherCellLength,divisionTimesSisterOne,divisionTimesSisterTwo,forest.getMetaxml().getProjectName(), populationIndex);
            populationIndex++;
		}
		
		return dataSet;
	}
	
    private void exportDivisionTimesSistersToCSV(List<Double> divisionTimeMotherCellLength, List<Double> divisionTimeSisterOne, List<Double> divisionTimeSisterTwo, String name, int population) {
    	try {
        	FileWriter handle = new FileWriter("CellDivisionTimes-Sisters-"+name+"-"+Integer.toString(population)+".csv");

    		if(divisionTimeSisterOne.size() > divisionTimeSisterTwo.size()){
    			for(int i = 0; i < divisionTimeSisterOne.size(); i++) {
    				handle.append(Double.toString(divisionTimeSisterOne.get(i)));
    				handle.append(",");
    				
    				if(i < divisionTimeSisterTwo.size()){
    					handle.append(Double.toString(divisionTimeSisterTwo.get(i)));
    					handle.append(",");
        				handle.append(Double.toString(divisionTimeMotherCellLength.get(i)));
    				} else {
    					handle.append("");
    					handle.append(",");
    					handle.append("");
    				}
    				handle.append("\n");
    			}
    		}
    		
    		if(divisionTimeSisterOne.size() < divisionTimeSisterTwo.size()) {
    			for(int i = 0; i < divisionTimeSisterTwo.size(); i++) {
    				
    				if(i < divisionTimeSisterOne.size()-1){
    					handle.append(Double.toString(divisionTimeSisterOne.get(i)));
    				} else {
    					handle.append("");
    				}
    				handle.append(",");
    				handle.append(Double.toString(divisionTimeSisterTwo.get(i)));
    				handle.append(",");
    				handle.append(Double.toString(divisionTimeMotherCellLength.get(i)));
    				handle.append("\n");
    			}
    		}
    		
    		if(divisionTimeSisterOne.size() == divisionTimeSisterTwo.size()) {
    			for(int i = 0; i < divisionTimeSisterOne.size(); i++) {
    				handle.append(Double.toString(divisionTimeSisterOne.get(i)));
    				handle.append(",");
    				if(i < divisionTimeSisterTwo.size()-1){
    					handle.append(Double.toString(divisionTimeSisterTwo.get(i)));
    				} else {
    					handle.append("");
    				}
    				handle.append(",");
    				handle.append(Double.toString(divisionTimeMotherCellLength.get(i)));
    				handle.append("\n");
    			}
    		}
    		handle.flush();
    		handle.close();
    		
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
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

}