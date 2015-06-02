/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.comparison;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DelegateTree;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Clade;
import vizardous.model.impl.Forest;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */

public class HybridComparisonOfCells {
    private NumberFormat n = NumberFormat.getInstance();
    private Forest forest = null;
    int count = 0;
    
    public HybridComparisonOfCells(Forest forest) {
        this.forest = forest;
    }
    
    public boolean comapareTwoNodes(Cell c1, Cell c2) {
        DelegateTree<Cell,Clade> treeGraph_1 = c1.getPhyloTreeGraph();
        DelegateTree<Cell,Clade> treeGraph_2 = c2.getPhyloTreeGraph();
        boolean same = false;

        if ( (c1.isLeaf() && !c2.isLeaf()) || (!c1.isLeaf() && c2.isLeaf()) ) 
            return same;

        else if ( (c1.isLeaf() && c2.isLeaf()) ) 
            return true;

        else {
            
            int c1Dist = calculateDijkstraShortPathLength(treeGraph_1, c1);
            int c2Dist = calculateDijkstraShortPathLength(treeGraph_2, c2);
            
            double c1SLength = calculateSLengthOfCell(c1);
            double c2SLength = calculateSLengthOfCell(c2);
            
            double c1SArea = calculateSAreaOfCell(c1);
            double c2SArea = calculateSAreaOfCell(c2);
            
            double c1SFluorescence = calculateSFluorescenceOfCell(c1, "yfp");
            double c2SFluorescence = calculateSFluorescenceOfCell(c2, "yfp");
            
            if ( (c1Dist == c2Dist) & (n.format(c1SLength).equals(n.format(c2SLength))) 
                    & (n.format(c1SArea).equals(n.format(c2SArea))) 
                    ) {
                count++;
                System.out.println(" 1-Cell-Id: " + c1.getId() + " 2-Cell-Id: " + c2.getId());
                 List<Cell> c1Childlist = c1.getChildren();
                 List<Cell> c2Childlist = c2.getChildren();
                 if ( c1Childlist.size() == c2Childlist.size() ) {
                      for (int i = 0; i<c1Childlist.size(); i++) {
                          Cell c1Child1 = c1Childlist.get(i);
                          Cell c2Child2 = c2Childlist.get(i);
                          if ( comapareTwoNodes(c1Child1, c2Child2) )
                              same = true;
                      }
                 }
                 System.out.println("counter: " + count);
            } else 
                return same;
        }
        return same;
    }

 /**
     * Calculate the sum of all short path length of the subtree
     * From JUNG2 Java Doc 2.0
     * Returns a LinkedHashMap which maps each vertex in the graph (including the source vertex) to its distance from the source vertex. 
     * The map's iterator will return the elements in order of increasing distance from source.
     * 
     * The size of the map returned will be the number of vertices reachable from source.
     * Calculates distances in a specified graph, using Dijkstra's single-source-shortest-path algorithm. 
     * All edge weights in the graph must be nonnegative; if any edge with negative weight is found in the course of calculating distances, 
     * an IllegalArgumentException will be thrown. (Note: this exception will only be thrown when such an edge would be used to update a 
     * given tentative distance; the algorithm does not check for negative-weight edges "up front".)
     * Distances and partial results are optionally cached (by this instance) for later reference. Thus, if the 10 closest vertices to a 
     * specified source vertex are known, calculating the 20 closest vertices does not require starting Dijkstra's algorithm over from scratch.
     * Distances are stored as double-precision values. If a vertex is not reachable from the specified source vertex, no distance is stored. 
     * This is new behavior with version 1.4; the previous behavior was to store a value of Double.POSITIVE_INFINITY. This change gives the 
     * algorithm an approximate complexity of O(kD log k), where k is either the number of requested targets or the number of reachable 
     * vertices (whichever is smaller), and D is the average degree of a vertex.
     * The elements in the maps returned by getDistanceMap are ordered (that is, returned by the iterator) by nondecreasing distance from source.
     * Users are cautioned that distances calculated should be assumed to be invalidated by changes to the graph, and should invoke reset() when 
     * appropriate so that the distances can be recalculated.
     * @param g
     * @param cell
     * @return 
     */     
     public int calculateDijkstraShortPathLength(DelegateTree treeGraph, Cell cell) {
        int dist =0;
        DijkstraShortestPath dijkDist = new DijkstraShortestPath(treeGraph);
        Map<Cell, Number> mapCellToDist = dijkDist.getDistanceMap(cell);
        for (Map.Entry<Cell, Number> me : mapCellToDist.entrySet()) { 
//            System.out.println("DSP-Cell-Id: "+ me.getKey().getId() + "    DSP-value: "+ me.getValue().intValue()); 
            dist += me.getValue().intValue(); 
        }
        return dist;
    }
     
     private double calculateSFluorescenceOfCell(Cell c, String typ) {
         double sumFluorescence = -1;
         if( typ == null) {
//             System.out.println("Unknow typ of fluorescence!");
         } else {
             sumFluorescence = c.getFluorescences().get(typ).doubleValue();
         }
         
         List<Cell> list = forest.getCellChildrenByCellId(c.getId());
         for(Cell cell : list) 
            sumFluorescence += cell.getFluorescences().get(typ).doubleValue();
         
         return sumFluorescence;
     }
     
     private double calculateSLengthOfCell(Cell c) {
         double sumLength = c.getLength();
         
         List<Cell> list = forest.getCellChildrenByCellId(c.getId());
         for(Cell cell : list) 
            sumLength += cell.getLength();
         
         return sumLength;
     }
     
     private double calculateSAreaOfCell(Cell c) {
         double sumArea = c.getArea();
         
         List<Cell> list = forest.getCellChildrenByCellId(c.getId());
         for(Cell cell : list) 
            sumArea += cell.getArea();
         
         return sumArea;
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
}
