/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a Meta information frame (MIFrame). 
 * This class stores information about one frame in the data model. 
 * Frame contain an arbitrary number of cells. 
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0.0
 */
public class MIFrame {
    
    private String              filePath        = null;
    private int                 frameId         = -1;
    private double              elapsedTime     = 0.0;
    private String              elapsedTimeUnit = "N/A";
    private MetaXML             metaInfoObject  = null;
    private ArrayList<Cell>     cellList        = null;
    private Population population = null;
    
   /**
    * Meta information frame (MIFrame) constructor.
    * 
    * @param filePath File path of the frame.
    * @param frameId Frame id.
    * @param elapsedTime Frame recording time.
    * @param elapsedTimeUnit Unit of recording time.
    * @param cellList List of cells that contain in this frame.
    * @param population The associated population of this frame.
    */
    public MIFrame(String filePath, int frameId, double elapsedTime, String elapsedTimeUnit, ArrayList<Cell> cellList, Population population) {
        this.filePath           = filePath;
        this.frameId            = frameId;
        this.elapsedTime        = elapsedTime;
        this.elapsedTimeUnit    = elapsedTimeUnit;
        this.cellList           = cellList;
        this.population         = population;
    }

    /**
     * Gets the file path of this frame.
     * 
     * @return Returns the path where the frame is stored.
     */
    public String getFilePath(){
        return filePath;
    }

    /**
     * Sets the file path of this frame.
     * 
     * @param filePath file path to set.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the unit of recording time of frame.
     * the unit of recording time.
     * @return Returns the unit of recording time of this frame.
     */
    public String getElapsedTimeUnit() {
        return elapsedTimeUnit;
    }

    /**
     * Sets the unit of recording time.
     * 
     * @param elapsedTimeUnit Unit of recording time to set.
     */
    public void setElapsedTimeUnit(String elapsedTimeUnit) {
        this.elapsedTimeUnit = elapsedTimeUnit;
    }
    
    /**
     * Gets id of frame.
     * 
     * @return Returns the id of this frame.
     */
    public int getFrameId() {
        return frameId;
    }

    /**
     * Sets the id of frame.
     * 
     * @param frameId Id to set.
     */
    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    /**
     * Gets the recording time of this frame.
     * 
     * @return Returns the recording time of this frame.
     */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Sets the recording time of this frame.
     * 
     * @param elapsedTime Recording time to set.
     */
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * Gets the associated Population of this frame.
     * 
     * @return Returns the population.
     */
    public Population getPopulation() {
            return population;
    }

    /**
     * Sets the associated Population of this frame.
     * 
     * @param population the population to set.
     */
    public void setPopulation(Population population) {
            this.population = population;
    }

    /**
     * Add cell to this frame.
     * 
     * @param cellObject Cell to add.
     */
    public void addCell(Cell cellObject){
        cellList.add(cellObject);
    }
    
    /**
     * Gets all cells of this frame.
     * 
     * @return Returns a list of cells that contain in this frame. 
     */
    public ArrayList<Cell> getAllCellsInFrame(){
        return cellList;
    }
    
    /**
     * Computes the fluorescence mean value of all cells on-the-fly.
     * 
     * @param channel the string identifier of the fluorescence channel, e.g. "yfp".
     * @return the mean value of the fluorescence values in this frame
     */
    public double getFluorescenceMean(String channel) {
    	SummaryStatistics stats = new SummaryStatistics();
    	
    	for (Cell c : cellList) {
    		Map<String, Double> fluorescences = c.getFluorescences();
    		double fluorescenceValue = fluorescences.get(channel);
    		
    		stats.addValue(fluorescenceValue);
    	}
    	
    	return stats.getMean();
    }
    
    /**
     * Computes the fluorescence standard deviation of all cells on-the-fly.
     * 
     * @param channel
     *            the string identifier of the fluorescence channel, e.g. "yfp".
     * @return the standard deviation of the fluorescence values in this frame
     */
    public double getFluorescenceStdDev(String channel) {
    	SummaryStatistics stats = new SummaryStatistics();
    	
    	for (Cell c : cellList) {
    		Map<String, Double> fluorescences = c.getFluorescences();
    		double fluorescenceValue = fluorescences.get(channel);
    		
    		stats.addValue(fluorescenceValue);
    	}
    	
    	return stats.getStandardDeviation();
    }
}
    
    

