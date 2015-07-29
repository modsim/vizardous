/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.fileFilter;

import javax.swing.filechooser.FileFilter;

/**
 * A {@link FileFilter} for JPEG files.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class JPEGFileFilter extends AbstractFileFilter {

	private static final JPEGFileFilter instance = new JPEGFileFilter();

	public static JPEGFileFilter getInstance() {
		return instance;
	}

	protected JPEGFileFilter() {
		super(new String[] { ".jpeg" }, "Joint Photographic Experts Group Format (*.jpeg)");
	}
}
