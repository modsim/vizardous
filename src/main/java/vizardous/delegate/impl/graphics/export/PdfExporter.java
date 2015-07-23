package vizardous.delegate.impl.graphics.export;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.filechooser.FileFilter;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphComponent.mxGraphControl;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import vizardous.delegate.impl.fileFilter.PDFFileFilter;
import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PdfExporter implements LineageExporter, ChartExporter, VectorExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, String filePath) {
		Document document = new Document(new Rectangle(0f, 0f, (float) chart.getWidth(), (float) chart.getHeight()), 0, 0, 0, 0);
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
	public void exportLineage(mxGraphComponent graphComponent, Clipping clipping, String filePath) {
		
		mxGraph graph = graphComponent.getGraph();		
		Object[] cells = new Object[] { graph.getModel().getRoot() };		
		mxGraphControl graphControl = graphComponent.getGraphControl();
		
		java.awt.Rectangle rect = null;
		
		switch (clipping) {
		case VIEWPORT:
			/* Just export the current viewport */
			int width = graphComponent.getWidth();
			int height = graphComponent.getHeight();
			int x = -graphControl.getX();
			int y = -graphControl.getY();
			
			rect = new java.awt.Rectangle(x, y, width, height);
			break;
		case NONE:
		default:
			/* Take the zoom level and export the complete tree */
			rect = graph.getPaintBounds(cells).getRectangle();
			break;
		}
		
		
		Document document = new Document(new Rectangle((float) rect.getWidth(), (float) rect.getHeight()), 0, 0, 0, 0);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
			document.open();
			PdfContentByte contentByte = writer.getDirectContent();
			
			final Graphics2D g2 = contentByte.createGraphics((float) rect.getWidth(), (float) rect.getHeight());
			
			mxGraphics2DCanvas canvas = new mxGraphics2DCanvas(g2);
			mxGraphView view = graph.getView();

			// TODO Figure out, why the original code does not work
			/* The following lines of code are from mxCellRenderer.java:L75 */
			double previousScale = canvas.getScale();
			Point previousTranslate = canvas.getTranslate();

			try
			{
				canvas.setTranslate(-rect.x, -rect.y);
				canvas.setScale(view.getScale());

				for (int i = 0; i < cells.length; i++)
				{
					graph.drawCell(canvas, cells[i]);
				}
			}
			finally
			{
				canvas.setScale(previousScale);
				canvas.setTranslate(previousTranslate.x,
						previousTranslate.y);
			}
			/* End of copy from mxCellRenderer.java */
			
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

	@Override
	public FileFilter getFileFilter() {
		return new PDFFileFilter();
	}
	
}
