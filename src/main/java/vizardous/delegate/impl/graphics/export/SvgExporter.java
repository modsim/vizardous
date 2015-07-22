package vizardous.delegate.impl.graphics.export;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JScrollPane;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

import vizardous.delegate.impl.graphics.AbstractChart2D;
import vizardous.delegate.impl.graphics.DistributionChart2D;
import vizardous.delegate.impl.graphics.TraceChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class SvgExporter implements LineageExporter, ChartExporter {
	
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
	public void exportLineage(JScrollPane treePanel, String filePath) {
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		// TODO Think what's the best to take: the size of viewing
		// panel or the size og drawing tree
		svgGenerator.setSVGCanvasSize(new Dimension(treePanel.getWidth(), treePanel.getHeight()));
		boolean useCSS = true;
		try {
			FileOutputStream os = new FileOutputStream(new File(filePath));
			Writer out = new OutputStreamWriter(os, "UTF-8");
			treePanel.paint(svgGenerator);
			svgGenerator.stream(out, useCSS);
			os.flush();
			os.close();
		} catch (Exception ex) {
			GraphicsExporter.logger.error("Lineage tree could not be exported.", ex);
		}
	}
	
	@Override
	public String getFileExtension() {
		return ".svg";
	}

}
