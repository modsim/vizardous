package vizardous.delegate.graphics.export;

import javax.swing.filechooser.FileFilter;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public interface Exporter {
	
	/** TODO Documentation */
	public String getFileExtension();
	
	/** TODO Documentation */
	public FileFilter getFileFilter();
}
