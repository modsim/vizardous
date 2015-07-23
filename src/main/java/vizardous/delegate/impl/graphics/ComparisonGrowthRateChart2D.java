package vizardous.delegate.impl.graphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;

import vizardous.model.impl.MIFrame;
import vizardous.model.impl.MetaXMLException;
import vizardous.model.impl.PhyloXMLException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import vizardous.delegate.impl.graphics.util.ColorIcon;
import vizardous.model.computations.GrowthRateCalculator;
import vizardous.model.impl.Cell;
import vizardous.model.impl.DataModel;
import vizardous.model.impl.Forest;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */

public final class ComparisonGrowthRateChart2D extends JPanel {
    
    private DataModel              datamodel      = null;
    private JFreeChart      chart       = null ;
    private XYDataset       dataset     = null; 
    
    public ComparisonGrowthRateChart2D(DataModel datamodel) {
        this.datamodel = datamodel;
        if( datamodel != null ) {
            this.setLayout(new BorderLayout());
            dataset = createDataset();
            chart = createJFreeChart(dataset);
            final ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 250));
            chartPanel.setMouseZoomable(true, false);
            chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214,214,214)));
            this.add(BorderLayout.CENTER, chartPanel);
            this.setBackground(Color.white);
        } else
            throw new IllegalArgumentException("Error");
    }
    
    public XYDataset createDataset() {              
        // create the dataset...
        double x, x0;
        final XYSeriesCollection datasetLocal = new XYSeriesCollection();
        List<Forest> fList = datamodel.getForestsList();
        for( Forest forest: fList ) {
            
            if ( forest != null ) {
                GrowthRateCalculator growthRateCalculator = new GrowthRateCalculator(forest);
                double growthRate = growthRateCalculator.processNew();
                final XYSeries seriesGrowthRate = new XYSeries("" + forest.getMetaxml().getProjectName() + " µ: " + String.format("%.3f", growthRate));
                
                List<MIFrame> miFramesList = forest.getMetaxml().getAllFrames();
                int miFramesListsize = miFramesList.size();
                x0 = miFramesList.get(0).getAllCellsInFrame().size();
                for(int i = 0; i<miFramesListsize; i++) {
                    double elapsedTime = miFramesList.get(i).getElapsedTime() / 60;
                    x = x0 * (Math.pow(Math.E, growthRate*elapsedTime));
                    seriesGrowthRate.add(elapsedTime, x);
                 }
                // Add a new series for the growth curve 
                final XYSeries seriesGrowthCurve = new XYSeries("" + forest.getMetaxml().getProjectName());
                Collection<Cell> cells = forest.getAllCellsInMetaXML();
                for( Cell cell : cells ) {
                        MIFrame miframe = cell.getMIFrameObject();
                        if( miframe != null ) {
                            int cellNumber = miframe.getAllCellsInFrame().size();
                            /** Convert the time unit of minute to hour */
                            double frameElapsedTime = miframe.getElapsedTime() / 60;
                            seriesGrowthCurve.add(frameElapsedTime, cellNumber);
                        } else
                            throw new IllegalArgumentException("Error: miframe is null.");
                 }
                
                // Add series to dataset
                datasetLocal.addSeries(seriesGrowthRate);
                datasetLocal.addSeries(seriesGrowthCurve);
                
            }  else
                    throw new IllegalArgumentException("Error");
        }
        return datasetLocal;
    }
    
    /**
     * Creates a Growth rate Of Population chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return The chart.
     */
    public JFreeChart createJFreeChart(final XYDataset dataset) {
            final JFreeChart chart = ChartFactory.createXYLineChart(
                 "Growth rate and growth curve of several populations", 
                 "Time t [h]", 
                 "Cell Number N", 
                 dataset, 
                 PlotOrientation.VERTICAL, 
                 true, 
                 true, 
                 false);
        
            chart.setBackgroundPaint(Color.white);
            LegendTitle legend = chart.getLegend();
            legend.setPosition(RectangleEdge.RIGHT);
            legend.setVisible(true);
            
            final XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(Color.white);
            plot.setRangeGridlinePaint(Color.lightGray); 

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(){
                    Stroke soild = new BasicStroke(2.0f);
                    Stroke dashed =  new BasicStroke(1.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] {5.0f}, 0.0f);
                    @Override
                    public Stroke getItemStroke(int row, int column) {
                        if ( (row % 2) == 1) {
                                return dashed;
                        } else
                            return super.getItemStroke(row, column);
                    }
                };
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setBaseStroke(new BasicStroke(3));
            plot.setRenderer(renderer);
            
            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.lightGray);

            // customise the range axis...
            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setAutoRangeIncludesZero(false);
            rangeAxis.setStandardTickUnits(rangeAxis.getStandardTickUnits());

            return chart;
        }   

    /**
     * Create a panel to represent the legend of chart
     * 
     * @param chart
     * @return Legend panel
     */
    private JScrollPane createLegendPanel(JFreeChart chart) {
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
    
    // Test method
    public static void main(String[] args) {
        File f1 = new File("C:\\Users\\Azzouzi\\Desktop\\xmlTestFiles\\testDataMultiplePhylogenies"
                + "\\PHH1001.nd2-PHH1001.nd2(series11)_preprocessed_cropped_rois-1_48_final-1.xml");
        File f2 = new File("C:\\Users\\Azzouzi\\Desktop\\xmlTestFiles\\testDataMultiplePhylogenies"
                + "\\PHH1001.nd2-PHH1001.nd2(series11)_preprocessed_cropped_rois-1_48_final-1_meta - Kopie.xml");
        Map<File, File> map = new HashMap<File, File>();
        map.put(f1, f2);
        ComparisonGrowthRateChart2D growthRateOfPopulationChart = null;
		try {
			growthRateOfPopulationChart = new ComparisonGrowthRateChart2D(new DataModel(map.entrySet()));
		} catch (PhyloXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MetaXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JFrame  testFrame = new JFrame("Test Window");
        testFrame.setSize(500, 500);
        testFrame.getContentPane().add(growthRateOfPopulationChart);
        testFrame.setVisible(true);
    }
}
