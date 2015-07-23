package vizardous.delegate.impl.graphics.export;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import com.mxgraph.swing.mxGraphComponent;

import vizardous.delegate.impl.fileFilter.PNGFileFilter;
import vizardous.delegate.impl.graphics.AbstractChart2D;

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
		BufferedImage bi = new BufferedImage((int) graphComponent.getBounds().getWidth(), (int) graphComponent.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		graphComponent.paint(g);
		g.dispose();
		try {
			ImageIO.write(bi, "png", new File(filePath));
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
		return new PNGFileFilter();
	}

}
