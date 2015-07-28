package vizardous.delegate.comparison;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package vizardous.delegate.impl;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import vizardous.model.impl.Cell;
//import vizardous.model.impl.Clade;
//import vizardous.model.impl.Constants;
//import vizardous.model.impl.DM;
//import vizardous.model.impl.Phylogeny;
//
///**
// * TODO
// *
// * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
// * @version 1.0
// * 
// */
//public class CellsComparison {
//
//    private DM dataModel = null;
//    private Phylogeny phylogenyObject = null;
//    List<Cell> cellsList = null;
//    List<Clade> cladesList = null;
//    DecimalFormat f = new DecimalFormat("#0.00000");
//
//    public CellsComparison(DM dm) {
//        dataModel = dm;
//        phylogenyObject = dm.getPhyloxml().getPhylogenyObject();
//        cladesList = phylogenyObject.getAllCladeInPhylogeny();
//        cellsList = dm.getCellsListToCladesList(cladesList);
//    }
//
//    public List<String> compareCells(double fluorWeight, double lengthWeight,
//            double areaWeight, double devi) {
//        // String [cell_1-name] [cell_2-name]
//        List<String> list = new ArrayList<String>();
//        int count = 0;
//        for (int i = 0; i < cellsList.size(); i++) {
//            for (int j = 0; j < cellsList.size(); j++) {
//                if (!(cellsList.get(i).getId()).equals(cellsList.get(j).getId())) {
//                    if ((compareMetaDataOfTwoCells(cellsList.get(i), cellsList.get(j),
//                            fluorWeight, lengthWeight, areaWeight, devi)) & (compareStructureOfTwoCells(cellsList.get(i), cellsList.get(j)))) {
//                        
//                        list.add(cellsList.get(i).getId() + ":" + cellsList.get(j).getId());
//                        
//                        count++;
//                    }
//
//                }
//            }
//        }
//        
//        for (int k = 0; k < list.size(); k++) {
//            System.out.println("*** list.get(i): " + list.get(k));
//            String str1 = list.get(k);
//            String [] temp = str1.split(":");
//            String str2 = temp[1] + ":" + temp[0];
//            System.out.println("str2 = " + str2);
////            if( str1.equals(str2) ) {
////                
////            }
//        }
//        
//        return list;
//    }
//
//    private boolean compareMetaDataOfTwoCells(Cell cell_1, Cell cell_2,
//            double fluorWeight, double lengthWeight,
//            double areaWeight, double devi) {
//
//        boolean same = false;
//        double fluoresDiff, lengthDiff, areaDiff;
//
//        fluoresDiff = Math.abs(cell_1.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) 
//                - cell_2.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS));
//        lengthDiff = Math.abs(cell_1.getLength() - cell_2.getLength());
//        areaDiff = Math.abs(cell_1.getArea() - cell_2.getArea());
//
//        if ( (fluoresDiff <= devi) || (lengthDiff <= devi) || (areaDiff <= devi) ) {
//            same = true;
//        }
//        return same;
//    }
//
//    private boolean compareStructureOfTwoCells(Cell cell_1, Cell cell_2) {
//        boolean same = false;
//        int cladeCellLevelDiff, cladeCellDepthDiff, cladeCellNumbOfDescendentsDiff;
//
//        Clade clade_1 = dataModel.getCladeByCellId(cell_1.getId());
//        Clade clade_2 = dataModel.getCladeByCellId(cell_2.getId());
//
//        cladeCellLevelDiff = Math.abs(phylogenyObject.calculateLevelOfClade(clade_1)
//                - phylogenyObject.calculateLevelOfClade(clade_2));
//
//        cladeCellDepthDiff = Math.abs(phylogenyObject.calculateDepthOfClade(clade_1)
//                - phylogenyObject.calculateDepthOfClade(clade_2));
//
//        cladeCellNumbOfDescendentsDiff = Math.abs(phylogenyObject.getNumbersOfDescendentsOfClade(clade_1)
//                - phylogenyObject.getNumbersOfDescendentsOfClade(clade_2));
//
////        System.out.println("cladeCellLevelDiff: " + cladeCellLevelDiff + "  cladeCellDepthDiff:" + cladeCellDepthDiff);
//        if ((cladeCellLevelDiff == 0) && (cladeCellDepthDiff == 0) && (cladeCellNumbOfDescendentsDiff == 0)) {
//            same = true;
//        }
//        return same;
//    }
//
//    public double calculateDegree(/*ArrayList<Cell> cellsListPar, */double fluorWeight, double lengthWeight,
//            double areaWeight, double levelWeight, double depthWeight) {
//        double degree = 0, degreeNorm = 0, rtn = 0;
//        int cladeCellLevel, cladeCellDepth;
//        int treeSize = cellsList.size();
//        Clade clade;
//
//        for (Cell c : cellsList) {
//            clade = dataModel.getCladeByCellId(c.getId());
//            cladeCellLevel = phylogenyObject.calculateLevelOfClade(clade);
//            cladeCellDepth = phylogenyObject.calculateDepthOfClade(clade);
//
//            degree += (fluorWeight * c.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS)) + (lengthWeight * c.getLength()) 
//                    + (areaWeight * c.getArea()) + (levelWeight * cladeCellLevel) 
//                    + (depthWeight * cladeCellDepth);
//        }
//
//        degreeNorm = degree / treeSize;
////         rtn = Double.parseDouble(f.format(degreeNorm));
//
//        return degreeNorm;
//    }
//
//    public static void main(String[] args) throws Throwable, IOException {
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
//    }
//}
