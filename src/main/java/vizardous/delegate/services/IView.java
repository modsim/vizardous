/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.services;

import java.io.File;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
public interface IView {
    
    public File getPhyloXMLFile(); 
    public File getMetaXMLFile();
    
}
