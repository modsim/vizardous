/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model;


import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
public class MetaXML {
    
    private     String  projectName             = null;
    private     double  experimentDuration      = 0;
    private     String  experimentDurationUnit  = "N/A";
    private     ArrayList<MIFrame>  miFrames            = null;

    /**
     * 
     */
    public MetaXML(){   miFrames = new ArrayList<MIFrame>();}
    
    /**
     * 
     * @param projectName
     * @param experimentDuration
     * @param miframe 
     */
    public MetaXML(String projectName, double experimentDuration, String  experimentDurationUnit, ArrayList<MIFrame> miframe) {
        this.projectName            = projectName;
        this.experimentDuration     = experimentDuration;
        this.experimentDurationUnit = experimentDurationUnit;
        this.miFrames               = miframe;
    }

    /**
     * 
     * @return 
     */
    public String getProjectName(){
        return projectName; 
    }
    
    /**
     * 
     * @return 
     */
    public double getExperimentDuration(){
        return experimentDuration;
    }
    
    /**
     * 
     * @param frameObject 
     */
    public void addMIFrame(MIFrame frameObject){
        miFrames.add(frameObject);
    }
    
    /**
     * 
     * @return 
     */
    public ArrayList<MIFrame> getAllFrames(){
        return miFrames;
    }
    
    /**
     * this method returns an MIFrame object when entering an ID
     * 
     * @param   frameId frame-Id
     * @return  MIFrame-Object
     */
    public MIFrame getFrameByID(int frameId){
        MIFrame mif = null;
        ArrayList<MIFrame> miframeList = getAllFrames();
       
        for(int i=0; i<miframeList.size(); i++){
            if((miframeList.get(i).getFrameId()) == frameId){            
                mif = new MIFrame(miframeList.get(i).getFilePath(), 
                        miframeList.get(i).getFrameId(), 
                        miframeList.get(i).getElapsedTime(), 
                        miframeList.get(i).getElapsedTimeUnit(),
                        miframeList.get(i).getAllCellsInFrame(),
                        miframeList.get(i).getPopulation());
            }
        }
        if(mif == null)            
           throw new NullPointerException("Frame with Id = "+ frameId+ " doesn't exist ");      
        
        return mif;
    }
    
    /**
     * find a frame with a given file pathname
     * 
     * @param filePath
     * @return 
     */
    public MIFrame getFrameByFilePath(String filePath){
        MIFrame mif = null;
        ArrayList<MIFrame> miframeList = getAllFrames();
       
        for(int i=0; i<miframeList.size(); i++){
            if((miframeList.get(i).getFilePath()).equals(filePath)){            
                mif = new MIFrame(miframeList.get(i).getFilePath(), 
                        miframeList.get(i).getFrameId(), 
                        miframeList.get(i).getElapsedTime(), 
                        miframeList.get(i).getElapsedTimeUnit(),
                        miframeList.get(i).getAllCellsInFrame(),
                        miframeList.get(i).getPopulation());
            }
        }
        if(mif == null)            
           throw new NullPointerException("Frame with this file path = "+ filePath+ " doesn't exist ");      
        
        return mif;
    }
     
     /**
      * give a set of cell-Objects that exist in a give Frame
      * 
      * @param frameObj
      * @return List of cell objects
      */
      public ArrayList<Cell> getCellsInFrame(MIFrame frameObj) {
        ArrayList<Cell> cells = new ArrayList<Cell>();
        cells = frameObj.getAllCellsInFrame();
        return cells;
      } 
      
      public MIFrame getMIFrameByCellId(String cellId) {
          MIFrame miFrame = null;
          ArrayList<MIFrame> mifList = getAllFrames();
          
          for(int i=0; i<mifList.size(); i++) {
              ArrayList<Cell> celLList = mifList.get(i).getAllCellsInFrame();
              for(int j=0; j<celLList.size(); j++) {
                    if((celLList.get(j).getId()).equals(cellId)) {        
                        miFrame = mifList.get(i);
                        return miFrame;
                    }
              }
          }
          return miFrame;
      }
      
      public int cellsNumberInFrame(int frameId) {
          int count = 0;
          ArrayList<MIFrame> mifList = getAllFrames();
          
          for(int i=0; i<mifList.size(); i++) {
              if((mifList.get(i).getFrameId()) == frameId) {
                  ArrayList<Cell> celLList = mifList.get(i).getAllCellsInFrame();
                  count = celLList.size();
              }              
          }          
          return count;
      }
     /**
      * Gets all Cells contained in the metaXML file
      * 
      * @return list of cells
      */ 
     public List<Cell> getAllCellsInMetaXML() {
         List<Cell> listCells = new ArrayList<Cell>();
          for ( MIFrame miframre : getAllFrames()) 
              for (Cell cell : miframre.getAllCellsInFrame() )
                  listCells.add(cell);
         return listCells;    
     } 
     
     /**
      * Checks whether the given cell is a dummy cell or not    
      * 
      * @param cell object of the Cell class
      * @return True if cell is dummy. Otherwise, false
      */
     public boolean isDummyCell(Cell cell) {
        boolean isDummy = false;
        if( (cell.getId() == null) && (cell.getLength() == 0.0) && (cell.getLengthUnit() == null) 
                && (cell.getArea() == 0.0) && (cell.getAreaUnit() == null) 
                && (cell.getFluorescences() == null) && (cell.getFluorescenceUnit() == null) 
                && (cell.getMIFrameObject() == null) && (cell.getCladeObject() == null)) {
            
            isDummy = true;
        }
        
        return isDummy;
    }
     
     public String getExperimentDurationUnit() {
        return experimentDurationUnit;
    }

    public void setExperimentDurationUnit(String experimentDurationUnit) {
        this.experimentDurationUnit = experimentDurationUnit;
    }

}
