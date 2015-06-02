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

public class SVGFileFilter extends AbstractFileFilter {
        public SVGFileFilter() {
                super(new String[] { ".svg" }, "Scalable Vector Graphics (*.svg)");
        }
    }
