package vizardous.delegate.impl.graphics.export;

import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public interface ChartExporter extends Exporter {
	
	/** TODO Documentation */
	public void exportChart(AbstractChart2D chart, String filePath);

}
