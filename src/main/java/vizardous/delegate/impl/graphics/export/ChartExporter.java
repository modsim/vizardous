package vizardous.delegate.impl.graphics.export;

import java.io.File;

import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public abstract class ChartExporter {
	
	/** TODO Documentation */
	public abstract void exportChart(AbstractChart2D chart, File file);
	
	/** TODO Documentation */
	public void exportChart(AbstractChart2D chart, String filePath) {
		exportChart(chart, new File(filePath));
	}
	
	/** TODO Documentation */
	public abstract String getFileExtension();
}
