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

public class PDFFileFilter extends AbstractFileFilter {
        public PDFFileFilter() {
                super(new String[] { ".pdf" }, "Portable Document Format (*.pdf)");
        }
    }