package vizardous.delegate.impl.graphics.export;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mxgraph.swing.mxGraphComponent;

import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PdfExporter implements LineageExporter, ChartExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, String filePath) {
		com.lowagie.text.Document document = new com.lowagie.text.Document(new Rectangle(0f, 0f, (float) chart.getWidth(), (float) chart.getHeight()), 0, 0, 0, 0);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();
			PdfContentByte contentByte = writer.getDirectContent();
			Graphics2D g2 = contentByte.createGraphics(chart.getWidth(), chart.getHeight());

			chart.getChart().draw(g2, new java.awt.Rectangle(chart.getWidth(), chart.getHeight()));

			g2.dispose();
		} catch (Exception ex) {
			GraphicsExporter.logger.error("Chart could not be exported.", ex);
		} finally {
			document.close();
		}
	}

	@Override
	public void exportLineage(mxGraphComponent graphComponent, String filePath) {
		com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A3.rotate(), 0, 0, 0, 0);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
			document.open();
			PdfContentByte contentByte = writer.getDirectContent();
			
			Graphics2D g2 = contentByte.createGraphics(graphComponent.getWidth(), graphComponent.getHeight());
			graphComponent.paint(g2);
			
			g2.dispose();
		} catch (Exception ex) {
			GraphicsExporter.logger.error("Lineage tree could not be exported.", ex);
		} finally {
			if (document.isOpen()) {
				document.close();
			}
		}
	}
	
	@Override
	public String getFileExtension() {
		return ".pdf";
	}
	
}
