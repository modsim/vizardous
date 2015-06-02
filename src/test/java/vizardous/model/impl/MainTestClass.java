///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package vizardous.model.impl;
//
//import org.w3c.dom.*;
//import java.io.*;
//import java.io.InputStreamReader;
//import java.util.*;
//import javax.xml.stream.events.Namespace;
//import org.w3c.dom.Element;
//
///**
// *
// * @author azzouzi
// */
//public class MainTestClass {
//
////    static String fileName = "C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\MavenTestProject\\src\\main\\resources\\XMLTestFiles\\CP_v6.xml";
//    //File miFile = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\MavenTestProject\\src\\main\\resources\\XMLTestFiles\\CP_v6_meta.xml");
//    public static String xmlrootname = null;
//    public static Element phylogenyElem = null;
//    public static Element rootElement = null;
//    private static Element lastElement = null;
//    public static List<Element> phylogenyElemList = null;
//    public static List<Element> cladeElemList = null;
//    public static List<Element> subCladeElemList = null;
//    private static Phylogeny phylogenyObject = null;
//    public static ArrayList<Clade> cladeList = null;
//    public static PhyloXML phyxml = null;
////    public static Namespace ns = Namespace.getNamespace("http://www.phyloxml.org");
//    Clade c = null;
//    //Attr. Declarartion of MetaXML-File
//    public static Element miXMLrootElement = null;
//    public static String miXMLrootname = null;
////    public static Namespace ns2 = Namespace.getNamespace("http://meta");
//    //-------------------MetaXML-Attribute-Deklaration-----------------------
//    private static ArrayList<MIFrame> miFrameList = new ArrayList<MIFrame>();
//    public static List<Element> frameElemList = null;
//    public static List<Element> cellElemList = null;
//    // PhyloXML-Erweiterung
////    private static Clade rootClade = new Clade();
//    private static int complete = 0;
//    private static PhyloXML phyloXmlObj = null;
//    private static MetaXML metaXMLObj = null;
//
//    private static void searchCladeByName(Clade startClade, String searchName) {
//        if (startClade != null) {
//            if (startClade.getName() != null) {
////                    System.out.println(startClade.getName() +" Brach = "+ startClade.getBranchLength());
//                if (startClade.getName().toLowerCase().equals(searchName.toLowerCase())) {
//                    System.out.println(startClade.getName() + " Branch_Len = " + startClade.getBranchLength());
//
//                    if (startClade.getSubCladeList() != null) {
//                        for (int i = 0, len = startClade.getSubCladeList().size(); i < len; i++) {
//
//                            System.out.println("    => Name= " + startClade.getSubCladeList().get(i).getName()
//                                    + "\tBranch_Len: " + startClade.getSubCladeList().get(i).getBranchLength());
//                        }
//                    }
//                }
//            }
//        }
//        if (startClade != null) {
//            if (startClade.getSubCladeList().size() > 0) {
//                ArrayList<Clade> list = startClade.getSubCladeList();
//                for (int i = 0, len = list.size(); i < len; i++) {
//                    searchCladeByName(list.get(i), searchName);
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Throwable, IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
////        int choice = 1;
////        File f2 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\MVIBG1Tool\\target\\classes\\XMLTestFiles\\CP_v6.xml");
//        File f1 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\Vizardous\\src\\main"
//                + "\\resources\\XMLTestFiles\\TestFilesForComparison\\TestFall_1 (same phenotypic data and different structures)\\CP_v6_test_1.xml");
//        File f2 = new File("C:\\Users\\Azzouzi\\Documents\\NetBeansProjects\\Vizardous\\src\\main"
//                + "\\resources\\XMLTestFiles\\TestFilesForComparison\\TestFall_1 (same phenotypic data and different structures)\\metaInfo_idee_new_comp-1.0.0.xml");
//        //----------------------------------
////        File f1 = new File("D:\\Projects\\NetBeans\\MVIBG1Tool\\src\\main\\resources\\XMLTestFiles\\CP_v6_TestVersion.xml");
////       File f2 = new File("D:\\Projects\\NetBeans\\MVIBG1Tool\\src\\main\\resources\\XMLTestFiles\\metaInfo_idee.xml");
//
//        //--------------------------- START--> Test von readPyhloXML-Methode ----------------------     
//
//        Map<File, File> mapFile = new HashMap<File, File>();
//        mapFile.put(f2, f2);
//        DataModel dm = new DataModel((Set<Map.Entry<File, File>>) mapFile);
//        List<Forest> forests = dm.getForestsList();
//
//        int auswahl = 0;
//        int aus = 0;
//        char choice;
//
//        do {
//            do {
//                System.out.println("*******************MENÜ******************************");
//                System.out.println("********** 1. PhyloXML             ******************");
//                System.out.println("********** 2. MetaXML              ******************");
//                System.out.println("********** 3. DataModel            ******************");
//                System.out.println("********** 4. Das Programm beenden ******************");
//                System.out.println("*****************************************************");
//
//                System.out.print("Auswahl: ");
//                auswahl = Integer.parseInt(in.readLine());
//
//                switch (auswahl) {
//                    case 1:
//
//                        do {
//                            System.out.println("");
//                            System.out.println("*****************************SUBMENÜ***************************");
//                            System.out.println("****** 1. PhyloXML komplett anzeigen    	         ******");
//                            System.out.println("****** 2. Suche nach einem bestimmten Clades 	         ******");
//                            System.out.println("****** 3. Suche nach der Kinder eines bestimmten Clades  ******");
//                            System.out.println("****** 4. Suche nach der Eltern eines bestimmten Clades  ******");
//                            System.out.println("****** 5. Suche nach Clades in gleichem Level  ******");
//                            System.out.println("****** 6. Das Programm beenden    	                 ******");
//                            System.out.println("***************************************************************");
//
//                            System.out.print("Auswahl: ");
//                            aus = Integer.parseInt(in.readLine());
//
//                            switch (aus) {
//                                case 1:
//                                    // 1. PhyloXML komplett anzeigen
//                                    List<Clade> clList = new ArrayList<Clade>();
//                                    clList = forests.get(0).getPhyloxml().getPhylogenyObject().getAllCladeInPhylogeny();
//
//                                    int size = clList.size();
//                                    System.out.println("Clade-List size:" + size);
//                                    System.out.println("Phylogeny");
//                                    for (int i = 0; i < size; i++) {
//                                        System.out.println("    CladeName: " + clList.get(i).getName() + "      CladeBranchLength: " + clList.get(i).getBranchLength());
//                                    }
//                                    System.out.println("/Phylogeny");
//                                    break;
//                                case 2:
//                                    // 2. Suche nach einem Clades
//                                    System.out.println("Geben Sie bitte die Name eines Clades ein");
//                                    System.out.print("Clade-Name: ");
//                                    String str = in.readLine();
//                                    System.out.println("");
////                                                        ArrayList<Clade> clList1 = new ArrayList<Clade>();
////                                                        clList1 = dm.getPhyloxml().getPhylogenyObject().getAllCladeInPhylogeny();
////                                                        
////                                                        for(int i=0; i<clList1.size(); i++){
////                                                           if(clList1.get(i).getName().equals(str)){
////                                                               System.out.println("     CladeName: " 
////                                                                       + clList1.get(i).getName() 
////                                                                       + "      CladeBranchLength: " 
////                                                                       + clList1.get(i).getBranchLength() 
////                                                                       + "      Anzahl der SubClades: " 
////                                                                       + clList1.get(i).getSubCladeList().size());
////                                                           }
////                                                        }
//                                    Clade c = forests.get(0).getPhyloxml().getPhylogenyObject().getCladeByName(str);
//                                    System.out.println("CladeName: " + c.getName()
//                                            + "      CladeBranchLength: "
//                                            + c.getBranchLength()
//                                            + "      Anzahl der SubClades: "
//                                            + c.getSubCladeList().size() //                                                                + "     CellId: " 
//                                            //                                                                + c.getCellObject().getId()
//                                            //                                                                + "     CellLength: " 
//                                            //                                                                + c.getCellObject().getLength()
//                                            );
//                                    break;
//                                case 3:
//                                    // 3. Suche nach der Kinder eines bestimmten Clades
//                                    System.out.println("Geben Sie bitte die Name eines Clades ein");
//                                    System.out.print("Clade-Name: ");
//                                    String str1 = in.readLine();
//                                    System.out.println("");
//                                    List<Clade> clList2 = new ArrayList<Clade>();
//
//                                    clList2 = forests.get(0).getPhyloxml().getPhylogenyObject().getAllCladeInPhylogeny();
//                                    for (int i = 0; i < clList2.size(); i++) {
//                                        if (clList2.get(i).getName().equals(str1)) {
//
//                                            ArrayList<Clade> subcllist = clList2.get(i).getSubCladeList();
//                                            if (subcllist.size() > 0) {
//                                                for (int j = 0; j < subcllist.size(); j++) {
//
//                                                    System.out.println("CladeName: "
//                                                            + subcllist.get(j).getName()
//                                                            + "      CladeBranchLength: "
//                                                            + subcllist.get(j).getBranchLength()
//                                                            + "      Anzahl der SubClades: "
//                                                            + subcllist.get(j).getSubCladeList().size());
////                                                                            subcllist = subcllist.get(j).getSubCladeList();
//                                                }
//                                            } else {
//                                                System.out.println("Der Clade hat kein SubClade!");
//                                            }
//
//                                        }
//                                    }
//                                    //--------------------------------------------
//
//                                    break;
//
//                                case 4:
//                                    // 4. Suche nach der Eltern eines bestimmten Clades
//                                    System.out.println("Geben Sie bitte die Name eines Clades ein");
//                                    System.out.print("Clade-Name: ");
//                                    String name3 = in.readLine();
//                                    System.out.println("");
//                                    Clade c1 = forests.get(0).getPhyloxml().getPhylogenyObject().getCladeByName(name3);
//                                    ArrayList<Clade> clList22 = new ArrayList<Clade>();
////                                                        clList22 = dm.getPhyloxml().getPhylogenyObject().getCladeParents(c1,"", clList22);
//                                    //System.out.println("Anzahl der Clades in clList22: " +clList22.size());
//                                    //System.out.println("Liste der Eltern Clade von Clade: " +name3);
//                                    for (int i = 0; i < clList22.size(); i++) {
//                                        if (clList22.get(i) != null) {
//                                            System.out.println("    CladeName: "
//                                                    + clList22.get(i).getName()
//                                                    + "      CladeBranchLength: "
//                                                    + clList22.get(i).getBranchLength());
////                                                                + "      ParentClade: " 
////                                                                + clList22.get(i).getParentClade().getName());
//                                        } else {
//                                            break;
//                                        }
//
//                                    }
//
////                                                        Clade parentClade = dr.getParentCladeByCladeName(name3);
////                                                        System.out.println("    CladeName: " 
////                                                                + parentClade.getName() 
////                                                                + "      CladeBranchLength: " 
////                                                                + parentClade.getBranchLength());
//                                    break;
//
//                                case 5:
//                                    // 4. Suche nach der Eltern eines bestimmten Clades
//                                    System.out.println("Geben Sie bitte der gewünchten Level ein");
//                                    System.out.print("Level: ");
//                                    int level = Integer.parseInt(in.readLine());
//                                    System.out.println("");
//
//                                    ArrayList<Clade> clListNew = new ArrayList<Clade>();
//                                    clListNew = forests.get(0).cladesInSameLevel(level);
////                                                        clList22 = dm.getPhyloxml().getPhylogenyObject().getCladeParents(c1,"", clList22);
//                                    //System.out.println("Anzahl der Clades in clList22: " +clList22.size());
//                                    //System.out.println("Liste der Eltern Clade von Clade: " +name3);
//                                    for (int indice = 0; indice < clListNew.size(); indice++) {
//                                        if (clListNew.get(indice) != null) {
//                                            System.out.println("    CladeName: "
//                                                    + clListNew.get(indice).getName()
//                                                    + "      CladeBranchLength: "
//                                                    + clListNew.get(indice).getBranchLength());
////                                                                + "      ParentClade: " 
////                                                                + clList22.get(i).getParentClade().getName());
//                                        } else {
//                                            break;
//                                        }
//
//                                    }
//
////                                                        Clade parentClade = dr.getParentCladeByCladeName(name3);
////                                                        System.out.println("    CladeName: " 
////                                                                + parentClade.getName() 
////                                                                + "      CladeBranchLength: " 
////                                                                + parentClade.getBranchLength());
//                                    break;
//
//                                case 6:
//                                    // 5. Das Programm beenden
//                                    System.out.println("Das Programm wurde beendet.");
//                                    System.exit(0);
//                                    break;
//                                default:
//                                    System.out.println("Das Programm wurde beendet.");
//                                    System.exit(0);
//                                    break;
//
//                            }
//                        } while (aus < 1 || aus > 5);
//                        break;
//
//                    case 2:
//                        System.out.println("");
//                        System.out.println("************************SUBMENÜ********************");
//                        System.out.println("****** 1. MetaXML komplett anzeigen          ******");
//                        System.out.println("****** 2. Alle existierende Frames anzeigen  ******");
//                        System.out.println("****** 3. Suche nach einer bestimmten Frame  ******");
//                        System.out.println("****** 4. Suche nach Zellen einer Frame      ******");
//                        System.out.println("****** 5. Suche nach einer bestimmten Zelle  ******");
//                        System.out.println("****** 6. Das Programm beenden    	     ******");
//                        System.out.println("***************************************************");
//
//                        System.out.print("Auswahl: ");
//                        int aus2 = Integer.parseInt(in.readLine());
//                        System.out.println("");
//
//                        switch (aus2) {
//                            case 1:
//                                // 1. MetaXML komplett anzeigen 
//                                ArrayList<MIFrame> flist = forests.get(0).getMetaxml().getAllFrames();
//                                int size = flist.size();
//                                for (int i = 0; i < size; i++) {
//                                    System.out.println("Frame-Id: " + flist.get(i).getFrameId() + "     Frame-FilePath: " + flist.get(i).getFilePath() + "  elapsedTime=" + flist.get(i).getElapsedTime() + " " + flist.get(i).getElapsedTimeUnit());
//
//                                    ArrayList<Cell> clist = flist.get(i).getAllCellsInFrame();
//                                    for (int j = 0; j < clist.size(); j++) {
//                                        System.out.println("    Cell-Id: " + clist.get(j).getId() + "      Cell-Area: " + clist.get(j).getArea()
//                                                + "      Cell-Length: " + clist.get(j).getLength() + "      Cell-Flurescence: " + clist.get(j).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS));
//
//                                    }
//                                    System.out.println("");
//                                }
//                                break;
//
//                            case 2:
//                                // 2. Alle existierende Frames anzeigen 
//                                flist = forests.get(0).getMetaxml().getAllFrames();
//                                for (int i = 0; i < flist.size(); i++) {
//                                    System.out.println("Frame-Id: " + flist.get(i).getFrameId() + "  file=" + flist.get(i).getFilePath() + "  elapsedTime=" + flist.get(i).getElapsedTime() + " " + flist.get(i).getElapsedTimeUnit());// + "     Frame-FilePath: " + flist.get(i).getFilePath());
//                                    //System.out.println("");
//                                }
//                                break;
//
//                            case 3:
//                                // 3. Suche nach einer bestimmten Frame
//                                System.out.println("Geben Sie bitte die Frame-Id ein");
//                                System.out.print("Frame Id: ");
//                                int frameId = Integer.parseInt(in.readLine());
//                                System.out.println("");
//                                MIFrame mif = forests.get(0).getMetaxml().getFrameByID(frameId);
//
//                                if (mif == null) {
//                                    System.err.println("Frame with Id = " + frameId + " doesn't exist ");
//                                }
//
//                                System.out.println("Frame   id=" + mif.getFrameId() + "  file=" + mif.getFilePath() + "  elapsedTime=" + mif.getElapsedTime() + " " + mif.getElapsedTimeUnit());
//                                ArrayList<Cell> clist1 = mif.getAllCellsInFrame();
//                                for (int j = 0; j < clist1.size(); j++) {
//                                    System.out.println("    Cell-Id: " + clist1.get(j).getId() + "      Cell-Area: " + clist1.get(j).getArea()
//                                            + "      Cell-Length: " + clist1.get(j).getLength() + "      Cell-Flurescence: " + clist1.get(j).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS));
//                                }
//
//                                break;
//                            case 4:
//                                // 4. Suche nach Zellen einer Frame
//                                System.out.println("Geben Sie bitte die Frame-Id ein");
//                                System.out.print("Frame Id: ");
//                                int frameId1 = Integer.parseInt(in.readLine());
//                                System.out.println("");
//                                MIFrame mif1 = forests.get(0).getMetaxml().getFrameByID(frameId1);
//
//                                ArrayList<Cell> clist2 = mif1.getAllCellsInFrame();
//                                for (int j = 0; j < clist2.size(); j++) {
//                                    System.out.println(
//                                            "      Cell-Id: " + clist2.get(j).getId()
//                                            + "      Cell-Area: " + clist2.get(j).getArea()
//                                            + "      Cell-Length: " + clist2.get(j).getLength()
//                                            + "      Cell-Flurescence: " + clist2.get(j).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS)
//                                            + "      Clade-Name: " + clist2.get(j).getCladeObject().getName()
//                                            + "      Clade-BranchLength: " + clist2.get(j).getCladeObject().getBranchLength()
//                                            + "      Clade-ParentClade: " + clist2.get(j).getCladeObject().getParentClade().getName()
//                                            + "      Clade-FirstSubClade-Name: " + clist2.get(j).getCladeObject().getSubCladeList().get(0).getName());
//                                }
//                                break;
//                            case 5:
//                                // 5. Suche nach einer bestimmten Zelle
//                                System.out.println("Geben Sie bitte die Cell-Id ein");
//                                System.out.print("Cell Id: ");
//                                String cellId2 = in.readLine();
//                                System.out.println("");
//
//                                ArrayList<MIFrame> flist1 = forests.get(0).getMetaxml().getAllFrames();
//                                int size1 = flist1.size();
//                                for (int i = 0; i < size1; i++) {
//                                    //System.out.println("Frame-Id: " + flist1.get(i).getFrameId() + "     Frame-FilePath: " + flist1.get(i).getFilePath());
//                                    ArrayList<Cell> clist3 = flist1.get(i).getAllCellsInFrame();
//                                    for (int j = 0; j < clist3.size(); j++) {
//                                        if (cellId2.equals(clist3.get(j).getId())) {
//                                            System.out.println("    Cell-Id: " + clist3.get(j).getId() + "      Cell-Area: " + clist3.get(j).getArea()
//                                                    + "      Cell-Length: " + clist3.get(j).getLength() + "      Cell-Flurescence: " + clist3.get(j).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS));
//                                        }
//                                    }
//                                }
//                                break;
//                            case 6:
//                                System.out.println("Das Programm wurde beendet.");
//                                System.exit(0);
//                                break;
//                            default:
//                                System.out.println("Default");
//                                break;
//                        }
//                        break;
//                    case 3:
//                        System.out.println("");
//                        System.out.println("************************SUBMENÜ*************************************************");
//                        System.out.println("****** 1. Eigenschaften einer Zelle bei der Auswahl eines Clades anzeigen ******");
//                        System.out.println("****** 2. Suche nach Kindern einer bestimmten Zelle	                  ******");
//                        System.out.println("****** 3. Suche nach Eltern einer bestimmten Zelle                        ******");
////                                      System.out.println("****** 4. Suche nach Zellen einer Frame                                   ******");
////                                      System.out.println("****** 5. Suche nach einer bestimmte Zelle                                ******");
//                        System.out.println("********************************************************************************");
//
//                        System.out.print("Auswahl: ");
//                        int aus3 = Integer.parseInt(in.readLine());
//
//                        switch (aus3) {
//                            case 1:
//                                System.out.print("Clade-Name: ");
//                                String name = in.readLine();
//                                System.out.println("");
//                                Cell c = forests.get(0).getCellByCladeName(name);
//                                System.out.println("    Cell-Id: "
//                                        + c.getId() + "      Cell-Area: "
//                                        + c.getArea() + "      Cell-Length: "
//                                        + c.getLength() + "      Cell-Flurescence: "
//                                        + c.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS));
//                                break;
//
//                            case 2:
//                                System.out.print("Clade-Name: ");
//                                String name1 = in.readLine();
//                                System.out.println("");
////                                                dm.getPhyloxml().getPhylogenyObject().
//                                ArrayList<Cell> cellList = forests.get(0).getChildrenOfCellByCladeName(name1);
//                                for (int indx = 0; indx < cellList.size(); indx++) {
//                                    System.out.println("    Cell-Id: "
//                                            + cellList.get(indx).getId() + "      Cell-Area: "
//                                            + cellList.get(indx).getArea() + " " + cellList.get(indx).getAreaUnit()
//                                            + "      Cell-Length: "
//                                            + cellList.get(indx).getLength() + " " + cellList.get(indx).getLengthUnit() + "      Cell-Flurescence: "
//                                            + cellList.get(indx).getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) + " " + cellList.get(indx).getFluorescenceUnit());
//                                }
//                                break;
//
//                            case 3:
////                                                System.out.print("Cell-Id: ");
////						int id = Integer.parseInt(in.readLine());
////                                                System.out.println("");
////                                                Clade c1 = dr.getCladeByCellId(id);
////                                                 System.out.println("    CladeName: " 
////                                                                + c1.getName()
////                                                                + "      CladeBranchLength: " 
////                                                                + c1.getBranchLength());
////                                                dm.getPhyloxml().getPhylogenyObject().
////                                                dr.cellLengthAverage(id);
////                                                dr.cellAreaAverage(id);
////                                                dr.cellFluorescenceAverage(id);
////                                                ArrayList<Cell> cellList1 = dr.getCellParentsByCellId(id);
////                                                
////                                                for(int indx1=0; indx1<cellList1.size(); indx1++){
////                                                    if(cellList1.get(indx1) != null){
////                                                        System.out.println("    Cell-Id: " 
////                                                        + cellList1.get(indx1).getId() + "      Cell-Area: "
////                                                        + cellList1.get(indx1).getArea()+" " + cellList1.get(indx1).getAreaUnit()    
////                                                        + "      Cell-Length: " 
////                                                          
////                                                        + cellList1.get(indx1).getLength() + " " + cellList1.get(indx1).getLengthUnit()  + "      Cell-Flurescence: " 
////                                                        + cellList1.get(indx1).getFluorescence() +" " + cellList1.get(indx1).getFluorescenceUnit());  
////                                                    }else{
//////                                                      System.out.println("Clade is null");
////                                                    }                                                 
////                                                }
////						break;
//
//                                //---------------------------Test--------------
//                                // 4. Suche nach der Eltern eines bestimmten Clades
//                                System.out.println("Geben Sie bitte der gewünchten Level ein");
//                                System.out.print("Level: ");
//                                int level = Integer.parseInt(in.readLine());
//                                System.out.println("");
//
//                                ArrayList<Clade> clListNew = new ArrayList<Clade>();
//                                clListNew = forests.get(0).cladesInSameLevel(level);
////                                                        clList22 = dm.getPhyloxml().getPhylogenyObject().getCladeParents(c1,"", clList22);
//                                //System.out.println("Anzahl der Clades in clList22: " +clList22.size());
//                                //System.out.println("Liste der Eltern Clade von Clade: " +name3);
//                                for (int indice = 0; indice < clListNew.size(); indice++) {
//                                    if (clListNew.get(indice) != null) {
//                                        System.out.println("    CladeName: "
//                                                + clListNew.get(indice).getName()
//                                                + "      CladeBranchLength: "
//                                                + clListNew.get(indice).getBranchLength()
//                                                + "      ParentClade: "
//                                                + clListNew.get(indice).getParentClade().getName());
//                                    } else {
//                                        break;
//                                    }
//
//                                }
//
////                                                        Clade parentClade = dr.getParentCladeByCladeName(name3);
////                                                        System.out.println("    CladeName: " 
////                                                                + parentClade.getName() 
////                                                                + "      CladeBranchLength: " 
////                                                                + parentClade.getBranchLength());
//                                break;
//
//                            //_---------------------------
//
//                            default:
//                                System.out.println("Default");
//                                break;
//                        }
//                        break;
////					
//                    case 4:
//                        System.out.println("Das Programm wurde beendet.");
//                        System.exit(0);
//                        break;
//                }
//
//            } while (auswahl < 1 || auswahl > 3);
//            System.out.println("");
//            System.out.println("Moechten Sie dies Programm beenden ! J/N ");
//            choice = (char) in.read();
//            in.readLine();
//
//
//        } while (choice == 'N' || choice == 'n');
//        System.out.print("Programm wurde beendet.");
//        System.out.println("");
//
//    }
//}
