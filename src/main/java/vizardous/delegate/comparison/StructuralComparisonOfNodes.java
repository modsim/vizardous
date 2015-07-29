/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.comparison;

import edu.uci.ics.jung.algorithms.blockmodel.StructurallyEquivalent;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DelegateTree;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Constants;
import vizardous.model.Forest;
import vizardous.model.Phylogeny;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */

public class StructuralComparisonOfNodes {
    
    public StructuralComparisonOfNodes() {}
    
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
            if ( c1Dist == c2Dist ) {
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
            } else 
                return same;
        }
        return same;
    }
    
    /**
     * Calculate the sum of all short path length of the subtree
     * 
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
//         System.out.println("Cell-Id: "+ cell.getId() + " ---> DSP-Cell-Id: " + dist);   
        return dist;
    }
    
     
    
    
    
    public static void main(String[] args) throws Throwable, IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        //Test1
////        File f1 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\Vizardous\\src\\main\\resources"
////                + "\\xmlTestFiles\\testFilesForComparison\\testFall_1 (same phenotypic data and different structures)\\CP_v6_test-1.xml");
////        File f2 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\Vizardous\\src\\main\\resources"
////                + "\\xmlTestFiles\\testFilesForComparison\\testFall_1 (same phenotypic data and different structures)\\metaInfo_idee_test-1.xml");
//
////        //Test2
//        File f1 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\vizardous_clone\\src\\main\\resources\\xmlTestFiles\\CP_v6-2.0.0.xml");
//        File f2 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\vizardous_clone\\src\\main\\resources\\xmlTestFiles\\metaInfo_idee-2.2.0.xml");
//        char choice;
//
//        DataModel dr = new DataModel();
//        DataModel dm = dr.readDataModel(f1, f2);
//        CellsComparison cellsComp = new CellsComparison(dm);
//
//        do {
//                System.out.print("Fluoreszenz-Gewichtung = ");
//                double fluorWeight = Double.parseDouble(in.readLine());
//
//                System.out.print("Laenge-Gewichtung      = ");
//                double lengthWeight = Double.parseDouble(in.readLine());
//
//                System.out.print("Flaeche-Gewichtung     = ");
//                double areaWeight = Double.parseDouble(in.readLine());
//                System.out.print("");
//                
//                System.out.print("Level-Gewichtung       = ");
//                double levelWeight = Double.parseDouble(in.readLine());
//
//                System.out.print("Depth-Gewichtung       = ");
//                double depthWeight = Double.parseDouble(in.readLine());
//                System.out.print("");
//
//                System.out.print("Abweichung-Wert        = ");
//                double devi = Double.parseDouble(in.readLine());
//                System.out.println("");
//
//        //        int len = cellsComp.compareCells(fluorWeight, lengthWeight, areaWeight, devi).length;
//        //        String [] isophCellsName = new String[100]; 
//                List<String> isophCellsName = cellsComp.compareCells(fluorWeight, lengthWeight, areaWeight, devi);
//        //        System.out.println("isophCellsName.length: " + isophCellsName.size());
//
//                if(isophCellsName == null) 
//                    System.out.println("Es gibt keine ähnlichen Zellen!"); 
//                else {
//                    for(int i = 0; i < isophCellsName.size(); i++) {
//                        String[] names = isophCellsName.get(i).split(":");
//                        String Cell_1_Name = names[0];
//                        String Cell_2_Name = names[1];
//                       
//                        System.out.println( i + ")  " + Cell_1_Name + " und " + Cell_2_Name + " sind änhlich"); 
//                        System.out.println( "   ==> Leng. Vergleich: " + dm.getCellById(Cell_1_Name).getLength()
//                                + " | " + dm.getCellById(Cell_2_Name).getLength());
//                        
////                        System.out.println("TEst: " + dm.getCellById(Cell_1_Name).getChildren().size());
//                       
//                        System.out.println( "   ==> Fluo. Vergleich: " + dm.getCellById(Cell_1_Name).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) 
//                                + " | " + dm.getCellById(Cell_2_Name).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) );
//                      
//                                           
//                        System.out.println( "   ==> Area. Vergleich: " + dm.getCellById(Cell_1_Name).getArea()
//                                + " | " + dm.getCellById(Cell_2_Name).getArea());
//                         System.out.println("");
//                    }
//
//            }
//
//            double degree = cellsComp.calculateDegree(fluorWeight, lengthWeight, areaWeight, levelWeight, depthWeight);
//            System.out.println("Der Maß von T ist gleich : " + degree);
//
//
//            System.out.println("");
//            System.out.println("Moechten Sie das Programm beenden ! J/N ");
//            choice = (char) in.read();
//            in.readLine();
//        } while (choice == 'N' || choice == 'n');
//        System.out.print("Programm wurde beendet.");
//        System.out.println("");
    }
    
}
