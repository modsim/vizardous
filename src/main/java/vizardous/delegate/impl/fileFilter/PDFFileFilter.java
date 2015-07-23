/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.fileFilter;

import javax.swing.filechooser.FileFilter;

/**
 * A {@link FileFilter} for PDF files.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PDFFileFilter extends AbstractFileFilter {

	private static final PDFFileFilter instance = new PDFFileFilter();

	public static PDFFileFilter getInstance() {
		return instance;
	}
	
	protected PDFFileFilter() {
		super(new String[] { ".pdf" }, "Portable Document Format (*.pdf)");
	}
}