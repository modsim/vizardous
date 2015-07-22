/**
 * 
 */
package vizardous.delegate.impl.graphics.export;

import javax.swing.JScrollPane;

import com.mxgraph.swing.mxGraphComponent;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public interface LineageExporter extends Exporter {
	
	/** TODO Documentation */
	public void exportLineage(mxGraphComponent graphComponent, String filePath);
}
