/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics.export;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxgraph.swing.mxGraphComponent;

import vizardous.delegate.impl.fileFilter.JPEGFileFilter;
import vizardous.delegate.impl.fileFilter.PDFFileFilter;
import vizardous.delegate.impl.fileFilter.PNGFileFilter;
import vizardous.delegate.impl.fileFilter.SVGFileFilter;
import vizardous.delegate.impl.graphics.AbstractChart2D;

/**
 * Central entry point for export functionality of charts and lineage trees.
 * 
 * Methods are called via menu entries for exporting charts and the context menu
 * when exporting a lineage tree.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class GraphicsExporter {

	/** The {@link Logger} for this class. */
	final static Logger logger = LoggerFactory.getLogger(GraphicsExporter.class);

	/**
	 * A map that establishes the connection between {@link FileFilter}
	 * descriptions and {@link ChartExporter}s.
	 */
	final static Map<FileFilter, ChartExporter> chartExporters = new HashMap<FileFilter, ChartExporter>();

	/**
	 * A map that establishes the connection between {@link FileFilter}
	 * descriptions and {@link LineageExporter}s.
	 */
	final static Map<FileFilter, LineageExporter> lineageExporters = new HashMap<FileFilter, LineageExporter>();

	static {
		/* Initialize the available ChartExporters */
		chartExporters.put(PNGFileFilter.getInstance(), new PngExporter());
		chartExporters.put(SVGFileFilter.getInstance(), new SvgExporter());
		chartExporters.put(JPEGFileFilter.getInstance(), new JpegExporter());
		chartExporters.put(PDFFileFilter.getInstance(), new PdfExporter());

		/* Initialize the available LineageExporters */
		lineageExporters.put(PNGFileFilter.getInstance(), new PngExporter());
		lineageExporters.put(SVGFileFilter.getInstance(), new SvgExporter());
		lineageExporters.put(JPEGFileFilter.getInstance(), new JpegExporter());
		lineageExporters.put(PDFFileFilter.getInstance(), new PdfExporter());
	}

	/**
	 * Exports an {@link AbstractChart2D} to a file. To that end, a file chooser
	 * is shown, that determines the location of the exported image.
	 * 
	 * @param chart
	 *            The {@link AbstractChart2D} that is exported.
	 * @param chartArt
	 *            The type of chart that is exported.
	 */
	public static void exportChart2D(AbstractChart2D chart, String chartArt) {
		JFileChooser myChooser = GraphicsExporter.createFileChooser();

		if (chart != null) {
			int option = myChooser.showSaveDialog(chart);
			if (option == JFileChooser.APPROVE_OPTION) {
				if (myChooser.getSelectedFile() != null) {
					String filePath = myChooser.getSelectedFile().getPath();
					FileFilter filter = myChooser.getFileFilter();

					/* Remove file extension (that might be wrong) */
					if (!FilenameUtils.getExtension(filePath).equals("")) {
						filePath = FilenameUtils.removeExtension(filePath);
					}

					/* Get exporter for FileFilter */
					ChartExporter exporter = chartExporters.get(filter);

					/* Get correct extension for the exporter */
					if (FilenameUtils.getExtension(filePath).equals("")) {
						filePath = filePath + exporter.getFileExtension();
					}

					exporter.exportChart(chart, filePath);
				}
			}
		}
	}

	/**
	 * Exports a lineage tree in form of an {@link mxGraphComponent} to a file.
	 * To that end, a file chooser is shown, that determines the location of the
	 * exported image.
	 * 
	 * It is coupled with the lineage tree visualization so that either the
	 * complete tree or a clipped version (as seen on the screen) is exported.
	 * Furthermore, the current zoom level of the visualization is used for the
	 * export.
	 * 
	 * @param graphComponent
	 *            The {@link mxGraphComponent} of the lineage tree that is
	 *            exported.
	 * @param clipping
	 *            An option to select wether the complete tree or a clipped
	 *            version is exported.
	 */
	public static void exportLineageTree(mxGraphComponent graphComponent,	Clipping clipping) {
		JFileChooser chooserTreeExp = GraphicsExporter.createFileChooser();

		int option = chooserTreeExp.showSaveDialog(graphComponent);
		if (option == JFileChooser.APPROVE_OPTION) {
			if (chooserTreeExp.getSelectedFile() != null) {
				String filePath = chooserTreeExp.getSelectedFile().getPath();
				FileFilter filter = chooserTreeExp.getFileFilter();

				/* Remove file extension (that might be wrong) */
				if (!FilenameUtils.getExtension(filePath).equals("")) {
					filePath = FilenameUtils.removeExtension(filePath);
				}

				/* Get exporter for FileFilter */
				LineageExporter exporter = lineageExporters.get(filter);

				/* Get correct extension for the exporter */
				if (FilenameUtils.getExtension(filePath).equals("")) {
					filePath = filePath + exporter.getFileExtension();
				}

				exporter.exportLineage(graphComponent, clipping, filePath);
			}
		}
	}
	
	/**
	 * Creates a file chooser with the available exporters (curated manually)
	 * 
	 * @return A {@link JFileChooser} of the user's home directory without
	 *         multi-selection enabled and the avilable file filters.
	 */
	private static JFileChooser createFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(PNGFileFilter.getInstance());
		chooser.addChoosableFileFilter(SVGFileFilter.getInstance());
		chooser.addChoosableFileFilter(JPEGFileFilter.getInstance());
		chooser.addChoosableFileFilter(PDFFileFilter.getInstance());
		
		return chooser;
	}
}
