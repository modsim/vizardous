/**
 * 
 */
package vizardous.delegate.impl.graphics.export;

import javax.swing.JScrollPane;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public interface LineageExporter extends Exporter {
	
	/** TODO Documentation */
	public void exportLineage(JScrollPane treePanel, String filePath);
}
