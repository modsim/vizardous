/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.graphics;

import java.awt.BorderLayout;

import vizardous.delegate.graphics.util.ColorIcon;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.DataModel;
import vizardous.model.Forest;
import vizardous.model.MIFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */

public final class ComparisonGrowthCurveChart2D extends JPanel {
    private JFreeChart      chart       = null ;
    private XYDataset       xyDataset     = null; 
    private JScrollPane     legendPanel = null;
    
    /**
     * Class constructor 
     * 
     * @param datamodel 
     */
    public ComparisonGrowthCurveChart2D(DataModel datamodel) {
        if( datamodel != null ) {
            this.setLayout(new BorderLayout());
            xyDataset = createDataset(datamodel);
            chart = createJFreeChart(xyDataset);
            final ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 250));
            chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214,214,214)));
            this.add(BorderLayout.CENTER, chartPanel);
            this.setBackground(Color.white);
        
        } else
            throw new IllegalArgumentException("Error: datamodel is null.");
    }
    
    public XYDataset createDataset(DataModel datamodel) { 
        final XYSeriesCollection datasetLocal = new XYSeriesCollection();
        // create the dataset...
        List<Forest> fList = datamodel.getForestsList();
        for( Forest forest: fList ) {
            if ( forest != null ) {
                // Add the of the complete forest in the chart
                final XYSeries series = new XYSeries("" + forest.getMetaxml().getProjectName());
                Collection<Cell> cells = forest.getAllCellsInMetaXML();
                for( Cell cell : cells ) {
                        MIFrame miframe = cell.getMIFrameObject();
                        if( miframe != null ) {
                            int cellNumber = miframe.getAllCellsInFrame().size();
                            /** Convert the time unit of minute to hour */
                            double frameElapsedTime = miframe.getElapsedTime() / 60;
                            series.add(frameElapsedTime, cellNumber);
                        } else
                            throw new IllegalArgumentException("Error: miframe is null.");
                    }
                   datasetLocal.addSeries(series);
            }
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
        
            final JFreeChart chartLocal = ChartFactory.createXYLineChart(
                 "Growth curve of several populations", 
                 "Time t [h]", 
                 "Cell Number N", 
                 dataset, 
                 PlotOrientation.VERTICAL, 
                 true, 
                 true, 
                 false);
        
            chartLocal.setBackgroundPaint(Color.white);
            LegendTitle legend = chartLocal.getLegend();
            legend.setPosition(RectangleEdge.RIGHT);
            legend.setVisible(true);

            final XYPlot plot = chartLocal.getXYPlot();
            plot.setBackgroundPaint(Color.white);
            plot.setRangeGridlinePaint(Color.lightGray);  
            plot.setDomainPannable(true);
            plot.setRangePannable(true);

            final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesLinesVisible(0,true);
            renderer.setSeriesShapesVisible(1, true);
            renderer.setBaseLinesVisible(true);
            renderer.setBaseShapesFilled(true);
            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.lightGray);
            plot.setRenderer(renderer);

            // customise the range axis...
            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setAutoRangeIncludesZero(false);
            rangeAxis.setStandardTickUnits(rangeAxis.getStandardTickUnits());

           
            return chartLocal;
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
}
