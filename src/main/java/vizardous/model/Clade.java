/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model;

import java.util.ArrayList;

/**
 * Class the represents a phyloXML Clade.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0.0
 */

public class Clade {

    private String              name            = null;
    private double              branchLength    = -1;
    private Clade               parentClade     = null; 
    private ArrayList<Clade>    subCladeList    = null;
    private Cell                cellObject      = null;
    private Phylogeny           phylogenyObj    = null;

    /**
     * Constructor
     * 
     * @param phylogenyObj 
     */
     
    public Clade(Phylogeny phylogenyObj) {
        this.phylogenyObj = phylogenyObj;
        subCladeList = new ArrayList<Clade>();
    }
    
    /***
     * Constructor
     * 
     * @param name the name of clade
     * @param branchLength the length of clade
     * @param parentClade the ancestor of clade
     * @param cellObject the cell associated to the clade
     */
    public Clade(String name, double branchLength, Clade parentClade, Cell cellObject) {
        this.name           = name;
        this.branchLength   = branchLength;
        this.parentClade    = parentClade;
        this.cellObject     = cellObject;
        subCladeList        = new ArrayList<Clade>();
        
    }
       
    /**
     * Gets the name of clade.
     * 
     * @return Returns the name of the clade, or <code>null</code> no name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of clade.
     * 
     * @param the name of the clade, or <code>null</code>
     * @throws <code>IllegalArgumentException</code> for none-name.
     */
    public void setName(String name) {
        if( name == null ) 
            throw new IllegalArgumentException("The clade name to set is null!");
        else
            this.name = name;
    }

    /**
     * Gets the branch length of this clade.
     * 
     * @return Returns the branch length of the clade, or <code>null</code> no name.
     */
    public double getBranchLength() {
        return branchLength;
    }

    /**
     * Sets the name of clade.
     * 
     * @param branchLength The branch length value to set.
     */
    public void setBranchLength(double branchLengthValue) {
        this.branchLength = branchLengthValue;
    }
    
    /**
     * Gets the direct sub-clades.
     * 
     * @return Returns a list of the direct sub-clades of this clade.
     */
    public ArrayList<Clade> getSubCladeList() {
        return subCladeList;
    }
  
    /**
     * Add a sub-clade to the clade.
     * 
     * @param clade The clade to add.
     */
    public void addSubClade(Clade subClade){
        subCladeList.add(subClade);
    }
    
    /**
     * Gets the direct ancestor of this clade.
     * 
     * @return the direct ancestor of the clade, or <code>null</code> no name.
     */
    public Clade getParentClade() {
        return parentClade;
    }
    
    /**
     * Sets the parent of clade.
     * 
     * @param pClade The clade to set.
     */
    public void setParentClade(Clade pClade){
        this.parentClade = pClade;
    }

    /**
     * Gets the associated cell to this clade.
     * 
     * @return Returns the cell associate to the clade.
     */
    public Cell getCellObject() {
        return cellObject;
    }

    /**
     * Sets the associated cell to this clade.
     * 
     * @param cellObject The cell to set.
     */
    public void setCellObject(Cell cellObject) {
        this.cellObject = cellObject;
    }

    /**
     * Checks whether a clade is a leaf.
     *
     * @return True, if no subclades are found. Otherwise, false.
     */
    public boolean isLeaf() {
            return (this.subCladeList.size() == 0);
    }     
    
     /**
     * Checks whether a clade is a root.
     *
     * @return True, if no parent/ancestor clade are found. Otherwise, false.
     */
    public boolean isRoot() {  
        boolean isRoot = false; 
        if( this.getParentClade() == null )
             isRoot = true;
        
        return isRoot;
    }
    
    /**
    * Gets the phylogeny which contains this clade.
    * 
    * @return Returns the phylogeny object.
    */
    public Phylogeny getPhylogenyObj() {
        return phylogenyObj;
    }

    /**
     * Sets the phylogeny which contains this clade.
     * 
     * @param phylogenyObj Phylogeny object to set.
     */
    public void setPhylogenyObj(Phylogeny phylogenyObj) {
        this.phylogenyObj = phylogenyObj;
    }
    
}
