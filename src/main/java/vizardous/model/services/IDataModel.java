/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.services;

import java.io.File;
import vizardous.model.impl.DataModel;



/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */

public interface IDataModel {
    
    /**
     * read the data model of both files
     * @param phyloXMLFile
     * @param metaXMLFile
     * @return DataModel-Object
     */
    public DataModel readDataModel(File phyloXMLFile, File metaXMLFile);
}
