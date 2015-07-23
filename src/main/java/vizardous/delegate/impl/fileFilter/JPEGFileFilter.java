/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.fileFilter;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */

public class JPEGFileFilter extends AbstractFileFilter {
        public JPEGFileFilter() {
                super(new String[] { ".jpeg" }, "Joint Photographic Experts Group Format (*.jpeg)");
        }
    }
