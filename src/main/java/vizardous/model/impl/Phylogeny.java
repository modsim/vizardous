/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a Phylogeny.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0.0
 */
public class Phylogeny {
   
   /** TODO Documentation */
   private Forest forest = null; 
   
   /** TODO Documentation */
   private List<Clade> cladeList = null;

   /** TODO Documentation */
   private HashMap<String, Clade> nameToCladeMapping = new HashMap<String, Clade>();
   
   /** TODO Documentation */
   private HashMap<Double, List<Clade>> branchLengthMapping = new HashMap<Double, List<Clade>>();
   
   /**
    * Phylogeny constructor.
    * 
    * @param forest TODO Documentation
    */
   public Phylogeny(Forest forest) { 
       this.forest = forest;
       this.cladeList = new ArrayList<Clade>();
   }
  
    /**
     * Gets the root {@link Clade} of the phylogeny.
     * 
     * @return Returns root {@link Clade} of this phylogeny.
     */
    public Clade getRootClade() {
        return cladeList.get(0);
    }
    
    /**
     * Gets the root {@link Cell} of the phylogeny.
     * 
     * @return Returns root {@link Cell} of this phylogeny.
     */
    public Cell getRootCell() {
        Clade rootClade = this.getRootClade();    	
    	return rootClade.getCellObject();
    }
    
   /**
    * Add a new clade to the list of clades.
    * 
    * @param cladeObject Clade object to set.
    */
    public void addClade(Clade cladeObject){
        cladeList.add(cladeObject);
        
        nameToCladeMapping.put(cladeObject.getName(), cladeObject);
        
        // Keep track of Clades with the same branch length
        double branchLength = cladeObject.getBranchLength();
        List<Clade> cladesWithFittingBranchLength = branchLengthMapping.get(branchLength);
        
        if (cladesWithFittingBranchLength == null) {
        	cladesWithFittingBranchLength = new LinkedList<Clade>();
        	branchLengthMapping.put(branchLength, cladesWithFittingBranchLength);
        }
        
        cladesWithFittingBranchLength.add(cladeObject);
    }
        
    /**
     * Gets all clades contain in phylogeny.
     * 
     * @return Returns a list of clades in this phylogeny.
     */
    public List<Clade> getAllCladeInPhylogeny(){
        return cladeList;
    }
    
    /**
     * Find clade by given name of this clade.
     * 
     * @param name The name of the searching clade.
     * @return clade Returns the searched clade. 
     */
    public Clade getCladeByName(String name){
        return nameToCladeMapping.get(name);
    }
    
    /**
     * Gets all clades those have the same branch length.
     * 
     * @param branchLengthValue Branch length value.
     * @return  Returns a list of clades with the given branch length.
     */
    public List<Clade> getCladeByBranchLength(double branchLengthValue){
        return branchLengthMapping.get(branchLengthValue);
    }

    /**
     * Gets all descendants of the given clade.
     * 
     * @param actuallClade The given clade.
     * @param cladeChildrensList A empty list. 
     * @return Returns a list of all descendants of the given clade.
     */
    public ArrayList<Clade> getAllChildrenOfClade(Clade actuallClade, ArrayList<Clade> cladeChildrenList) {
     
            if(actuallClade!= null) {
                ArrayList<Clade> childrenList = actuallClade.getSubCladeList();
                
                for(int i=0; i<childrenList.size(); i++) {
                    if(childrenList.get(i).getName() != null) {
                        cladeChildrenList.add(childrenList.get(i));
                        getAllChildrenOfClade(childrenList.get(i), cladeChildrenList); 
                    }
                    else 
                        return cladeChildrenList;
                }
            }
            return cladeChildrenList;
    }
   
    /**
     * Gets all children/descendants of the given clade.
     * 
     * @param cladeName The name of clade.
     * @return Returns a list of all children/descendants of the given clade name.
     */
    public ArrayList<Clade> getAllChildrenOfClade(String cladeName){
        ArrayList<Clade> clListe = new ArrayList<Clade>();
        ArrayList<Clade> childrenCladeListe = new ArrayList<Clade>();
        
        Clade parentClade = getCladeByName(cladeName);
        ArrayList<Clade> subclListe = parentClade.getSubCladeList();
        for(int i=0; i<subclListe.size(); i++) {
            childrenCladeListe.add(subclListe.get(i));
        }
        
        for(int j=0; j<childrenCladeListe.size(); j++) {
            clListe.add(childrenCladeListe.get(j));
        }
        
        return clListe;
    }
    
    /**
     * Gets all parents/ancestors of the given clade.
     * 
     * @param actuallClade The given clade.
     * @param cladeParentsList A empty list.
     * @return Returns a list of all parents/ancestors of the given clade.
     */
    public ArrayList<Clade> getCladeParents(Clade actuallClade, ArrayList<Clade> cladeParentsList) {
            if ( actuallClade != null ) {
                Clade parent = actuallClade.getParentClade();
                if ( parent != null ) {
                    cladeParentsList.add(parent);
                    getCladeParents(parent, cladeParentsList); 
                }
                else 
                    return cladeParentsList;
            }
            return cladeParentsList;
    }

    /**
     * Gets all leaves existing in the Phylogeny.
     * 
     * @return Returns a list of all clades leaves contain in this Phylogeny.
     */
    public List<Clade> getLeaves() {
    	List<Clade> clades = getAllCladeInPhylogeny();
        List<Clade> leaves = new ArrayList<Clade>();

        for(Clade clade : clades) {
        	if(clade.isLeaf()) {
                leaves.add(clade);
            }
        }
        
        return leaves;
    }
    
    /**
     * Gets the number of clades without children/descendants.
     * 
     * @return Returns number of leaves, or <code>null</code>
     * @throws <code>IllegalArgumentException</code> for none-leaves. 
     */
    public int numberOfCladeLeaf() {
        int nb = 0;
        if ( getLeaves() == null )
            throw new IllegalArgumentException("The list is empty!");
        else
            nb = getLeaves().size();
        
        return nb;
    }

    /**
     * Gets the number of level of Phylogeny.
     * 
     * @return Returns the maximal number of level in Phylogeny.
     */
    public int numberOfLevel() {
        int maxLevelNumber = 0, levelNumber = 0;
        List<Clade> cladesListe = getLeaves();
        
        for(int i = 0; i < cladesListe.size(); i++) {
            ArrayList<Clade> tempList =  new ArrayList<Clade>();
            tempList = getCladeParents(cladesListe.get(i), tempList);
            levelNumber = tempList.size()+1;
            if(levelNumber > maxLevelNumber)
                maxLevelNumber = levelNumber;
        }
        return maxLevelNumber;
     } 
      
    /**
     * calculate the level/height of the clade.
     * 
     * @param clade Clade object.
     * @return Level of the given clade in this Phylogeny.
     */
    public int calculateLevelOfClade(Clade clade) {
        int level, temp, numb, count = 0;

        if( clade.isLeaf() ) {
            level = 0;
        } else {
            ArrayList<Clade> list = getAllChildrenOfClade(clade, new ArrayList<Clade>());
            for(int i=0; i<list.size(); i++) {
                temp = getCladeParents(list.get(i), new ArrayList<Clade>()).size(); 
                if(count < temp) 
                    count = temp;
            }
            numb = getCladeParents(clade, new ArrayList<Clade>()).size();
            level = count - numb;
        }
     return level+1;       
    }
    
    /**
     * calculate the depth of the clade.
     * 
     * @param clade Clade object.
     * @return Depth of the given clade in this Phylogeny.
     */
    public int calculateDepthOfClade(Clade clade) {
        int depth, count;
        
        count = getCladeParents(clade, new ArrayList<Clade>()).size();
        if( clade.isRoot() ) {
            depth = 0;
        } else 
            depth = count;
            
     return depth+1;       
    }
    
   /**
    * Gets the {@link Forest} that contains this {@link Phylogeny}.
    * 
    * @return the {@link Forest} object that contains this {@link Phylogeny}.
    */ 
   public Forest getForest() {
       return this.forest;
   }
    
}
