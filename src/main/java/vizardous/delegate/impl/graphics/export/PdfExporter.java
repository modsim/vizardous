package vizardous.delegate.impl.graphics.export;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PdfExporter extends ChartExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, File file) {
		com.lowagie.text.Document document = new com.lowagie.text.Document(new Rectangle(0f, 0f, (float) chart.getWidth(), (float) chart.getHeight()), 0, 0, 0, 0);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
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
	public String getFileExtension() {
		return ".pdf";
	}
	
}
