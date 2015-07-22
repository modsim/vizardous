package vizardous.delegate.impl.graphics.export;

import java.io.File;
import java.io.IOException;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;

import vizardous.delegate.impl.graphics.AbstractChart2D;
import vizardous.delegate.impl.graphics.DistributionChart2D;
import vizardous.delegate.impl.graphics.TraceChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class SvgExporter implements LineageExporter, ChartExporter, VectorExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, String filePath) {
		if (chart instanceof TraceChart2D) {
			TraceChart2D traceChart = (TraceChart2D) chart;
			traceChart.saveSVG(new File(filePath));
		} else if (chart instanceof DistributionChart2D) {
			DistributionChart2D distributionChart = (DistributionChart2D) chart;
			distributionChart.saveSVG(new File(filePath));
		}
	}
	
	@Override
	public void exportLineage(mxGraphComponent graphComponent, Clipping clipping, String filePath) {
	
		mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(graphComponent.getGraph(), null, 1, null, new CanvasFactory() {							
			public mxICanvas createCanvas(int width, int height) {
				mxSvgCanvas canvas = new mxSvgCanvas(mxDomUtils.createSvgDocument(width, height));
				canvas.setEmbedded(true);

				return canvas;
			}});

		try {
			mxUtils.writeFile(mxXmlUtils.getXml(canvas.getDocument()), filePath);
		} catch (IOException e) {
			GraphicsExporter.logger.error("Lineage tree could not be exported.", e);
		}
	}
	
	@Override
	public String getFileExtension() {
		return ".svg";
	}

}
