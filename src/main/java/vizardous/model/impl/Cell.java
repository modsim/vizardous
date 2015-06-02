/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.impl;

import edu.uci.ics.jung.graph.DelegateTree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vizardous.model.computations.CellularStateInterface;

/**
 * This class represents a cell/node of a phylogenetic tree in the Vizsardous data model.   
 *
 * @author Charaf E. Azzouzi <c.azzuzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0.0
 */
public class Cell implements Iterable<Cell> {
    
	// not initialised data
	public static final double noLength = -1d;
	public static final double noArea = -1d;
	public static final double noFluorescence = -1d;
	
    private String      id               = "no id";

    private double      area             = noArea; //Double.parseDouble("n.a");
    private double      fluorescence     = noFluorescence; //Double.parseDouble("n.a");
    private double      length           = noLength; //Double.parseDouble("n.a");
    private String      lengthUnit       = "N/A";
    private String      areaUnit         = "N/A";
    private String      fluorescenceUnit = "N/A";
    private MIFrame     frameObject      = null;
    private Clade       cladeObject      = null;
    private Color       cellColor        = null;
    private DelegateTree<Cell, Clade> treeGraph = null;
    private double x = -1;
    private double y = -1;
    private double z = -1;
    private Map<String, Double> fluorescences = null;
    private CellularStateInterface state = null;
    
   
    /**
     * TODO Documentation
     * 
     * @param builder
     */
    public Cell(Builder builder) {
    	this.id = builder.cellId;
    	this.cladeObject = builder.cladeObject;
        this.frameObject = builder.frameObject;
    	
    	this.x = builder.x;
        this.y = builder.y;
        this.z = builder.z;
    	this.length = builder.length;
        this.lengthUnit = builder.lengthUnit;
        this.area = builder.area;
        this.areaUnit = builder.areaUnit;
        
        if (builder.cladeObject != null) {
        	this.cladeObject.setCellObject(this);
        }
        
        this.fluorescences = builder.fluorescences;
        this.fluorescenceUnit = builder.fluorescenceUnit;
    }
    

    /**
     * Constructs a new <code>Cell</code> object.
     * 
     * @param   cellId  cell/node id
     * @param   length length of cell
     * @param   lengthUnit unit of length of cell [µm]
     * @param   area area of cell
     * @param   areaUnit unit of area of cell [mm]
     * @param   fluorescence fluorescence value in cell
     * @param   fluorescenceUnit unit of fluorescence value of cell [arbitrary]
     * @param   frameObject object of MIFrame class
     * @param   cladeObject object of Clade class
     */
    @Deprecated
    public Cell(String cellId, double length, String lengthUnit, double area, String areaUnit, Map<String, Double> fluorescences, String fluorescenceUnit, MIFrame frameObject, Clade cladeObject) {
        this.id                 = cellId;
        this.length             = length;
        this.lengthUnit         = lengthUnit;
        this.area               = area;
        this.areaUnit           = areaUnit;
        this.frameObject        = frameObject;
        this.cladeObject        = cladeObject;
        
        if (cladeObject != null) {
        	this.cladeObject.setCellObject(this);
        }
        
        this.fluorescences      = fluorescences;
        this.fluorescenceUnit   = fluorescenceUnit;
    }

    /**
     * Gets the id of cell/node.
     * 
     * @return Returns the id of cell/node.
     */
    public String getId() {
        return id;
    }

    /**
    * Gets the length of cell.
    * 
    * @return Returns the length of cell.
    */
    public double getLength() {
        return length;
    }

    /**
    * Gets the area of cell.
    * 
    * @return Returns the area of cell.
    */
    public double getArea() {
        return area;
    }

    
    /**
    * Gets the unit of length of cell.
    * 
    * @return Returns the unit of length of cell.
    */
    public String getLengthUnit() {
        return lengthUnit;
    }

    /**
    * Gets the unit of area of cell.
    * 
    * @return Returns the unit of area of cell.
    */
    public String getAreaUnit() {
        return areaUnit;
    }

    /**
    * Gets the unit of fluorescence value of cell.
    * 
    * @return Returns the unit of fluorescence value of cell.
    */
    public String getFluorescenceUnit() {
        return fluorescenceUnit;
    }

    /**
    * Gets the associated clade of cell.
    * 
    * @return Returns the associated clade of cell.
    */
    public Clade getCladeObject() {
        return cladeObject;
    }
    
    /**
    * Gets the associated metaXML-frame of cell.
    * 
    * @return Returns the associated metaXML-frame of cell.
    */
    public MIFrame getMIFrameObject(){
        return frameObject;
    }
    
    /**
     * Sets the id of cell/node.
     * 
     * @param cellId the fluorescences to set.
     */
    public void setId(String cellId) {
        this.id = cellId;
    }
   
    /**
     * Sets the length of cell.
     * 
     * @param length the length value to set.
     */
    public void setLength(double length) {
        this.length = length;
    }
    
    /**
     * Sets the area of cell.
     * 
     * @param area area value to set.
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * Sets the unit of length of cell.
     * 
     * @param lengthUnit the unit of length to set.
     */
    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = lengthUnit;
    }
    
    /**
     * Sets the unit of area of cell.
     * 
     * @param areaUnit the unit of area to set.
     */
    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    /**
     * Sets the unit of fluorescence value of cell.
     * 
     * @param fluorescenceUnit the unit of fluorescence to set.
     */
    public void setFluorescenceUnit(String fluorescenceUnit) {
        this.fluorescenceUnit = fluorescenceUnit;
    }
   
    /**
     * Sets the associated clade.
     * 
     * @param cladeObject the clade object to set.
     */
    public void setCladeObject(Clade cladeObject) {
        this.cladeObject = cladeObject;
    }
    
    /**
     * Sets the associated metaXml-frame.
     * 
     * @param frameObject the metaXml-frame object to set.
     */
    public void setMIFrameObject(MIFrame frameObject) {
        this.frameObject = frameObject;
    }
    
    /**
     * Gets all fluorescences of the cell.
     * 
     * @return Returns all fluorescences of the cell [yfp, crimson].
     */
    public Map<String, Double> getFluorescences() {
    	return fluorescences;
    }

    /**
     * Sets all fluorescences of the cell.
     * 
     * @param fluorescences the fluorescences to set.
     */
    public void setFluorescences(Map<String, Double> fluorescences) {
    	this.fluorescences = fluorescences;
    }

    /**
    * Gets the tree graph which contains this cell/node.
    * 
    * @return Returns the tree graph which contains this cell/node.
    */
    public DelegateTree<Cell, Clade> getPhyloTreeGraph() {
        return treeGraph;
    }

    /**
    * Sets the tree graph which contains this cell/node
    * 
    * @param treeGraph the tree graph which contains this cell/node
    */
    public void setPhyloTreeGraph(DelegateTree<Cell, Clade> treeGraph) {
        this.treeGraph = treeGraph;
    }
    
    /**
     * Gets the x-position of cell.
     * 
     * @return Returns the position of cell in x-axis.
     */
    public double getX() {
            return x;
    }

    /**
     * Sets the x-position of cell.
     * 
     * @param x the x to set.
     */
    public void setX(double x) {
            this.x = x;
    }

    /**
     * Gets the y-position of cell.
     * 
     * @return Returns the position of cell in y-axis. 
     */
    public double getY() {
            return y;
    }

    /**
     * Sets the y-position of cell. 
     * 
     * @param y the y to set.
     */
    public void setY(double y) {
            this.y = y;
    }

    /**
     * Gets the z-position of cell. 
     * 
     * @return Returns the position of cell in z-axis. 
     */
    public double getZ() {
            return z;
    }

     /**
     * Sets the z-position of cell.
     * 
     * @param z the y to set.
     */
    public void setZ(double z) {
            this.z = z;
    }

   /**
    * Gets direct children of cell.
    * 
    * @return Returns a list of children of this cell.
    */
    public List<Cell> getChildren(){
        if (cladeObject == null) 
        	throw new IllegalStateException("Cell does not have a Clade.");
    	
    	List<Cell> cells = new ArrayList<Cell>();
        List<Clade> clades = cladeObject.getSubCladeList();
            
        for(Clade clade : clades) {
            Cell c = clade.getCellObject(); 
            
            // HACK!
            if (c == null) 
                c = new DummyCell(null, 0.0, "N/A", 0.0, "N/A", new HashMap<String, Double>(), "N/A", null, clade);

            cells.add(c);
        }
        
        return cells;
    }
    
    /**
     * Gets the parent of cell.
     * 
     * @return c, which is either the cell's parent or null.
     */
    public Cell getParentCell() {
        Clade clade = cladeObject.getParentClade();
        Cell cell = (clade != null) ? clade.getCellObject() : null;
        
        return cell;
    }
    
    /**
     * Checks whether the cell has a sibling or not.
     * 
     * @return Returns true if the cell has a sibling. Otherwise false.
     */
    public boolean hasSiblings() {
    	if (this.getParentCell() == null) {
    		return false;
    	}
    	
    	return this.getParentCell().getChildren().size() > 1;
    }
    
    /**
     * The iterator walks the lineage tree in depth-first order.
     * 
     * @return an iterator for the subtree rooted at the node from which it is called.
     */
    public Iterator<Cell> iterator() {
    	return new DFSTreeIterator(this);
    }
    
    /**
     * The iterator walks the lineage tree reversely to the root.
     * 
     * @return an instance of ReverseTreeIterator<Cell>
     */
    public Iterator<Cell> reverseIterator() {
    	return new ReverseTreeIterator(this);
    }

    // TODO Implement iterator in the direction of the root.
    
    /**
     * Checks whether a cell is a leaf.
     *
     * @return True, if no children are found, otherwise, false.
     */
    public boolean isLeaf() {
    	return getChildren().isEmpty();
    }
    
    /**
     * Checks whether this cell is a root node or not.
     * 
     * @return true if the node a root, otherwise false.
     */
    public boolean isRoot() {
    	return (getParentCell() == null) || (getCladeObject().getParentClade() == null);
    }
    
    /**
     * Gets all leaves attached to cell.
     * 
     * @return Returns a list of cells/nodes which are a leaves and they have this cell as a common ancestor.
     */
    public List<Cell> getAttachedLeaves() {
    	List<Cell> leaves = new LinkedList<Cell>();
		
		Iterator<Cell> iter = this.iterator();		
		while (iter.hasNext()) {
			Cell c = iter.next();
			
			if ( c.isLeaf() ) {
				leaves.add(c);
			}
		}
		
		return leaves;
    }
    
    /**
     * Gets the direct siblings of this cell. The list does not include the
     * cell itself!
     * 
     * @return A list of direct siblings without the cell itself.
     */
    public List<Cell> getSiblings() {
    	if (this.getParentCell() == null) {
    		return new ArrayList<Cell>();
    	}
    	
    	List<Cell> siblings = new ArrayList<Cell>(this.getParentCell().getChildren());
    	siblings.remove(this);
    	
    	return siblings;
    }

   /**
    * Gets the cell/node color.
    * 
    * @return Returns the color of cell.
    */
    public Color getCellColor() {
        return cellColor;
    }

    /**
     * Sets the cell/node color.
     * 
     * @param cellColor The color to set.
     */
    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
    }

    /**
    * Gets the state of cell.
    * 
    * @return Returns the state of cell.
    */
    public CellularStateInterface getState() {
            return state;
    }

    /**
     * Sets the state of cell.
     * 
     * @param cellColor The state to set.
     */
    public void setState(CellularStateInterface state) {
            this.state = state;
    }

    /**
     * TODO Documentation
     * 
     * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
     * @version 1.0.0
     */
    public static class Builder {
            // Required parameters
            private final String cellId;
            private final Clade cladeObject;
            private final MIFrame frameObject;

            // Optional parameters
            private double x = -1d;
            private double y = -1d;
            private double z = -1d;
            private double length = -1d;
            private String lengthUnit = "px";
            private double area = -1;
            private String areaUnit = "px";
            private Map<String, Double> fluorescences = new HashMap<String, Double>();
            private String fluorescenceUnit = "au";

            public Builder(String cellId, Clade cladeObject, MIFrame frameObject) {
                    this.cellId = cellId;
                    this.cladeObject = cladeObject;			
                    this.frameObject = frameObject;
            }

            public Builder x(double x) {
                    this.x = x;
                    return this;
            }

            public Builder y(double y) {
                    this.y = y;
                    return this;
            }

            public Builder z(double z) {
                    this.z = z;
                    return this;
            }

            public Builder length(double length) {
                    this.length = length;
                    return this;
            }

            public Builder lengthUnit(String lengthUnit) {
                    this.lengthUnit = lengthUnit;
                    return this;
            }

            public Builder area(double area) {
                    this.area = area;
                    return this;
            }

            public Builder areaUnit(String areaUnit) {
                    this.areaUnit = areaUnit;
                    return this;
            }

            public Builder fluorescences(Map<String, Double> fluorescences) {
                    this.fluorescences = fluorescences;
                    return this;
            }

            public Builder fluorescenceUnit(String fluorescenceUnit) {
                    this.fluorescenceUnit = fluorescenceUnit;
                    return this;
            }

            public Cell build() {
                    return new Cell(this);
            }
    }

}
