/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.analysis;

import edu.uci.ics.jung.graph.DelegateTree;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Forest;
import vizardous.model.Phylogeny;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author azzouzi
 */
public class PhyloTreeStatistics {

    private Forest forest = null;
    private Phylogeny phyloTree = null;
    private DelegateTree graph = null;
    private DecimalFormat df = null;
    
    
    public PhyloTreeStatistics(Forest forest, DelegateTree graph ) {
        this.forest = forest;
        this.graph  = graph;
    }
    
    public PhyloTreeStatistics(Phylogeny phyloTree, DelegateTree graph) {
        this.phyloTree  = phyloTree;
        this.graph      = graph;
        df = new DecimalFormat("#,###,##0.000");
        calculateCollnessIndex();
        calculateSackinIndex();
        calculateNumberOfCherries();
        calculateHeightOfTree();
        calculateDegreeOfTree();
        getNumberOfLeafsInTree();
        getNumberOfInternalNodesInTree();
        getDegreeDistribution();
        
    }
    
    public double calculateCollnessIndex() {
        double collessIndex = 0;
        int numOfTips = 0;
        int count = 0;
        
        for ( Clade clade : phyloTree.getAllCladeInPhylogeny() ) {
            if ( (!clade.isRoot()) && (!clade.isLeaf()) ) {
                if (clade.getSubCladeList().size() == 2) {
                    
                    int tips0 =  clade.getSubCladeList().get(0).getCellObject().getAttachedLeaves().size();
                    int tips1 =  clade.getSubCladeList().get(1).getCellObject().getAttachedLeaves().size();
                
                    collessIndex += Math.abs(tips0-tips1);
                    count++;
                }
            }
        }
        
        numOfTips = phyloTree.getLeaves().size();
        collessIndex = collessIndex / ((numOfTips - 1) * (numOfTips - 2) / 2);
        
        System.out.println("");
//        System.out.print("Colless Index: " + collessIndex);
        System.out.print("Colless: " + df.format(collessIndex));
        
        return collessIndex;
    }
    
    public double calculateSackinIndex() {
        double sackinIndex = 0;
        
        for (Clade clade : phyloTree.getLeaves())
            sackinIndex += phyloTree.getCladeParents(clade, new ArrayList<Clade>()).size();
        
//        System.out.print("; Sackin's Index: " + sackinIndex);
        System.out.print("; Sackin: " + sackinIndex);
       
        return sackinIndex;
    }
    
    public double calculateNumberOfCherries() {
        double numberOfCherries = 0;
        
        for (Clade clade : phyloTree.getLeaves())
            if( clade.getCellObject().hasSiblings() ) 
                    numberOfCherries ++;
        
//        System.out.print("; Number of Cherries: " + numberOfCherries/2);   
        System.out.print("; Cherries: " + numberOfCherries/2);   
        
        return numberOfCherries/2;
    }

    
    public int calculateHeightOfTree() {
        int treeheight = 0;
        Collection<Cell> cellList =  graph.getVertices();
        for(Cell v : cellList ) 
                treeheight = Math.max(treeheight, graph.getDepth(v));
        
//        System.out.print("; Height of tree: " + treeheight); 
        System.out.print("; Height: " + treeheight); 
        
        return treeheight;
    }
    
    public double calculateDegreeOfTree() {
        double treeDegree = 0;
        
        Collection<Cell> cellList =  graph.getVertices();
        for(Cell v : cellList ) 
                treeDegree = Math.max(treeDegree, graph.degree(v));
        
        
//        System.out.print("; Degree of tree: " + treeDegree);
         System.out.print("; Degree: " + treeDegree); 
        
        return treeDegree;
    }
    
    public int getNumberOfLeafsInTree() {
        int leafsNumber = 0;
        
        leafsNumber = phyloTree.getRootClade().getCellObject().getAttachedLeaves().size();
        
//        System.out.print("; Numb of leafs: " + leafsNumber);
         System.out.print("; leafs: " + leafsNumber); 
        
        return leafsNumber;
    }
    
    public int getNumberOfInternalNodesInTree() {
        int internalNodesNumber = 0;
        
        for ( Clade clade : phyloTree.getAllCladeInPhylogeny() ) 
            if ( !clade.isRoot() && !clade.isLeaf() ) 
                    internalNodesNumber ++;
        
//        System.out.print("; Numb of internal nodes: " + internalNodesNumber); 
        System.out.print("; internal: " + internalNodesNumber); 
        
        return internalNodesNumber;
    }
    
    public List<Integer> getDegreeDistribution() {
        double meanValue = 0.0, sum = 0.0;
        List<Integer> internalNodesNumber = new LinkedList<Integer>();
        Collection<Cell> cellList =  graph.getVertices();
        
        for( Cell v : cellList ) {
               internalNodesNumber.add(graph.degree(v));
               sum += graph.degree(v);
        }
        
        meanValue = ( sum / internalNodesNumber.size() );
//        System.out.print("; mean value of degree of nodes: " + meanValue); 
        System.out.print("; mean of degree: " +  df.format(meanValue) +"        "); 
        
        return internalNodesNumber;
    }
    
}
