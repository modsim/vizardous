/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.fileFilter;

import javax.swing.filechooser.FileFilter;

/**
 * A {@link FileFilter} for PNG files.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PNGFileFilter extends AbstractFileFilter {
	
	private static final PNGFileFilter instance = new PNGFileFilter();

	public static PNGFileFilter getInstance() {
		return instance;
	}
	
	protected PNGFileFilter() {
		super(new String[] { ".png" }, "Portable Network Graphics (*.png)");
	}
}