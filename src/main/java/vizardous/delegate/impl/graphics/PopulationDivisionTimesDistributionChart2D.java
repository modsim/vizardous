package vizardous.delegate.impl.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
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
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PopulationDivisionTimesDistributionChart2D extends DistributionChart2D {

	private static final long serialVersionUID = 1L;
    
    /**
     * TODO Documentation
     * 
     * @param forest
     */
    public PopulationDivisionTimesDistributionChart2D(Forest forest) { 
        super(forest);

        this.tabName = "Division Times Distribution";
        
        if( forest != null ) {
            this.setLayout(new BorderLayout());
            colorPalette = new LinkedList<Color>();
            this.histogramDataset = createPopDivisionTimesHistogramDataset(forest);
            this.chart = createPopDivisionTimesChart(this.histogramDataset);
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
         } else
            throw new IllegalArgumentException("Error");
    }
    
    /**
     * TODO Documentation
     * 
     * @param forest
     * @return
     */
     private HistogramDataset createPopDivisionTimesHistogramDataset(Forest forest) {       
        final HistogramDataset histogramDataset = new HistogramDataset();
                int populationIndex = 1;
                List<Phylogeny> phyloList = forest.getPhyloxml().getPhylogenies();
                for( Phylogeny phylo: phyloList ) {
                	
                    /* Computed division times */
                	List<Double> divisionTimes = new LinkedList<Double>();
                	List<Integer> noOfGenerations = new LinkedList<Integer>();
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
                    
                    
                    /*
                     * Cells -> -	
                     * Node -> o
                     * 
                     * 				  
                     * 				 o-----
                     * 				.
                     * 		  o-----
                     * 		 .		.
                     *      .  		 o-----
                     *o-----	 	
                     *      .		 o-----
                     * 		 .		.
                     * 		  o-----
                     * 				.
                     * 				 o-----
                     * 
                     * 
                     * Traces
                     * 
                     * Traces no .1
                     * 				 o-----
                     * 				.
                     * 		  o-----
                     * 		 .
                     *      .
                     *o-----
                     * 
                     * 
                     * Traces no. 2
                     * 
                     * 
                     * 		  o-----
                     * 		 .		.
                     * 		.		 o-----
                     *o-----
                     * 
                     * 
                     * Traces no. 3
                     * 
                     * 
                     *o-----
                     *		.		 o-----
                     *		 .		.
                     *		  o-----
                     * 
                     * 
                     * Traces no. 4
                     * 
                     * Trace 2->1
                     * o-----
                     * 1	 . 
                     * 		  . Trace 3->2
                     * 		   o-----
                     * 		   2 	 .
                     * 				  o-----
                     * 				  3
                     */
                    
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
                        
                        
                        /*
                         * Code Charaf
                         * 
                         */
                        
                        // The case that the cell is a leaf. The division time of cell/leaf
                        // is equals to elapsed time of leaf minus the elapsed time of the
                        // first ancestors, which has more than one child
                       /* 
                       predecessorIterator = parentsCellList.iterator();
                        
                        if ( cell.isLeaf() ) {
                            // First and last cell of a generation
                            Cell firstCell = null;
                            Cell lastCell = cell;
                             
                            while (predecessorIterator.hasNext()) {
                                    Cell predecessor = predecessorIterator.next();
                                    // Check if predecessor has siblings (or is root node)
                                    if (predecessor.getParentCell() != null && predecessor.getParentCell().getChildren().size() <= 1)
                                            continue;
                                    else {
                                    		//if(predecessorCells.isEmpty() || !predecessorCells.contains(predecessor)) {
                                                firstCell = predecessor;
                                                //predecessorCells.add(firstCell);
                                                
                                                // Compute time between firstCell and lastCell
                                                MIFrame miframeLastCell = lastCell.getMIFrameObject();
                                                double elapsedTimeLastCell = 0.0d;
                                                if (miframeLastCell != null)
                                                        elapsedTimeLastCell = miframeLastCell.getElapsedTime();
                                                else
                                                        throw new IllegalArgumentException("MIFrameObject is null!");

                                                MIFrame miframeFirstCell = firstCell.getMIFrameObject();
                                                double elapsedTimeFirstCell = 0.0d;
                                                if (miframeFirstCell != null)
                                                        elapsedTimeFirstCell = miframeFirstCell.getElapsedTime();
                                                else
                                                        throw new IllegalArgumentException("MIFrameObject is null!");

                                                double divisionTime = elapsedTimeLastCell - elapsedTimeFirstCell;
                                                //divisionTimes.add(divisionTime);
      
                                                break;
                                    		//}


                                    }
                            }
                    // The case that the cell is not a leaf. The division time of cell
                    // is equals to the elapsed time of the first ancestors, which has
                    // more than one child minus the next ancestors, which has more than
                    // one child
                    } else if ( !cell.isLeaf() && !cell.isRoot() ) {
                            //Computed division times
                            List<Cell> cellTempList = new LinkedList<Cell>();

                            while (predecessorIterator.hasNext()) {
                                    Cell cellTemp = predecessorIterator.next();
                                    if (cellTemp.getParentCell() != null && cellTemp.getParentCell().getChildren().size() > 1) 
                                            cellTempList.add(cellTemp);
                                    else 
                                        continue;
                            }
                            if( cellTempList.isEmpty() )
                                continue;
                            else 
                            {
                                Cell rootCell = parentsCellList.get(parentsCellList.size() - 1);
                                cellTempList.add(rootCell);
                                //First and last cell of a generation
                                Cell firstCell = cellTempList.get(1);
                                // Selected cell
                                Cell lastCell = cellTempList.get(0);
                                // Compute time between firstCell and lastCell
                                MIFrame miframeLastCell = lastCell.getMIFrameObject();
                                double elapsedTimeLastCell = 0.0d;
                                if (miframeLastCell != null)
                                        elapsedTimeLastCell = miframeLastCell.getElapsedTime();
                                else
                                        throw new IllegalArgumentException("MIFrameObject is null!");

                                MIFrame miframeFirstCell = firstCell.getMIFrameObject();
                                double elapsedTimeFirstCell = 0.0d;
                                if (miframeFirstCell != null)
                                        elapsedTimeFirstCell = miframeFirstCell.getElapsedTime();
                                else
                                        throw new IllegalArgumentException("MIFrameObject is null!");

                                double divisionTime = elapsedTimeLastCell - elapsedTimeFirstCell;
                                //divisionTimes.add(divisionTime);
    
                                
                                }
                            } else if ( cell.isRoot() ) {
                            // Just for the root cell, sets with null division time and null generation
                            List<Double> temp2 = new LinkedList<Double>();
                            double cellRootdivisionTime = 0.0;
                            temp2.add(cellRootdivisionTime);
                            // Doesn't add the division time of the root to the dataset --> Alex 
							//ds.add( temp2, "" + "population-" + populationIndex , "" + new Double(0.0));
                        }
                        */ 
                    } // for cells
                
                
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
                    		//System.out.print(Double.toString(tempTraces.getDivisionTime())+"from traces"+firstCell.getId()+","+lastCell.getId()+"\n");
                    	
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
                    
//                    logger.info("Elongation rate \n");
                    
                    
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
                    
                    
                    for(int m = 0; m < cellsElongation.size(); m++) {
                    	SimpleRegression regression = new SimpleRegression();
                    	
                    	List<Cell> cells = cellsElongation.get(m);
                    	Double lengths[] = new Double[cells.size()];
                    	Double time[] = new Double[cells.size()];
                    	int i=0;
                    	for(Cell c:cells) {
                    		
                    		lengths[i] = c.getLength();
                    		
                    		time[i] = c.getMIFrameObject().getElapsedTime();

                    		regression.addData(time[i], lengths[i]);
//                    		logger.info("Length: "+Double.toString(lengths[i])+", time:"+Double.toString(time[i])+" "+c.getId()+"\n");
                    		i++;
                    	}
                    	
                    	cellLengths.add(lengths);
                    	cellLengthsTime.add(time);
                    	elongationRates.add(regression.getSlope());
//                    	logger.info("Elongation rate: "+regression.getSlope()+"("+cellsElongationGeneration.get(m)+")"+"\n");
//                    	logger.info("\n");
                    }
                    
//                    exportElongationRatesToCSV(cellsElongationGeneration, elongationRates,forest.getMetaxml().getProjectName(), populationIndex);
                    
                   
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
                    
//                    logger.info("Cell lengths/area before and after division: \n");

                    for(int k = 0; k < tracesGeneration.size(); k++) {
                    	List<Trace> traces_ = tracesGeneration.get(k).getTraces();
                    	
                    	for(Trace t_ : traces_) {
                    		
                    			if(cellsBeforeDivision.isEmpty()) {
//                    				logger.info("After: "+t_.getChildNode().getCellLengthAfterDivision()+"("+t_.getChildNode().getCell().getId()+")\n");
//                    				logger.info("Before: "+t_.getChildNode().getCellLengthBeforeDivision()+"("+t_.getChildNode().getCellBeforeDivision().getId()+")\n");
                    				
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
//                    					logger.info("Before: "+t_.getChildNode().getCellLengthBeforeDivision()+"("+t_.getChildNode().getCellBeforeDivision().getId()+")\n");
                    					
                    					cellLengthsBeforeDivision.add(t_.getChildNode().getCellLengthBeforeDivision());
                    					cellAreasBeforeDivision.add(t_.getChildNode().getCellAreaBeforeDivision());
                    					
                    					cellsBeforeDivision.add(t_.getChildNode().getCellBeforeDivision());
                        				elapsedTimeBeforeDivision.add(t_.getChildNode().getCellBeforeDivision().getMIFrameObject().getElapsedTime());
                    				}
                    				
                    				if(!cellsAfterDivision.contains(t_.getChildNode().getCell())) {
//                    					logger.info("After: "+t_.getChildNode().getCellLengthAfterDivision()+"("+t_.getChildNode().getCell().getId()+")\n");
                    					
                    					cellLengthsAfterDivision.add(t_.getChildNode().getCellLengthAfterDivision());
                    					cellAreasAfterDivision.add(t_.getChildNode().getCellAreaAfterDivision());
                    					
                    					cellsAfterDivision.add(t_.getChildNode().getCell());
                        				elapsedTimeAfterDivision.add(t_.getChildNode().getCell().getMIFrameObject().getElapsedTime());
                    				}
                    			}
                    	}
                    	
                    }
                    
                    // Export cell lengths before and after division to CSV
                    
//                    exportCellLengthsBeforeDivisionToCSV(elapsedTimeBeforeDivision,cellLengthsBeforeDivision,forest.getMetaxml().getProjectName(), populationIndex);
//                    exportCellLengthsAfterDivisionToCSV(elapsedTimeAfterDivision,cellLengthsAfterDivision,forest.getMetaxml().getProjectName(), populationIndex);
                    
                   // Export cell areas before and after division to CSV
                    
//                    exportCellAreasBeforeDivisionToCSV(cellAreasBeforeDivision,forest.getMetaxml().getProjectName(), populationIndex);
//                    exportCellAreasAfterDivisionToCSV(cellAreasAfterDivision,forest.getMetaxml().getProjectName(), populationIndex);


                    /*
                     * Determination of division times
                     */
                    
                    // List of checked parent cells
                    List<Cell> nodes = new LinkedList<Cell>();
                    
//                    logger.info("Divison times: \n");
                    
                    for(int k = 0; k < tracesGeneration.size(); k++) {
                    	List<Trace> traces_ = tracesGeneration.get(k).getTraces();
                    
                	
                    	for(Trace t_ : traces_) {
                    		
                    		// No parent cells checked yet
                    		if(nodes.isEmpty()) {
                    			// Add parent cell to list of checked nodes
                    			nodes.add(t_.getParentNode().getCell());
                    			
                    			// Get division time from trace
                    			divisionTimes.add(t_.getDivisionTime());
                    			noOfGenerations.add(t_.getGeneration());
//                    			logger.info("Div. time: "+Double.toString(t_.getDivisionTime())+",Gen.: "+Integer.toString(t_.getGeneration())+"->"+t_.getTraces().get(0).getCell().getId()+","+t_.getTraces().get(1).getCell().getId()+"\n");
                    			continue;
                    		} else {
                    			
                    			// Skip node if already has been taking in account before
                    			if(nodes.contains(t_.getParentNode().getCell())) {
                    				//System.out.print("Node already checked\n");
                    				continue;
                    			}

                    			// Add parent cell to list of checked nodes
                    			nodes.add(t_.getParentNode().getCell());
                    			divisionTimes.add(t_.getDivisionTime());
                    			noOfGenerations.add(t_.getGeneration());
//                    			logger.info("Div. time: "+Double.toString(t_.getDivisionTime())+",Gen.: "+Integer.toString(t_.getGeneration())+"->"+t_.getTraces().get(0).getCell().getId()+","+t_.getTraces().get(1).getCell().getId()+"\n");
                    		}
                    	}
                
                    }
                
                    // No. of bins estimation                    
                    double divTime[] = new double[divisionTimes.size()];
                    int gen[] = new int[noOfGenerations.size()];
                    double tempVariable = 0;
                    int binsNumber = 0;
                    for (int i = 0; i < divisionTimes.size(); i++) {
                    	divTime[i] = divisionTimes.get(i);
                    	gen[i] = noOfGenerations.get(i);
                    	if( tempVariable < divisionTimes.get(i) ) 
                    		tempVariable = divisionTimes.get(i);                 
                    }
                    
                    // Export division times to csv file
//                    exportDivisionTimeToCSV(gen,divTime, forest.getMetaxml().getProjectName(), populationIndex);
                                                           
                    binsNumber =  (int) Math.floor(Math.sqrt(divisionTimes.size())); // #bins = sqrt(N)
                    if (binsNumber > 0 || divTime.length > 0) {
                    	String seriesKey = String.format("Population %d", populationIndex);                    	
                    	histogramDataset.addSeries(seriesKey, divTime, binsNumber);
                    	
                    	Comparable seriesIndex = histogramDataset.indexOf(seriesKey); 
                    	Collection<Double> datasetValuesList = new ArrayList<Double>(Arrays.asList(ArrayUtils.toObject(divTime)));                   	 
                   	 	getData().put(seriesIndex, datasetValuesList);
                    }
                    populationIndex++;

                } //for Phylogenies
         return histogramDataset;
    } 
        
     /**
      * TODO Documentation
      * 
      * @param dataset
      * @return
      */
    private JFreeChart createPopDivisionTimesChart(IntervalXYDataset dataset) {
    	String timeUnit = "min"; // Set default value
		
		if (forest != null) {
			timeUnit = forest.getMetaxml().getExperimentDurationUnit();
		}
    	
    	final JFreeChart chartLocal = ChartFactory.createHistogram(
                "Division times distribution",
                String.format("Division times [%s]", timeUnit),
                "Occurrence [-]",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
         );

        styleChart(chartLocal);
               
        return chartLocal;
    }  
    
//    /**
//     * TODO Documenation
//     * 
//     * @param time
//     * @param cellLengthsBeforeDivision
//     * @param name
//     * @param population
//     */
//    public void exportCellLengthsBeforeDivisionToCSV(List<Double> time,List<Double> cellLengthsBeforeDivision, String name, int population) {
//    	try {
//    		FileWriter handle = new FileWriter("CellLengthsBeforeDivision-"+name+"-"+Integer.toString(population)+".csv");
//    		for(int i = 0; i < cellLengthsBeforeDivision.size(); i++) {
//    			handle.append(Double.toString(time.get(i)));
//    			handle.append(",");
//    			handle.append(Double.toString(cellLengthsBeforeDivision.get(i)));
//    			handle.append("\n");
//    		}
//    		
//    		handle.flush();
//    		handle.close();
//    	} catch(IOException e) {
//    		e.printStackTrace();
//    	}
//    }
//
//    /**
//     * TODO Documentation
//     * 
//     * @param time
//     * @param cellLengthsAfterDivision
//     * @param name
//     * @param population
//     */
//    public void exportCellLengthsAfterDivisionToCSV(List<Double> time, List<Double> cellLengthsAfterDivision, String name, int population) {
//    	try {
//    		FileWriter handle = new FileWriter("CellLengthsAfterDivision-"+name+"-"+Integer.toString(population)+".csv");
//    		for(int i = 0; i < cellLengthsAfterDivision.size(); i++) {
//    			handle.append(Double.toString(time.get(i)));
//    			handle.append(",");
//    			handle.append(Double.toString(cellLengthsAfterDivision.get(i)));
//    			handle.append("\n");
//    		}
//    		
//    		handle.flush();
//    		handle.close();
//    		
//    	} catch(IOException e) {
//    		e.printStackTrace();
//    	}
//    }
//    
//    /**
//     * TODO Documentation
//     * 
//     * @param cellAreasBeforeDivision
//     * @param name
//     * @param population
//     */
//    public void exportCellAreasBeforeDivisionToCSV(List<Double> cellAreasBeforeDivision, String name, int population) {
//    	try {
//    		FileWriter handle = new FileWriter("CellAreasBeforeDivision-"+name+"-"+Integer.toString(population)+".csv");
//    		for(int i = 0; i < cellAreasBeforeDivision.size(); i++) {
//    			handle.append(Double.toString(cellAreasBeforeDivision.get(i)));
//    			handle.append("\n");
//    		}
//    		
//    		handle.flush();
//    		handle.close();
//    	} catch(IOException e) {
//    		e.printStackTrace();
//    	}
//    }
//    
//    public void exportCellAreasAfterDivisionToCSV(List<Double> cellAreasAfterDivision, String name, int population) {
//    	try {
//    		FileWriter handle = new FileWriter("CellAreasAfterDivision-"+name+"-"+Integer.toString(population)+".csv");
//    		for(int i = 0; i < cellAreasAfterDivision.size(); i++) {
//    			handle.append(Double.toString(cellAreasAfterDivision.get(i)));
//    			handle.append("\n");
//    		}
//    		
//    		handle.flush();
//    		handle.close();
//    	} catch(IOException e) {
//    		e.printStackTrace();
//    	}
//    }
//    
//    /**
//     * TODO Documentation
//     * 
//     * @param elongationRatesGenerations
//     * @param elongationRates
//     * @param name
//     * @param population
//     */
//    public void exportElongationRatesToCSV(List<Integer> elongationRatesGenerations, List<Double> elongationRates, String name, int population) {
//    	try {
//        	FileWriter handle = new FileWriter("CellElongationRates-"+name+"-"+Integer.toString(population)+".csv");
//        	for(int i = 0; i < elongationRates.size(); i++) {
//        		handle.append(Integer.toString(elongationRatesGenerations.get(i)));
//        		handle.append(",");
//        		handle.append(Double.toString(elongationRates.get(i)));
//        		handle.append("\n");
//        	}
//        	
//    		handle.flush();
//    		handle.close();
//    	} catch(IOException e) {
//    		e.printStackTrace();
//    	}
//    }

	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

}
