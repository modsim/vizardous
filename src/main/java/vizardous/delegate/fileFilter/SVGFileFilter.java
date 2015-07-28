/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.fileFilter;

import javax.swing.filechooser.FileFilter;

/**
 * A {@link FileFilter} for SVG files.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class SVGFileFilter extends AbstractFileFilter {
	
	private static final SVGFileFilter instance = new SVGFileFilter();

	public static SVGFileFilter getInstance() {
		return instance;
	}
	
	protected SVGFileFilter() {
		super(new String[] { ".svg" }, "Scalable Vector Graphics (*.svg)");
	}
}
