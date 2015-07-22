package vizardous.delegate.impl.graphics.export;

import java.io.File;

import vizardous.delegate.impl.graphics.AbstractChart2D;
import vizardous.delegate.impl.graphics.DistributionChart2D;
import vizardous.delegate.impl.graphics.TraceChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class SvgExporter extends ChartExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, File file) {
		if (chart instanceof TraceChart2D) {
			TraceChart2D traceChart = (TraceChart2D) chart;
			traceChart.saveSVG(file);
		} else if (chart instanceof DistributionChart2D) {
			DistributionChart2D distributionChart = (DistributionChart2D) chart;
			distributionChart.saveSVG(file);
		}
	}

	@Override
	public String getFileExtension() {
		return ".svg";
	}

}
