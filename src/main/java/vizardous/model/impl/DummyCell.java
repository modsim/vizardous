/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.impl;

import java.util.Map;

/**
 * This class represents a dummy cell/node of a phylogenetic tree in the Vizsardous data model.   
 *
 * @author Charaf E. Azzouzi <c.azzuzi@fz-juelich.de>
 * @version 1.0.0
 */

public class DummyCell extends Cell {

    /**
     * Constructs a new <code>Cell</code> object.
     * 
     * @param   cellId  cell/node id
     * @param   length length of cell
     * @param   lengthUnit unit of length of cell [µm]
     * @param   area area of cell
     * @param   areaUnit unit of area of cell [mm]
     * @param   fluorescence fluorescence value in cell
     * @param   fluorescenceUnit unit of fluorescence value of cell [arbitrary]
     * @param   frameObject object of MIFrame class
     * @param   cladeObject object of Clade class
     */
    public DummyCell(String cellId, double length, String lengthUnit, double area, String areaUnit, Map<String, Double> fluorescences, String fluorescenceUnit, MIFrame frameObject, Clade cladeObject) {
        super(cellId, length, lengthUnit, area, areaUnit, fluorescences, fluorescenceUnit, frameObject, cladeObject);
    }
    
    
    
}
