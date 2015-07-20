/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import cern.colt.matrix.doublealgo.Formatter;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import vizardous.delegate.impl.analysis.PhyloTreeAnalyser;
import vizardous.delegate.impl.comparison.HybridComparisonOfCells;
import vizardous.delegate.impl.comparison.StructuralComparisonOfNodes;
import vizardous.delegate.impl.jgraphx.MyCompactTreeLayout;
import vizardous.delegate.impl.jgraphx.MyGraph;
import vizardous.delegate.impl.jgraphx.MyGraphComponent;
import vizardous.delegate.impl.jgraphx.MyModel;
import vizardous.delegate.impl.jgraphx.Style;
import vizardous.delegate.impl.table.CellsInformationTable;
import vizardous.delegate.impl.table.CellsComparisonTable;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Clade;
import vizardous.model.impl.Constants;
import vizardous.model.impl.Forest;
import vizardous.model.impl.Phylogeny;

/**
 * This class shows the tree defined in the associated Phylogeny/PhyloXML file 
 * in a swing GUI. Additionally, in this class is implemented the interactions 
 * with the drawn tree.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class DrawPhyloTreeGraph extends JPanel implements Cloneable {
    private PhyloTreeAnalyser                   phyloTreeAnlyser                        = null;
    private Forest                              forest                                  = null;
    private Phylogeny                           phylogeny                               = null;
    private CellsInformationTable               cellInfoTable                           = null;
    private CellsComparisonTable                cellsCompTable                          = null;
//    private VisualizationViewer                 vv                                      = null;
	private mxGraph 							graph									= null;
	private MyGraphComponent					graphComponent							= null;
    private Collection<Cell>                          allCellsInMetaFile                      = null;
    private CreatePhyloTreeGraph                phyloTreeGraph                          = null;
    private DelegateTree<Cell,Clade>            g3                                      = null;
    private Color                               defaultColor                            = Color.BLACK; 
    private Map<List<Cell>, Paint>              mapCellPath2Color                       = new HashMap<List<Cell>, Paint>();
    private Map<List<Clade>, Paint>             mapCladePath2Color                      = new HashMap<List<Clade>, Paint>();
    private Map<Cell, Double>                   mapCellToNormLengthValue                = new HashMap<Cell, Double>();
    private Map<Cell, Double>                   mapCellToNormAreaValue                  = new HashMap<Cell, Double>();
    private Map<Cell, Double>                   mapCellToNormFluorescenceYFPValue       = new HashMap<Cell, Double>();
    private Map<Cell, Double>                   mapCellToNormFluorescenceCRIMSONValue   = new HashMap<Cell, Double>();
    private Map<Clade, Double>                  mapCladeToNormBranchLengthValue         = new HashMap<Clade, Double>();
    private Map<Clade, Number>                  mapEdgeToCladeBranchLengthValue         = new HashMap<Clade, Number>();
    private double defaultValueFluorescence = 0;
    private Map<Cell, Double> mapCellToYfpThreshold = new HashMap<Cell, Double>();
    private Map<Cell, Double> mapCellToCrimsonThreshold = new HashMap<Cell, Double>();
    private NumberFormat n = NumberFormat.getInstance();

    private Comparator<Cell> sorter = null;
    
    /** Indicates whether the phylogeny is displayed horizontally or vertically */
    private boolean bHorizontal = true;
    
    private MyPopUpMenu myPopupMenu;
    private CellPopupMenu cellPopupMenu;
    
    // max and stdandard dimensions for a vertex in the graph
    public static final Dimension max = new Dimension(100, 100);
    public static final Dimension std = new Dimension(20, 20);
    
    private int maxNodeSize = std.height;
    
    // standard vertex and edge style 
    public static final Style stdVertexStyle = new Style("");
    public static final Style stdEdgeStyle = new Style(";")
    						.put(mxConstants.STYLE_ROUNDED, "1")
    						.put(mxConstants.STYLE_ORTHOGONAL, "0")
    						.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle")// = new Style("edgeShape=connector;strokeWidth=2;strokeColor=black;endFill=0;");
    						.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR)
//    						.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC)
    						.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE)
    						.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER)
    						.put(mxConstants.STYLE_STROKECOLOR, "#000000") // default is #6482B9
    						.put(mxConstants.STYLE_FONTCOLOR, "#446299");
    
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(DrawPhyloTreeGraph.class);
    
   /**
    * Class constructor to draw a phyloTreeGraph
    * 
    * @param phyloTreeAnlyser phyloTreeAnlyser who the tree paint panel will shown
    * @param Forest forest 
    * @param Phylogeny phylo define the tree to drwan
    * @param CellsInformationTable cellInfoTable table 
    * @param Dimension dim 
    */
    public DrawPhyloTreeGraph(PhyloTreeAnalyser phyloTreeAnlyser, final Forest forest, Phylogeny phylo, final CellsInformationTable cellInfoTable, CellsComparisonTable cellCompTable) {
        this.phyloTreeAnlyser = phyloTreeAnlyser;
        this.forest = forest;
        this.phylogeny = phylo;
        this.cellInfoTable = cellInfoTable;
        n.setMaximumFractionDigits(3);
        allCellsInMetaFile = forest.getAllCellsInMetaXML();
        this.cellsCompTable = cellCompTable;                

        /** Get the created phyloTreeGraph to be drawn. */
        phyloTreeGraph = new CreatePhyloTreeGraph(phylogeny);
        g3 = phyloTreeGraph.getPhyloTreeGraph();
        
        //creating a map of edges
        mapEdgeToCladeBranchLengthValue = new HashMap<Clade,Number>();
        for (Clade edg : g3.getEdges()) {
          mapEdgeToCladeBranchLengthValue.put(edg, edg.getBranchLength());
        }
         
        // Initialize the map lists
        initMappedLists();
                
        // setup jGraphX
        createGraph(phylo);
        
        applyLayout(graph);

		this.graphComponent = new MyGraphComponent(graph);
		// disable horizontal and vertical scrollbars
//		graphComponent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		graphComponent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		// There cannot be made new connections
		graphComponent.setConnectable(false);
		// enable tooltips
		graphComponent.setToolTips(true);
		
		// the zoom listener
		graphComponent.getGraphControl().addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int i = e.getWheelRotation();
				if(i < 0) {
					graphComponent.zoomIn();
				}
				else {
					graphComponent.zoomOut();
				}
			}
		});
		
		graphComponent.getGraphControl().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent me) {
				// check whether a cell was hit with the left mouse button (display cell info)
                if ( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 1) {
					Object cell = graphComponent.getCellAt(me.getX(), me.getY());
					if(cell != null && ((mxCell)cell).isVertex()) {
					    callCladePropertiesFrame((Cell)((mxCell)cell).getValue()); 
						me.consume();
					}
                  }
                // check whether a right click was performed and show popup menu
                else if(me.getButton() == MouseEvent.BUTTON3) {
					Object cell = graphComponent.getCellAt(me.getX(), me.getY());
					if(cell != null && ((mxCell)cell).isVertex()) {
						Object[] cells = graph.getSelectionCells();
						// there was a right click on vertex that
						LinkedList<Cell> list = new LinkedList<Cell>();
						for(Object tmp : cells) {
							list.add((Cell)((mxCell)tmp).getValue());
						}
						cellPopupMenu.show(list, me.getComponent(), me.getX(), me.getY());
						me.consume();
					}
					else
					{
	                	myPopupMenu.show(me.getComponent(), me.getX(), me.getY());
	                    me.consume();
					}
                }
			}
		});
		
		graphComponent.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// shortcut for adding to infotable
				if(e.getKeyChar() == 'a') {
					// get list of selected cells
					Object[] cells = graph.getSelectionCells();
					LinkedList<Cell> list = new LinkedList<Cell>();
					for(Object tmp : cells) {
						list.add((Cell)((mxCell)tmp).getValue());
					}
					
					// add them all to the infotable
					for(Cell cell : list) {
						Double fluorescence = (double) 0;
						
			            if ( cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) != null ) 
			                fluorescence = cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS);

		                ((DefaultTableModel) cellInfoTable.getModel()).
		                addRow(new Object[]{cell.getId(), fluorescence, cell.getLength(), cell.getArea()});
		                
					}

					// deselect all cells
					try {
						graph.getModel().beginUpdate();
		                graph.getSelectionModel().clear();
					}
					finally {
		                graph.getModel().endUpdate();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		// make cells unmoveable
		graph.setCellsMovable(false);
		
		graphComponent.setPanning(true);
		
		graphComponent.getViewport().setBackground(Color.WHITE);
		
		// add the multi selection possibility
        new mxRubberband(graphComponent);

		applyPerfectScale();

        myPopupMenu = new MyPopUpMenu(forest, phylogeny, graphComponent, DrawPhyloTreeGraph.this, cellInfoTable);
        cellPopupMenu = new CellPopupMenu(cellInfoTable);
    }
    
    /**
     * Set the default styles for the graph.
     * @param graph to manipulate
     */
    public static void setDefaultStyle(mxGraph graph) {
		// create standard style for edges
	    Map<String, Object> edge = new HashMap<String, Object>();
	    edge.put(mxConstants.STYLE_ROUNDED, true);
	    edge.put(mxConstants.STYLE_ORTHOGONAL, false);
	    edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
	    edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
//    	    edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
	    edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
	    edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
	    edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
	    edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");

	    // create standard style for vertices
	    Map<String, Object> vertex = graph.getStylesheet().getDefaultVertexStyle();
	    //shape=ellipse;fillColor=black;strokeColor=black;exitY=0.5
	    vertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
	    vertex.put(mxConstants.STYLE_FILLCOLOR, "black");
	    vertex.put(mxConstants.STYLE_STROKECOLOR, "black");
//	    vertex.put(mxConstants.STYLE_STROKEWIDTH, "100");

	    mxStylesheet style = new mxStylesheet();
	    style.setDefaultEdgeStyle(edge);
	    style.setDefaultVertexStyle(vertex);
	    
	    graph.setStylesheet(style);
    }
    
    /**
     * Creates a graph based on the given phylogeny and sets the graph attribute in the object
     * 
     * @param phylo that contains cell data for the graph
     */
    public void createGraph(Phylogeny phylo) {
        // setup jGraphX
        mxGraph newGraph = new MyGraph();
		Object defaultParent = newGraph.getDefaultParent();

		newGraph.getModel().beginUpdate();
		try
		{
			Map<Cell, mxICell> map = new HashMap<Cell, mxICell>();

			/*
			 * Clades are objects that only contain structural information from
			 * phyloXML files. Cellular characteristics are stored in Cell
			 * objects.
			 */
			Clade rootClade = phylo.getRootClade();
			Cell rootCell = rootClade.getCellObject();
			
			/* Iterate the phylogeny in a depth-first manner */
			Iterator<Cell> phylogenyIter = rootCell.iterator();
			
			while (phylogenyIter.hasNext()) {
				Cell cell = phylogenyIter.next();
				Cell parent = cell.getParentCell();
				
				Object cellVertex = newGraph.insertVertex(defaultParent, cell.getId(), cell, 0, 0, std.getWidth(), std.getHeight());//, stdVertexStyle.toString());
				
				map.put(cell, (mxCell) cellVertex);
			}

			// add edges
			addEdges(newGraph, rootCell, sorter);
			
		   // set cells unresizeable
		   newGraph.setCellsResizable(false);
		   // set cells not disconnectable (now edges are not movable anymore)
		   newGraph.setCellsDisconnectable(false);
		   // do not move edge labels (that does not make sense :))
		   newGraph.setEdgeLabelsMovable(false);
		   newGraph.setCellsEditable(false);

		   setDefaultStyle(newGraph);
		}
		finally
		{
			newGraph.getModel().endUpdate();
		}

		// set object attribute to the new graph
		graph = newGraph;
		

    }

    /**
     * Add all edges to the jgraphX graph
     * @param graph of jgraphX
     * @param cell to start with (normally root)
     * @param comp that can compare two cells
     */
    public static void addEdges(mxGraph graph, Cell cell, Comparator<Cell> comp) {
		Object defaultParent = graph.getDefaultParent();
		mxCell cellVertex = ((MyModel)graph.getModel()).getVertex(cell.getId());
    	
		// if there is just one children add this to the graph and continue recursively
    	if(cell.getChildren().size() == 1) {
    		mxCell childVertex = ((MyModel)graph.getModel()).getVertex(cell.getChildren().get(0).getId());
    		mxCell edge = (mxCell)graph.insertEdge(defaultParent, cell.getId()+cell.getChildren().get(0).getId(), cell.getCladeObject(), cellVertex, childVertex);//, stdEdgeStyle.toString());
    		
    		addEdges(graph, cell.getChildren().get(0), comp);
    	}
    	// if there are two children
    	else if(cell.getChildren().size() == 2) {
    		Cell[] cells = {cell.getChildren().get(0), cell.getChildren().get(1)};
    		
    		// sort cells
    		if(comp != null) {
    			Arrays.sort(cells, comp);
    		}
    		

    		// get corresponding jgraphx vertices
    		mxCell[] vertices = {((MyModel)graph.getModel()).getVertex(cells[0].getId())
    				, ((MyModel)graph.getModel()).getVertex(cells[1].getId()) };
    		
    		// insert edges
    		graph.insertEdge(defaultParent, cell.getId() + cells[1].getId(), cell.getCladeObject(), cellVertex, vertices[1]);//, stdEdgeStyle.toString());
    		graph.insertEdge(defaultParent, cell.getId() + cells[0].getId(), cell.getCladeObject(), cellVertex, vertices[0]);//, stdEdgeStyle.toString());
    		
    		// continue recursively
    		addEdges(graph, cell.getChildren().get(0), comp);
    		addEdges(graph, cell.getChildren().get(1), comp);
    	}
    }

    
    /**
     * Gets the paint panel
     * 
     * @return mxGraphComponent graphComponent
     */
    public mxGraphComponent getPhyloTreePanel() {
        return graphComponent;
    }
    
    /**
     * Gets the created phyloTreeGraph
     * 
     * @return mxGraph graph
     */
    public DelegateTree<Cell, Clade> getPhyloTreeGraph(){
        return g3;
    }
    
    public Forest getForest() {
        return forest;
    }
    
    public Phylogeny getPhylogeny() {
        return phylogeny;
    }
    
    /**
     * Use to colorize the cell path (from current cell backwards to the root)
     * 
     * @param cell to colorize
     * @param colorCell Color
     */
    public void colorizePathCell(Cell cell, Color colorCell) {
    	colorVertexAndPredecessors(cell, colorCell);
    	colorEdgesAndPredecessors(cell, colorCell);
    }
    
    /**
     * Give the CladeProperties window, who is represented 
     * the information about cell and the assiociated clade
     * 
     * @param Cell cell their properties, which are displayed in the CladePorperties window 
     */
    private void callCladePropertiesFrame(Cell cell) {
        Clade clade = cell.getCladeObject();
        ArrayList<Clade> tempList= phylogeny.getAllChildrenOfClade(clade, new ArrayList<Clade>());
        ArrayList<Clade> tempparentList = new ArrayList<Clade>();
        tempparentList= phylogeny.getCladeParents(clade, tempparentList);
        String []subClades = null;
        subClades =  new String[tempList.size()];
        for(int i = 0; i < tempList.size(); i++) {
            subClades[i]= tempList.get(i).getName();
        }
        CladeProperties frame = new CladeProperties("Properties from "+ cell.getId(), MouseInfo.getPointerInfo().getLocation(), phylogeny, cellInfoTable, cellsCompTable);
        if(clade == null) 
            throw new IllegalArgumentException("Clade is null! The call was in class:" + this.getClass().getName());
        else {
            frame.setCladeName(clade.getName());
            frame.setCladeBrancheLength(clade.getBranchLength());
            frame.setCladeLevel(phylogeny.calculateLevelOfClade(clade));
            frame.setCladeDepth(phylogeny.calculateDepthOfClade(clade));
        }

        String []parentClades = null;
        parentClades =  new String[tempparentList.size()];
        for(int i = 0; i < tempparentList.size(); i++) {
            parentClades[i]= tempparentList.get(i).getName();
        }
        if(clade == null) 
            throw new IllegalArgumentException("Clade is null! The call was in class:" + this.getClass().getName());
        else {
                frame.setCladeName(clade.getName());
                frame.setCladeBrancheLength(clade.getBranchLength());
        }

        if(cell == null) 
            throw new IllegalArgumentException("Cell is null! The call was in class:" + this.getClass().getName());
        else {
            frame.setCellID(cell.getId());
            frame.setCellArea(cell.getArea());
            frame.setCellLength(cell.getLength());
            if ( cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) != null ) 
                frame.setFluorescence(cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS));
            else 
                frame.setFluorescence(defaultValueFluorescence);
            
            frame.setFrameElapsedTime(cell.getMIFrameObject().getElapsedTime());
            frame.setCellAreaUnit(cell.getAreaUnit());
            frame.setCellLengthUnit(cell.getLengthUnit());
            frame.setFluorescenceUnit(cell.getFluorescenceUnit());
            frame.setFrameElapsedTimeUnit(cell.getMIFrameObject().getElapsedTimeUnit());
        }

        frame.setSubCladesList(subClades);
        frame.setParentCladesList(parentClades);
        frame.setVisible(true);
    }

    /**
     * Sets the analysis mode
     *  mode: 1) Cells analysis mode
     *          a) fluorescences i) yfp fluorescence ii) crimson fluorescence
     *          b) length
     *          c) area 
     *  mode: 2) Clades analysis mode
     *          a) branch length
     * 
     * @param selectedAnalysisMode 
     */
    public void setVisualAnalysisMode(String selectedAnalysisMode, Map<String, Double> thresholds) {
    	
    	Map<Cell, Double> mapVertexSizeNormed = new HashMap<Cell, Double>();
    	
        if (selectedAnalysisMode.equals("Length")) {
        	mapVertexSizeNormed = mapCellToNormLengthValue;
        } else if (selectedAnalysisMode.equals("Area")) {
        	mapVertexSizeNormed = mapCellToNormAreaValue;
        } else if(selectedAnalysisMode.equals("YFP fluorescence")) {
        	mapVertexSizeNormed = mapCellToNormFluorescenceYFPValue;
        } else if(selectedAnalysisMode.equals("CRIMSON fluorescence")) {
        	mapVertexSizeNormed = mapCellToNormFluorescenceCRIMSONValue;
        } else if(selectedAnalysisMode.equals("Cells analysis mode") 
                || selectedAnalysisMode.equals("Clades analysis mode") 
                || selectedAnalysisMode.equals("fluorescencesRB")) {
        	mapVertexSizeNormed = mapCellToNormLengthValue;
        } else if(selectedAnalysisMode.equals("Branch length")) {
        	mapVertexSizeNormed = mapCellToNormLengthValue;
//            esa = new EdgeWeightStrokeFunction(mapCladeToNormBranchLengthValue);
//            esa.setWeighted(true);
        }
        else if (selectedAnalysisMode.equals("YFP threshold")) {
        	for(Cell cell : g3.getVertices()) {               
                Map<String, Double> fluorescences = cell.getFluorescences();
                // TODO Implement correctly
                if (fluorescences.get("yfp") > thresholds.get("yfp")) {
                	mapCellToYfpThreshold.put(cell, 1d);
                } else {
                	mapCellToYfpThreshold.put(cell, 0d);
                }
            }

        	mapVertexSizeNormed = mapCellToYfpThreshold;
        } else if (selectedAnalysisMode.equals("Crimson threshold")) {
        	for(Cell cell : g3.getVertices()) {               
                Map<String, Double> fluorescences = cell.getFluorescences();
                // TODO Implement correctly
                if (fluorescences.get("crimson") > thresholds.get("crimson")) {
                	mapCellToCrimsonThreshold.put(cell, 1d);
                } else {
                	mapCellToCrimsonThreshold.put(cell, 0d);
                }
            }
        	
        	mapVertexSizeNormed = mapCellToCrimsonThreshold;
        }

        // update the GUI
        updateVertexSize(mapVertexSizeNormed);
    }

    
    

	/**
     * Sets the analysis mode
     *  mode: 1) Cells analysis mode
     *          a) fluorescences i) yfp fluorescence ii) crimson fluorescence
     *          b) length
     *          c) area 
     *  mode: 2) Clades analysis mode
     *          a) branch length
     * 
     * @param selectedAnalysisMode 
     */
    public void setDataDriveAnalysisMode(String selectedDataDriveAnalysisMode) {
//        VertexShapeSizeAspect<Cell, Double> vssa = null;
//        VertexSimilarityPaintTransformer vspt= null;
        EdgeWeightStrokeFunction<Clade> esa = null;
        List<Cell> cellsToBeCompared = new LinkedList<Cell>(); 
        for(int i = 0; i < cellsCompTable.getModel().getRowCount(); i++) {
            String cellId = cellsCompTable.getValueAt(i, 2).toString();
            Cell cell = forest.getCellById(cellId);
            cellsToBeCompared.add(cell); 
        }
        if ( !cellsToBeCompared.isEmpty() ) {
            
            if (selectedDataDriveAnalysisMode.equals("Dijkstra shortest path algorithm")) {
                StructuralComparisonOfNodes scon = new StructuralComparisonOfNodes();
//                HybridComparisonOfCells hcon = new HybridComparisonOfCells(forest);
//                System.out.println("1-Cell-Id: " + cellsToBeCompared.get(0).getId() + "   2-Cell-Id: " + cellsToBeCompared.get(1).getId());
                boolean same = scon.comapareTwoNodes(cellsToBeCompared.get(0), cellsToBeCompared.get(1));
                if ( same == true ) {
/*                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    vv.getRenderContext().setVertexFillPaintTransformer(
                            new VertexSimilarityPaintTransformer(cellsToBeCompared));
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " and Cell: " + cellsToBeCompared.get(1).getId() + " are structurally same.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));*/

                } else {
/*                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " and Cell: " + cellsToBeCompared.get(1).getId() + " are not structurally same.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));*/
                }
            } else if (selectedDataDriveAnalysisMode.equals("Length")) {
                List<Double> list = cellsComparisonBasenOnLength(cellsToBeCompared.get(0), cellsToBeCompared.get(1));
                if ( list.get(0) == 1.0 ) {
/*                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                     vv.getRenderContext().setVertexFillPaintTransformer(
                            new VertexSimilarityPaintTransformer(cellsToBeCompared));
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() +  " [" + n.format(list.get(1).doubleValue()) + "]" + " and Cell: " 
                            + cellsToBeCompared.get(1).getId() + " [" + n.format(list.get(2).doubleValue()) + "]" + "  are same concering the length character.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));*/

                } else if ( list.get(0) == 0.0 ) {
/*                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " [" + n.format(list.get(1).doubleValue()) + "]" + " and Cell: " 
                            + cellsToBeCompared.get(1).getId() + " [" + n.format(list.get(2).doubleValue()) + "]" + "  are not same concering the length character.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));*/
                }
            } /* else if (selectedDataDriveAnalysisMode.equals("Area")) {
                vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                List<Double> list = cellsComparisonBasedOnArea(cellsToBeCompared.get(0), cellsToBeCompared.get(1));
                if ( list.get(0) == 1.0 ) {
                     vv.getRenderContext().setVertexFillPaintTransformer(
                            new VertexSimilarityPaintTransformer(cellsToBeCompared));
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() +  " [" + n.format(list.get(1)) + "]" + " and Cell: " 
                            + cellsToBeCompared.get(1).getId() + " [" + n.format(list.get(2)) + "]" + "  are same concering the area character.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));

                } else if ( list.get(0) == 0.0 ) {
                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() +  " [" + n.format(list.get(1)) + "]" + " and Cell: " + cellsToBeCompared.get(1).getId() 
                            +  " [" + n.format(list.get(1)) + "]" +  "  are not same concering the area character.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));
                }
            } else if (selectedDataDriveAnalysisMode.equals("Area:Dijkstra shortest path algorithm")) {
                 HybridComparisonOfCells hcon = new HybridComparisonOfCells(forest);
//                System.out.println("1-Cell-Id: " + cellsToBeCompared.get(0).getId() + "   2-Cell-Id: " + cellsToBeCompared.get(1).getId());
                boolean same = hcon.comapareTwoNodes(cellsToBeCompared.get(0), cellsToBeCompared.get(1));
                if ( same == true ) {
                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    vv.getRenderContext().setVertexFillPaintTransformer(
                            new VertexSimilarityPaintTransformer(cellsToBeCompared));
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " and Cell: " + cellsToBeCompared.get(1).getId() + " are the same.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));

                } else {
                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " and Cell: " + cellsToBeCompared.get(1).getId() + " are not the same.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));
                }
             } else if (selectedDataDriveAnalysisMode.equals("Length:Dijkstra shortest path algorithm")) {
                 HybridComparisonOfCells hcon = new HybridComparisonOfCells(forest);
//                System.out.println("1-Cell-Id: " + cellsToBeCompared.get(0).getId() + "   2-Cell-Id: " + cellsToBeCompared.get(1).getId());
                boolean same = hcon.comapareTwoNodes(cellsToBeCompared.get(0), cellsToBeCompared.get(1));
                if ( same == true ) {
                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    vv.getRenderContext().setVertexFillPaintTransformer(
                            new VertexSimilarityPaintTransformer(cellsToBeCompared));
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " and Cell: " + cellsToBeCompared.get(1).getId() + " are the same.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));

                } else {
                    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
                    JOptionPane.showMessageDialog(this,
                    "Cell: " + cellsToBeCompared.get(0).getId() + " and Cell: " + cellsToBeCompared.get(1).getId() + " are not the same.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));
                }
            } else if(selectedDataDriveAnalysisMode.equals("YFP fluorescence")) {
                vssa = new VertexShapeSizeAspect<Cell,Double>(mapCellToNormFluorescenceYFPValue); 
                vssa.setScaling(true);
            } else if(selectedDataDriveAnalysisMode.equals("CRIMSON fluorescence")) {
                vssa = new VertexShapeSizeAspect<Cell,Double>(mapCellToNormFluorescenceCRIMSONValue); 
                vssa.setScaling(true);
            } else if(selectedDataDriveAnalysisMode.equals("Cells analysis mode") 
                    || selectedDataDriveAnalysisMode.equals("Clades analysis mode") 
                    || selectedDataDriveAnalysisMode.equals("fluorescencesRB")) {
                vssa = new VertexShapeSizeAspect<Cell,Double>(mapCellToNormLengthValue);
                vssa.setScaling(true);
            } else if(selectedDataDriveAnalysisMode.equals("Branch length")) {
                vssa = new VertexShapeSizeAspect<Cell,Double>(mapCellToNormLengthValue);
                vssa.setScaling(false);
                esa = new EdgeWeightStrokeFunction(mapCladeToNormBranchLengthValue);
                esa.setWeighted(true);
                vv.getRenderContext().setEdgeStrokeTransformer(esa);
            }*/ 
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cell Comparison table is empty. You must select two cells to be compared.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));
        }
//        vv.updateUI();
    }
    
    /**
     * Initialize the mapped lists
     * 
     */
    private void initMappedLists() {
        double maxLengthValue = forest.getMaximalValueLength();
        double minLengthValue = forest.getMinimalValueLength();
        double maxAreaValue = forest.getMaximalValueArea();
        double minAreaValue = forest.getMinimalValueArea();
        double maxFluorescenceYFPValue = forest.getMaximalValueFluorescence( "yfp" );
        double minFluorescenceYFPValue = forest.getMinimalValueFluorescence( "yfp" );
        double maxFluorescenceCRIMSONValue = forest.getMaximalValueFluorescence( "crimson" );
        double minFluorescenceCRIMSONValue = forest.getMinimalValueFluorescence( "crimson" );
        double maxBranchLengthValue = forest.getMaximalValueCladeBranchLength();
        double minBranchLengthValue = forest.getMinimalValueCladeBranchLength();
        double normalizedLengthValue;
        double normalizedAreaValue;
        double normalizedFluorescenceYFPValue;
        double normalizedFluorescenceCRIMSONValue;
        double normalizedCladeBranchLengthValue;
        for(Cell cell : g3.getVertices()) {
            // visualler Vergleich bzgl. die Länge von Zellen
            normalizedLengthValue = forest.normalize(cell.getLength(), minLengthValue, maxLengthValue);
            mapCellToNormLengthValue.put(cell, normalizedLengthValue);
            
            // visualler Vergleich bzgl. die Fläche von Zellen
            normalizedAreaValue = forest.normalize(cell.getArea(), minAreaValue, maxAreaValue);
            mapCellToNormAreaValue.put(cell, normalizedAreaValue);
            
            if ( cell.getFluorescences().get("yfp") != null ) {
                // visualler Vergleich bzgl. die Fluorescence Type 'yfp' von Zellen
                normalizedFluorescenceYFPValue = forest.normalize(cell.getFluorescences().get("yfp"), minFluorescenceYFPValue, maxFluorescenceYFPValue);
                mapCellToNormFluorescenceYFPValue.put(cell, normalizedFluorescenceYFPValue);
            }
            if ( cell.getFluorescences().get("crimson") != null ) {
                // visualler Vergleich bzgl. die Fluorescence Type 'crimson' von Zellen
                normalizedFluorescenceCRIMSONValue = forest.normalize(cell.getFluorescences().get("crimson"), minFluorescenceCRIMSONValue, maxFluorescenceCRIMSONValue);
                mapCellToNormFluorescenceCRIMSONValue.put(cell, normalizedFluorescenceCRIMSONValue);
            }
            
        }
         
        for(Clade clade : g3.getEdges()) {
            // visualler Vergleich bzgl. die branchen-Länge von Klades
            normalizedCladeBranchLengthValue = forest.normalize(clade.getBranchLength(), minBranchLengthValue, maxBranchLengthValue);
            mapCladeToNormBranchLengthValue.put(clade, normalizedCladeBranchLengthValue);
         }
    }

    private List<Double> cellsComparisonBasenOnLength(Cell c1, Cell c2) {
        Double same = 0.0;
        List<Double> temp = new LinkedList<Double>();
        double sumLength1 = c1.getLength();
        double sumLength2 = c2.getLength();
        List<Cell> listC1 = forest.getCellChildrenByCellId(c1.getId());
        for(Cell cell : listC1) 
            sumLength1 += cell.getLength();
        List<Cell> listC2 = forest.getCellChildrenByCellId(c2.getId());
        for(Cell cell : listC2) 
            sumLength2 += cell.getLength();
        if( n.format(sumLength1) == null ? n.format(sumLength2) == null : n.format(sumLength1).equals(n.format(sumLength2)) ) {
            temp.add(1.0);
            temp.add(sumLength1);
            temp.add(sumLength2);
        } else {
            temp.add(0.0);
            temp.add(sumLength1);
            temp.add(sumLength2);
        }
        return temp;
    }
    
   private List<Double> cellsComparisonBasedOnArea(Cell c1, Cell c2) {
       Double same = 0.0;
        List<Double> temp = new LinkedList<Double>();
        double sumArea1 = c1.getArea();
        double sumArea2 = c2.getArea();
        List<Cell> listC1 = forest.getCellChildrenByCellId(c1.getId());
        for(Cell cell : listC1) 
            sumArea1 += cell.getArea();
        List<Cell> listC2 = forest.getCellChildrenByCellId(c2.getId());
        for(Cell cell : listC2)  
            sumArea2 += cell.getArea();
        
        if( n.format(sumArea1) == null ? n.format(sumArea2) == null : n.format(sumArea1).equals(n.format(sumArea2)) ) {
            temp.add(1.0);
            temp.add(sumArea1);
            temp.add(sumArea2);
        }
        return temp;
    }
    

    /**
     * This class implement a Tooltip option to display/show 
     * the informations about a clade.
     * 
     */
    private class CladeInformationTips implements Transformer<Clade,String> {
        public String transform(Clade clade) {
           String tooltipInfo = "<html>\n<head>\n<title>Clade Informations</title>\n</head>\n<body>\n\n<h2>"
                                + clade.getName()+"</h2>\n<p> BranchLength: "
                                + clade.getBranchLength()+"</p>\n<p> Level: "
                                + forest.getPhyloxml().getPhylogenyObject().calculateLevelOfClade(clade)+"</p>\n<p> Depth: "
                                + forest.getPhyloxml().getPhylogenyObject().calculateDepthOfClade(clade);
            return tooltipInfo;
        }
    }
    
    public void createMatrixFromGraph() {
        
        //creating a map of edges
      mapEdgeToCladeBranchLengthValue = new HashMap<Clade,Number>();
      for (Clade edg : g3.getEdges()) {
        mapEdgeToCladeBranchLengthValue.put(edg, edg.getBranchLength());
      }
        
      //populating matrix cells with edge weights, creating weighted matrix
           SparseDoubleMatrix2D adjacencyMatrix = GraphMatrixOperations.graphToSparseMatrix(g3, mapEdgeToCladeBranchLengthValue);
           SparseDoubleMatrix2D diagonalDegreeMatrix = GraphMatrixOperations.createVertexDegreeDiagonalMatrix(g3);
           SparseDoubleMatrix2D laplaceMatrix = new SparseDoubleMatrix2D(adjacencyMatrix.rows(), adjacencyMatrix.columns());
      
//      System.out.println(g3.toString());
//      System.out.println(m2.toString());
//        String title = "Project name: " + phylo.getForest().getMetaxml().getProjectName();
        String columnAxisName = "Cells-ID's";
        String rowAxisName = "Cells-ID's"; 
        List<String> rowNames = new LinkedList<String>();
        List<String> columnNames = new LinkedList<String>();
//        Iterator<Cell> itr = g3.getVertices().iterator();
        /*        String[] rowNames2 = new String[g3.getVertexCount()];
        String[] columnNames2 = new String[g3.getVertexCount()];
        while ( itr.hasNext() ) {
            rowNames.add(itr.next().getId());
         }
        for(int i=0; i<rowNames.size(); i++) {
            rowNames2 [i] = columnNames2 [i] = rowNames.get(i);
        }
        hep.aida.bin.BinFunctions1D F = hep.aida.bin.BinFunctions1D.functions; // alias
//        hep.aida.bin.BinFunction1D[] aggr = {F.mean, F.rms, F.quantile(0.25), F.median, F.quantile(0.75), F.stdDev, F.min, F.max};
        hep.aida.bin.BinFunction1D[] aggr = {F.mean, F.rms, F.stdDev};
        String format = "%1.2G";
//        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(values); 
        System.out.println("" + new Formatter(format).toTitleString(adjacencyMatrix, rowNames2, columnNames2, rowAxisName, columnAxisName, "", aggr));
        System.out.println("");
//        System.out.println(""+ m2.viewSorted(0));
        for (int row = 0; row < adjacencyMatrix.rows(); row++) {
             for (int column = 0; column < adjacencyMatrix.columns(); column++) {
                 System.out.println( row +")"+ adjacencyMatrix.getQuick(row, column));
             }
         }
//         System.out.println(m2.toString());
//         MatrixIO.show();
*/    }
    
    /**
     * Calculates the optimal scale (zoom) for the graph to fit in the window area and applys it
     */
    public void applyPerfectScale() {
    	// calculate the perfect scale so that the whole tree is visible
/*    	double newScale = 1;

        Dimension graphSize = graphComponent.getGraphControl().getSize();
        Dimension viewPortSize = graphComponent.getViewport().getSize();
        
        int gw = (int) graphSize.getWidth();
        int gh = (int) graphSize.getHeight();

        if (gw > 0 && gh > 0) {
            int w = (int) viewPortSize.getWidth();
            int h = (int) viewPortSize.getHeight();

            newScale = Math.min((double) w / gw, (double) h / gh);
        }

        // apply scale to phylo tree
        graphComponent.zoom(newScale);*/
        
		// apply the minimal zoom so that the biggest part of the graph is
		// visible (note: dynamical calculations failed because the window is
		// sometimes not initialized at that point.)
        graphComponent.zoom(0.04);
    }
    
    public void applyLayout(mxGraph graph) {
		// define layout
        MyCompactTreeLayout layout = new MyCompactTreeLayout(graph, bHorizontal);

		// set some layout specific features
        layout.setLevelDistance(50);
        layout.setMaxNodeSize(maxNodeSize);
        
	    // apply the layout to the graph
	    layout.execute(graph.getDefaultParent());
	}
    
    /**
	 * Updates the whole graph. Vertex size is calculated linear by the normed
	 * value defined in the map (0.0 - 1.0)
	 * 
	 * @param mapVertexSizeNormed Map with the normed vertex size
	 */
    private void updateVertexSize(Map<Cell, Double> mapVertexSizeNormed) {
    	mxGraph newGraph = new MyGraph();
    	
    	Object defaultParent = newGraph.getDefaultParent();
    	newGraph.getModel().beginUpdate();
		try
		{
			/*
			 * Clades are objects that only contain structural information from
			 * phyloXML files. Cellular characteristics are stored in Cell
			 * objects.
			 */
			Clade rootClade = phylogeny.getRootClade();
			Cell rootCell = rootClade.getCellObject();
			
			/* Iterate the phylogeny in a depth-first manner */
			Iterator<Cell> phylogenyIter = rootCell.iterator();
			
			while (phylogenyIter.hasNext()) {
				Cell cell = phylogenyIter.next();
				Cell parent = cell.getParentCell();
				
				mxCell oldVertex = ((MyModel)graph.getModel()).getVertex(cell.getId());
				
				Object cellVertex = newGraph.insertVertex(defaultParent, cell.getId(), cell, 0, 0, std.getWidth() + (max.getWidth() - std.getWidth()) * mapVertexSizeNormed.get(cell), std.getHeight() + (max.getHeight() - std.getHeight()) * mapVertexSizeNormed.get(cell), oldVertex.getStyle());
				
			}
			
			addEdges(newGraph, rootCell, sorter);
			
		   // set cells unresizeable
		   newGraph.setCellsResizable(false);
		   // set cells not disconnectable (now edges are not movable anymore)
		   newGraph.setCellsDisconnectable(false);
		   // do not move edge labels (that does not make sense :))
		   newGraph.setEdgeLabelsMovable(false);
		   newGraph.setCellsEditable(false);

		   setDefaultStyle(newGraph);
		}
		finally
		{
			newGraph.getModel().endUpdate();
		}
		
		maxNodeSize = max.height;
		
		applyLayout(newGraph);
	    
	    graphComponent.setGraph(newGraph);
	    
	    // set new graph in the object
	    graph = newGraph;
	    
	    // the scale changes also when the graph changes. At first aplly a new scale
	    applyPerfectScale();
	}

    
    /**
     * Applies a new sort to the graph
     * @param comp The comparator for the sort
     */
    public void updateGraphSort(Comparator<Cell> comp) {
    	sorter = comp;
    	
    	mxGraph newGraph = new MyGraph();
    	
    	Object defaultParent = newGraph.getDefaultParent();
    	newGraph.getModel().beginUpdate();
		try
		{
			/*
			 * Clades are objects that only contain structural information from
			 * phyloXML files. Cellular characteristics are stored in Cell
			 * objects.
			 */
			Clade rootClade = phylogeny.getRootClade();
			Cell rootCell = rootClade.getCellObject();
			
			/* Iterate the phylogeny in a depth-first manner */
			Iterator<Cell> phylogenyIter = rootCell.iterator();
			
			while (phylogenyIter.hasNext()) {
				Cell cell = phylogenyIter.next();
				Cell parent = cell.getParentCell();
				
				mxCell oldVertex = ((MyModel)graph.getModel()).getVertex(cell.getId());
				
				Object cellVertex = newGraph.insertVertex(defaultParent, cell.getId(), cell, 0, 0, oldVertex.getGeometry().getWidth(), oldVertex.getGeometry().getHeight(), oldVertex.getStyle());
				
			}
			
			// add the edges in the sort order
			addEdges(newGraph, rootCell, comp);
			
		   // set cells unresizeable
		   newGraph.setCellsResizable(false);
		   // set cells not disconnectable (now edges are not movable anymore)
		   newGraph.setCellsDisconnectable(false);
		   // do not move edge labels (that does not make sense :))
		   newGraph.setEdgeLabelsMovable(false);
		   newGraph.setCellsEditable(false);

		   setDefaultStyle(newGraph);
		}
		finally
		{
			newGraph.getModel().endUpdate();
		}

		applyLayout(newGraph);
	    
	    graphComponent.setGraph(newGraph);
	    
	    // set new graph in the object
	    graph = newGraph;
	    
	    // the scale changes also when the graph changes. At first aplly a new scale
	    applyPerfectScale();
	}
    
    /**
     * Color the specified cell in the graph
     * 
     * @param cell to color
     * @param color for the cell
     */
    public void colorVertex(Cell cell, Color color) {
		graph.getModel().beginUpdate();
    	try
    	{
    		mxCell vertex = ((MyModel)graph.getModel()).getVertex(cell.getId());
    		Style newStyle = new Style(vertex.getStyle());
    		newStyle.put("fillColor", color);
    		newStyle.put("strokeColor", color);
    		graph.getModel().setStyle(vertex, newStyle.toString());
    	}
    	finally {
			graph.getModel().endUpdate();
    	}
    }
    
    /**
     * Colors the specified cell and its predecessors in the graph
     * @param cell to color (and the predecessors)
     * @param color to use
     */
    public void colorVertexAndPredecessors(Cell cell, Color color) {
		graph.getModel().beginUpdate();
    	try {
    		colorVertex(cell, color);
    		if(cell.getParentCell() != null)
    			colorVertexAndPredecessors(cell.getParentCell(), color);
    	}
    	finally {
			graph.getModel().endUpdate();
    	}
    }
    
    /**
     * Color all cells in the graph with the specified color
     * 
     * @param color to use
     */
    public void colorAllVertices(Color color) {
    	graph.getModel().beginUpdate();
    	try
    	{
    		for(Cell cell : g3.getRoot()) {
    			colorVertex(cell, color);
    		}
    	}
    	finally {
    		graph.getModel().endUpdate();
    	}
    }
    
    /**
     * Colors a specified edge
     * @param edge to color
     * @param color for the edge
     */
    public void colorEdge(mxCell edge, Color color)
    {
    	try {
    		graph.getModel().beginUpdate();
    		Style currentStyle = new Style(edge.getStyle());
    		currentStyle.put("fillColor", color);
    		currentStyle.put("strokeColor", color);
    		graph.getModel().setStyle(edge, currentStyle.toString());
    	}
    	finally {
    		graph.getModel().endUpdate();
    	}
    }
    
    /**
     * Colors all edges on the path from the root to the cell
     * @param cell to end up
     * @param color for all the edges
     */
    public void colorEdgesAndPredecessors(Cell cell, Color color) {
    	if(cell == null || cell.getParentCell() == null)
    		return;
    	try {
    		graph.getModel().beginUpdate();
    		Cell parent = cell.getParentCell();
    		mxCell edge = ((MyModel)graph.getModel()).getEdge(parent.getId()+cell.getId());
	    	colorEdge(edge, color);
	    	colorEdgesAndPredecessors(cell.getParentCell(), color);
    	}
    	finally {
    		graph.getModel().endUpdate();
    	}
    }
    
    /**
     * 
     * 
     * @param <E> 
     */
    private final static class EdgeWeightStrokeFunction<E>
    implements Transformer<E,Stroke> {
        protected static final Stroke basic = new BasicStroke(1);
        protected static final Stroke heavy = new BasicStroke(2);
        protected static final Stroke dotted = RenderContext.DOTTED;
        
        protected boolean weighted = false;
        protected Map<Clade,Double> mapCladeToCharacterValue;
        
        public EdgeWeightStrokeFunction(Map<Clade,Double> mapCladeToCharacterValue) {
            this.mapCladeToCharacterValue = mapCladeToCharacterValue;
        }
        
        public void setWeighted(boolean weighted) {
            this.weighted = weighted;
        }
        
        public Stroke transform(E e) {
            if (weighted)
                return new BasicStroke((mapCladeToCharacterValue.get(e).floatValue() * 2));
            else
                return basic;
        }
    }

    /**
     * 
     * @return whether the phylo is horizontally displayed (true) or vertically (false)
     */
	public boolean isHorizontalLayout() {
		return bHorizontal;
	}

	/**
	 * Sets the graph layout
	 * @param bHorizontal if true the horizontal layout is applied else there will be the vertical one
	 */
	public void setGraphLayout(boolean bHorizontal) {
		this.bHorizontal = bHorizontal;
		graph.setCellsMovable(true);
		applyLayout(graph);
		graph.setCellsMovable(false);
	}
}
