/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics.export;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
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
	
    /** TODO Documentation */
    final static Map<String, ChartExporter> chartExporters = new HashMap<String, ChartExporter>();
    
    /** TODO Documentation */
    final static Map<String, LineageExporter> lineageExporters = new HashMap<String, LineageExporter>();
    
    static {
    	chartExporters.put("Portable Network Graphics (*.png)", new PngExporter());
    	chartExporters.put("Scalable Vector Graphics (*.svg)", new SvgExporter());
    	chartExporters.put("Joint Photographic Experts Group Format (*.jpeg)", new JpegExporter());
    	chartExporters.put("Portable Document Format (*.pdf)", new PdfExporter());
    	
    	lineageExporters.put("Portable Network Graphics (*.png)", new PngExporter());
    	lineageExporters.put("Scalable Vector Graphics (*.svg)", new SvgExporter());
    	lineageExporters.put("Joint Photographic Experts Group Format (*.jpeg)", new JpegExporter());
    	lineageExporters.put("Portable Document Format (*.pdf)", new PdfExporter());
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
					ChartExporter exporter = chartExporters.get(filterDescription);
					
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
	public static void exportLineageTree(mxGraphComponent graphComponent, Clipping clipping) {
		JFileChooser chooserTreeExp = new JFileChooser();
		chooserTreeExp.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
		chooserTreeExp.setMultiSelectionEnabled(false);
		chooserTreeExp.setAcceptAllFileFilterUsed(false);
		chooserTreeExp.addChoosableFileFilter(new PNGFileFilter());
		chooserTreeExp.addChoosableFileFilter(new SVGFileFilter());
		chooserTreeExp.addChoosableFileFilter(new JPEGFileFilter());
		chooserTreeExp.addChoosableFileFilter(new PDFFileFilter());
		
		int option = chooserTreeExp.showSaveDialog(graphComponent);
		if (option == JFileChooser.APPROVE_OPTION) {
			if (chooserTreeExp.getSelectedFile() != null) {
				String filePath = chooserTreeExp.getSelectedFile().getPath();
				String filterDescription = chooserTreeExp.getFileFilter().getDescription();// getChoosableFileFilters();

				/* Remove file extension (that might be wrong) */
				if (!FilenameUtils.getExtension(filePath).equals("")) {
					filePath = FilenameUtils.removeExtension(filePath);
				}
				
				/* Get exporter for FileFilter */
				LineageExporter exporter = lineageExporters.get(filterDescription);
				
				/* Get correct extension for the exporter */
				if (FilenameUtils.getExtension(filePath).equals("")) {
					filePath = filePath + exporter.getFileExtension();
				}
				
				exporter.exportLineage(graphComponent, clipping, filePath);				
			} // if-SelectFile
		} // iF-ApproveOpt.
	}
}
