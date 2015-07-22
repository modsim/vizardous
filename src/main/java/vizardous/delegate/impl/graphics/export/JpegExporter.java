package vizardous.delegate.impl.graphics.export;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class JpegExporter extends ChartExporter {
	
	@Override
	public void exportChart(AbstractChart2D chart, File file) {
		BufferedImage expImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
		/*
		 * Print to Image, scaling if necessary.
		 */
		Graphics2D g2 = expImage.createGraphics();
		chart.addNotify();
		chart.setVisible(true);
		chart.validate();
		chart.paint(g2);
		/*
		 * Write to File
		 */
		try {
			OutputStream out = new FileOutputStream(file);
			ImageIO.write(expImage, "jpeg", out);
			out.close();
		} catch (Exception ex) {
			GraphicsExporter.logger.error("Chart could not be exported.", ex);
		}
	}

	@Override
	public String getFileExtension() {
		return ".jpeg";
	}

}
