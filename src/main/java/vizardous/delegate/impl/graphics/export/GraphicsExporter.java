/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics.export;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;

import vizardous.delegate.impl.fileFilter.JPEGFileFilter;
import vizardous.delegate.impl.fileFilter.PDFFileFilter;
import vizardous.delegate.impl.fileFilter.PNGFileFilter;
import vizardous.delegate.impl.fileFilter.SVGFileFilter;
import vizardous.delegate.impl.graphics.AbstractChart2D;
import vizardous.delegate.impl.graphics.DistributionChart2D;
import vizardous.delegate.impl.graphics.TraceChart2D;

/**
 * Export functionality for charts.
 * 
 * Methods are called via menu entries for exporting charts.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class GraphicsExporter {
	
	/** The {@link Logger} for this class. */
    final static Logger logger = LoggerFactory.getLogger(GraphicsExporter.class);
	
    /** */
    final static Map<String, ChartExporter> exporters = new HashMap<String, ChartExporter>();
    
    static {
    	exporters.put("Portable Network Graphics (*.png)", new PngExporter());
    	exporters.put("Scalable Vector Graphics (*.svg)", new SvgExporter());
    	exporters.put("Joint Photographic Experts Group Format (*.jpeg)", new JpegExporter());
    	exporters.put("Portable Document Format (*.pdf)", new PdfExporter());
    }
    
	/**
	 * TODO Documentation
	 * 
	 * @param chart
	 * @param chartArt
	 */
	public static void exportChart2D(AbstractChart2D chart, String chartArt) {
		JFileChooser myChooser = new JFileChooser();
		myChooser.setCurrentDirectory(myChooser.getFileSystemView().getHomeDirectory());
		myChooser.setAcceptAllFileFilterUsed(false);
		myChooser.addChoosableFileFilter(new PNGFileFilter());
		myChooser.addChoosableFileFilter(new SVGFileFilter());
		myChooser.addChoosableFileFilter(new JPEGFileFilter());
		myChooser.addChoosableFileFilter(new PDFFileFilter());

		if (chart != null) {
			int option = myChooser.showSaveDialog(chart);
			if (option == JFileChooser.APPROVE_OPTION) {
				if (myChooser.getSelectedFile() != null) {
					String filePath = myChooser.getSelectedFile().getPath();
					String filterDescription = myChooser.getFileFilter().getDescription();// getChoosableFileFilters();
					
					/* Remove file extension (that might be wrong) */
					if (!FilenameUtils.getExtension(filePath).equals("")) {
						filePath = FilenameUtils.removeExtension(filePath);
					}
					
					/* Get exporter for FileFilter */
					ChartExporter exporter = exporters.get(filterDescription);
					
					/* Get correct extension for the exporter */
					if (FilenameUtils.getExtension(filePath).equals("")) {
						filePath = filePath + exporter.getFileExtension();
					}
					
					exporter.exportChart(chart, filePath);
				} // if-SelectFile
			} // iF-ApproveOpt.
		}
	}

	/**
	 * 
	 * @param treePanel
	 */
	public static void exportLineageTree(JScrollPane treePanel) {
		JFileChooser chooserTreeExp = new JFileChooser();
		chooserTreeExp.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
		chooserTreeExp.setMultiSelectionEnabled(false);
		chooserTreeExp.setAcceptAllFileFilterUsed(false);
		chooserTreeExp.addChoosableFileFilter(new PNGFileFilter());
		chooserTreeExp.addChoosableFileFilter(new SVGFileFilter());
		chooserTreeExp.addChoosableFileFilter(new JPEGFileFilter());
		chooserTreeExp.addChoosableFileFilter(new PDFFileFilter());
		int option = chooserTreeExp.showSaveDialog(treePanel);
		if (option == JFileChooser.APPROVE_OPTION) {
			if (chooserTreeExp.getSelectedFile() != null) {
				String filePath = chooserTreeExp.getSelectedFile().getPath();
				String filterDescription = chooserTreeExp.getFileFilter().getDescription();// getChoosableFileFilters();
				BufferedImage bi = new BufferedImage((int) treePanel.getBounds().getWidth(), (int) treePanel.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = bi.createGraphics();

				// export graphic in png format
				if (filterDescription.equals("Portable Network Graphics (*.png)")) {
					filePath = filePath + ".png";
					treePanel.paint(g);
					g.dispose();
					try {
						ImageIO.write(bi, "png", new File(filePath));
					} catch (Exception ex) {
						GraphicsExporter.logger.error("Lineage tree could not be exported.", ex);
					}
				}
				// export graphic in svg format
				else if (filterDescription.equals("Scalable Vector Graphics (*.svg)")) {
					filePath = filePath + ".svg";
					// ----------------------
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
				// export graphic in jpeg format
				else if (filterDescription.equals("Portable Document Format (*.jpeg)")) {
					filePath = filePath + ".jpeg";
					BufferedImage expImage = new BufferedImage(treePanel.getWidth(), treePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
					/*
					 * Print to Image, scaling if necessary.
					 */
					Graphics2D g2 = expImage.createGraphics();
					treePanel.paint(g2);
					/*
					 * Write to File
					 */
					try {
						OutputStream out = new FileOutputStream(filePath);
						ImageIO.write(expImage, "jpeg", out);
						out.close();
					} catch (Exception ex) {
						GraphicsExporter.logger.error("Lineage tree could not be exported.", ex);
					}
				}
				// export graphic in pdf format
				else if (filterDescription.equals("Portable Document Format (*.pdf)")) {
					filePath = filePath + ".pdf";
					com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A3.rotate(), 0, 0, 0, 0);
					try {
						PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
						document.open();
						PdfContentByte contentByte = writer.getDirectContent();
						PdfTemplate template = contentByte.createTemplate(treePanel.getWidth(), treePanel.getHeight());
						Graphics2D g2 = template.createGraphics(treePanel.getWidth(), treePanel.getHeight());
						treePanel.print(g2);
						g2.dispose();
						contentByte.addTemplate(template, 0, 0);
					} catch (Exception ex) {
						GraphicsExporter.logger.error("Lineage tree could not be exported.", ex);
					} finally {
						if (document.isOpen()) {
							document.close();
						}
					}
				} // if-PDF
			} // if-SelectFile
		} // iF-ApproveOpt.
	}
}
