package vizardous.delegate.graphics.export;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphComponent.mxGraphControl;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import vizardous.delegate.fileFilter.PNGFileFilter;
import vizardous.delegate.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PngExporter implements LineageExporter, ChartExporter, BitmapExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, String filePath) {
		BufferedImage bi = new BufferedImage((int) chart.getBounds().getWidth(), (int) chart.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		chart.addNotify();
		chart.setVisible(true);
		chart.validate();
		chart.paint(g); // this == JComponent
		g.dispose();
		try {
			ImageIO.write(bi, "png", new File(filePath));
		} catch (Exception ex) {
			GraphicsExporter.logger.error("Chart could not be exported.", ex);
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
			
		mxImageCanvas canvas = (mxImageCanvas) mxCellRenderer.drawCells(graphComponent.getGraph(), null, graph.getView().getScale(), new mxRectangle(rect), new CanvasFactory() {							
			public mxICanvas createCanvas(int width, int height) {
				mxImageCanvas canvas = new mxImageCanvas(new mxGraphics2DCanvas(), width, height, Color.WHITE, true);

				return canvas;
			}});
		
		BufferedImage image = canvas.destroy();
		
		try{	
			ImageIO.write(image, "PNG", new File(filePath));
		} catch (Exception ex) {
			GraphicsExporter.logger.error("Lineage tree could not be exported.", ex);
		}
	}

	@Override
	public String getFileExtension() {
		return ".png";
	}

	@Override
	public FileFilter getFileFilter() {
		return PNGFileFilter.getInstance();
	}

}
