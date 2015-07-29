/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate;

import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vizardous.delegate.treeSort.sorter.DirectedSortedSparseGraph;
import vizardous.delegate.treeSort.sorter.SortParam;
import vizardous.delegate.treeSort.sorter.SortType;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Phylogeny;

/**
 * This class create a tree graph.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */
public class CreatePhyloTreeGraph {

    /** the tree graph variable */
    private DelegateTree<Cell, Clade> g3;
    
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(CreatePhyloTreeGraph.class);
    
    /**
     * Class constructor 
     * 
     * @param phylogeny describes the tree, which will be created. 
     */
    public CreatePhyloTreeGraph(Phylogeny phylogeny) {
        g3 = new DelegateTree<Cell, Clade>();
        
        List<Clade> cladeList = phylogeny.getAllCladeInPhylogeny();
        List<Cell> cellsList = new ArrayList<Cell>();//phylogeny.getForest().getCellsListFromCladesList(cladeList);
        Clade rootClade = phylogeny.getRootClade();
        g3.setRoot(rootClade.getCellObject());
        int leng = cladeList.size();
        for(int i=1; i < leng ; i++) {
            Clade cld = cladeList.get(i);
            Cell cell = cld.getCellObject();
            cell.setPhyloTreeGraph(g3);
            cellsList.add(cell);
            Cell parentCell = cell.getParentCell();
            g3.addChild(cld, parentCell, cell);
        }
//        for(int i=1; i < cellsList.size() ; i++) {
//            cellsList.get(i).setPhyloTreeGraph(g3);
//        }
//        System.out.println("g3-Height: " + g3.getHeight());
//        g2.addTree(g3);
        
    }
    
/*    public CreatePhyloTreeGraph(Phylogeny phylogeny, SortType sortType, SortParam sortParam)
    {
    	// The only line changed: create a directed sorted sparse graph as delegate with the correct sorter and getter
        g3 = new DelegateTree<Cell, Clade>(new DelegateTree<Cell, Clade>(new DirectedSortedSparseGraph<Cell, Clade>(SortType.getSorter(sortType, sortParam))));
        
        List<Clade> cladeList = phylogeny.getAllCladeInPhylogeny();
        List<Cell> cellsList = new ArrayList<Cell>();//phylogeny.getForest().getCellsListFromCladesList(cladeList);
        Clade rootClade = phylogeny.getRootClade();
        g3.setRoot(rootClade.getCellObject());
        int leng = cladeList.size();
        for(int i=1; i < leng ; i++) {
            Clade cld = cladeList.get(i);
            Cell cell = cld.getCellObject();
            cell.setPhyloTreeGraph(g3);
            cellsList.add(cell);
            Cell parentCell = cell.getParentCell();
            g3.addChild(cld, parentCell, cell);
        }
    }*/
    
    /**
     * Give the created tree graph
     * 
     * @return DelegateTree g3 tree
     */
    public DelegateTree<Cell, Clade> getPhyloTreeGraph(){
        return this.g3;
    }
}
