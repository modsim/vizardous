/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.FilenameUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import vizardous.delegate.impl.graphics.util.ColorIcon;
import vizardous.model.impl.Forest;

/**
 * This class defines common functionality of most of the charts implemented:
 * 
 * <ul>
 * 	<li>Handling of legends</li>
 * 	<li>Export to SVG</li>
 * </ul>
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0
 * 
 */
public abstract class AbstractChart2D extends JPanel implements ActionListener {
    
	private static final long serialVersionUID = 1L;

	/** The Forest object from which the chart is constructed. */
    protected Forest forest = null;
    
    /** The resulting chart. */
    protected JFreeChart chart = null ;
    
    /** The underlying dataset for this chart. */
    protected XYDataset dataset = null;
    
    /** The color palette that is used for this chart. */
    protected List<Color> colorPalette = null;
    
    /** The {@link JScrollPane} that shows the legend. */
    protected JScrollPane legendPanel  = null;
    
    /** The context menu entry for saving the charts underlying data. */
    protected JMenuItem dumpDataItem = new JMenuItem("Dump data to file");
    
    /** The context menu entry for saving the chart to SVG. */
    protected JMenuItem saveSVGItem = new JMenuItem("Save as SVG");
    
    /** The tab name of this chart */
	protected String tabName = "n/a";
    
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * Default constructor.
     */
    public AbstractChart2D() {}
    
	/**
	 * Constructor.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
    public AbstractChart2D(Forest forest) {
    	this.forest = forest;
    }
    
	/**
	 * Creates the {@link JFreeChart} that this chart shows.
	 * 
	 * @param dataset
	 *            The {@link XYDataset} that backs the created
	 *            {@link JFreeChart}.
	 * @return A {@link JFreeChart} instance that is shown by this chart.
	 */
    public abstract JFreeChart createJFreeChart(XYDataset dataset);
    
	/**
	 * Getter for the color palette of this chart.
	 * 
	 * @return List<Color>
	 */
	public List<Color> getColorPalette() {
		return colorPalette;
	}
    
	/**
	 * This method is called when there was a right click on the chart.
	 */
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dumpDataItem) {
	    	JFileChooser fileChooser = new JFileChooser();
	    	
	    	FileNameExtensionFilter csvFilter = new FileNameExtensionFilter(".csv", "csv");
	    	fileChooser.addChoosableFileFilter(csvFilter);
	    	
	    	FileNameExtensionFilter xlsFilter = new FileNameExtensionFilter(".xlsx", "xlsx");
			fileChooser.addChoosableFileFilter(xlsFilter);
			
			int returnVal = fileChooser.showSaveDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				/* Get filename extension */
				File selectedFile = fileChooser.getSelectedFile();
				String filenameExtension = FilenameUtils.getExtension(selectedFile.getAbsolutePath());
				
				/* Elicit from filename extension which exporter to use */
				if (filenameExtension.equals("csv")) {
					exportToCSV(fileChooser.getSelectedFile());
				}
				
				if (filenameExtension.equals("xlsx")) {
					exportToExcel(fileChooser.getSelectedFile());
				} 
			}
		} else if (e.getSource() == saveSVGItem) {
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showSaveDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				saveSVG(fileChooser.getSelectedFile());
			}
		}
	}
    
	/**
	 * Exports the data from this chart to CSV for further processing.
	 * 
	 * @param f File to which the data is exported.
	 */
    public abstract void exportToCSV(File f);
    
    /**
	 * Exports the data from this chart to XLS for further processing.
	 * 
	 * @param f File to which the data is exported.
	 */
    public abstract void exportToExcel(File f);
    
	/**
	 * Exports this chart to SVG (vector graphics) using Batik's
	 * {@link SVGGraphics2D} for drawing.
	 * 
	 * @param svgFile
	 *            The {@link File} to which the SVG content is written.
	 */
    public void saveSVG(File svgFile) {
    	/* Get a DOMImplementation and create an XML document */
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);

        /* Draw the chart in the SVG generator */
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        chart.draw(svgGenerator, this.getBounds());

        /* Write SVG file */  
		try {
			OutputStream outputStream = new FileOutputStream(new File(svgFile.getPath() + ".svg"));
			Writer out = new OutputStreamWriter(outputStream, "UTF-8");
                        svgGenerator.stream(out, true /* use css */);						
                        outputStream.flush();
                        outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Logging
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Logging
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Logging
			e.printStackTrace();
		} 
    }
    
	/**
	 * Create a {@link JScrollPane} that holds the custom legend of the chart.
	 * 
	 * It takes the legend items from a provided {@link JFreeChart} and
	 * translates those items into standard labels with an according icon.
	 * 
	 * @param chart
	 *            The {@link JFreeChart} for which the legend is to be created.
	 * @return A {@link JScrollPane} that holds the legend panel.
	 */
    protected JScrollPane createLegendPanel(JFreeChart chart) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBackground(Color.white);
        panel.setSize(100,150);
        
        Iterator iterator = chart.getPlot().getLegendItems().iterator();
        while (iterator.hasNext()) {
            LegendItem item = (LegendItem) iterator.next();
            JLabel label = new JLabel(item.getLabel());
            label.setIcon(new ColorIcon(8, (Color) item.getFillPaint()));
            panel.add(label);
         }
        
        JScrollPane scrolPane = new JScrollPane();
        scrolPane.setViewportView(panel);
        scrolPane.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214,214,214)));
        
        return scrolPane;
    }
    
    /**
	 * Changes various appearance settings that are common to all the trace
	 * charts.
	 * 
	 * @param chart
	 *            A {@link JFreeChart} for which the common trace style is to be
	 *            applied.
	 * @return A {@link JFreeChart} that has the common trace style.
	 */
	protected JFreeChart styleChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.WHITE);
		
		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setVisible(false);
		
		return chart;
	}
	
	/**
	 * TODO Documentation
	 * 
	 * @return name of the tab for this chart.
	 */
	public String getTabName() {
		return this.tabName;
	}
	
	/**
	 * Returns the underlying {@link JFreeChart} instance that is for example
	 * used for exporting to vector graphics formats.
	 * 
	 * @return The wrapped {@link JFreeChart} instance.
	 */
	public JFreeChart getChart() {
		return this.chart;
	}
}
