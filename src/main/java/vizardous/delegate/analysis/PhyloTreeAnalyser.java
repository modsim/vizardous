/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.data.category.DefaultCategoryDataset;

import vizardous.delegate.DrawPhyloTreeGraph;
import vizardous.delegate.MultipleChartsTab;
import vizardous.delegate.MyPopUpMenu;
import vizardous.delegate.TabPopUpMenu;
import vizardous.delegate.graphics.AbstractChart2D;
import vizardous.delegate.graphics.CellAreaChart2D;
import vizardous.delegate.graphics.CellAreaDistributionChart2D;
import vizardous.delegate.graphics.CellAreaIndividualGenerationsChart2D;
import vizardous.delegate.graphics.CellDivisionTimeChart2D;
import vizardous.delegate.graphics.CellDivisionTimeSistersCorrelation;
import vizardous.delegate.graphics.CellElongationRateSistesCorrelation;
import vizardous.delegate.graphics.CellFluorescenceChart2D;
import vizardous.delegate.graphics.CellFluorescenceDistributionChart2D;
import vizardous.delegate.graphics.CellFluorescenceIndividualGenerationsChart2D;
import vizardous.delegate.graphics.CellLengthChart2D;
import vizardous.delegate.graphics.CellLengthDistributionChart2D;
import vizardous.delegate.graphics.PopulationCellLengthBeforeAfterDivisionDistribution;
import vizardous.delegate.graphics.PopulationDivisionTimesChart2D;
import vizardous.delegate.graphics.PopulationDivisionTimesDistributionChart2D;
import vizardous.delegate.graphics.PopulationElongationrateDistributionChart2D;
import vizardous.delegate.graphics.PopulationGrowthCurveChart2D;
import vizardous.delegate.graphics.TraceChart2D;
import vizardous.delegate.table.CellInformationTableDeleteEvent;
import vizardous.delegate.table.CellInformationTableModel;
import vizardous.delegate.table.CellsComparisonTable;
import vizardous.delegate.table.CellsInformationTable;
import vizardous.delegate.table.MetadataTable;
import vizardous.delegate.table.StructuralDataTable;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Constants;
import vizardous.model.Forest;
import vizardous.model.MIFrame;
import vizardous.model.Phylogeny;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
public class PhyloTreeAnalyser extends javax.swing.JInternalFrame implements TableModelListener {

    private CellsInformationTable   cellInfoTable           = null;
    private CellsComparisonTable    cellCompTable           = null;
    private StructuralDataTable     structuralDataTable     = null;
    private MetadataTable           metadataTable           = null;
    private javax.swing.JScrollPane fJScrollPane            = null;
    private InternalFrameListener   internalFrameListener   = null;
    private Forest                  forest                  = null;
	private JScrollPane             cellInformationTabScrollPane            = null;
    private JScrollPane             structuralDataScrollPane            = null;
    private JScrollPane             metadataScrollPane            = null;
    private List<DrawPhyloTreeGraph>  drawedPhyloTreeList   = new ArrayList<DrawPhyloTreeGraph>();
    private MyPopUpMenu popuMenu = null;
    private int numberOfPhylogeniesInPhyloXML = 0;
    private javax.swing.JPanel mainPanel;

    
    private TraceChart2D yfpChart;
    private TraceChart2D yfpChartIndividual;
    private CellFluorescenceDistributionChart2D yfpDistributionChart;
    
    private TraceChart2D crimsonChart;
    private TraceChart2D crimsonChartIndividual;
    private CellFluorescenceDistributionChart2D crimsonDistributionChart;
    
    private TraceChart2D lengthChart;

    private TraceChart2D areaChart;
    private TraceChart2D areaIndividualChart;
    private TraceChart2D cellDivisionTimesChart;
    
    private CellLengthDistributionChart2D lengthDistributionChart;
    private CellAreaDistributionChart2D areaDistributionChart;
    private PopulationDivisionTimesDistributionChart2D divisionTimeOfPopulationChart2D;
    private PopulationDivisionTimesChart2D populationDivisionTimeChart2D;
    private PopulationGrowthCurveChart2D growthCurveOfPopulationChart2D;
    private PopulationCellLengthBeforeAfterDivisionDistribution cellLengthAfterDivisionChart2D;
    private PopulationElongationrateDistributionChart2D cellElongationrateChart2D;
    private CellElongationRateSistesCorrelation cellElongationSistersChart2D;
    private CellDivisionTimeSistersCorrelation cellDivisionTimeSistersChart2D;
    
    public PhyloTreeAnalyser() {}
    /**
     * Creates new form PhyloTreeAnalyser
     */
    public PhyloTreeAnalyser(Forest forest, CellsComparisonTable cellCompTable) {
        setTitle(forest.getMetaxml().getProjectName());
        cellInfoTable = new CellsInformationTable(this);
        structuralDataTable = new StructuralDataTable(this);
        metadataTable = new MetadataTable(this);
        this.cellCompTable = cellCompTable;
        this.forest = forest;
        List<Phylogeny> phyloList = forest.getPhyloxml().getPhylogenies();
        numberOfPhylogeniesInPhyloXML = phyloList.size();
        initComponents();
        init();
        addInternalFrameListener(getInternalFrameListener());
        
        for(Phylogeny phylo: phyloList) {
            DrawPhyloTreeGraph fTree = new DrawPhyloTreeGraph(this, forest, phylo, cellInfoTable, cellCompTable);
            mainPanel.add(fTree.getPhyloTreePanel());
            drawedPhyloTreeList.add(fTree);
            addStructuralDataToStructuralDataTable(phylo);
            AddMetadataToMetadataTable(phylo);
            popuMenu = new MyPopUpMenu(forest, phylo, fTree.getPhyloTreePanel(), cellInfoTable);
            
        }
        verticalSplitPane.setTopComponent(mainPanel);
        
        cellInfoTable.getModel().addTableModelListener(this);

        // First check whether there is some data in the tree that can be displayed
        if(forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS)) {
        	LinkedList<AbstractChart2D> charts = new LinkedList<AbstractChart2D>();
        	
        	yfpChart = new CellFluorescenceChart2D(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS);
        	charts.add(yfpChart);
	        cellInfoTable.getModel().addTableModelListener(yfpChart);
	        
	        yfpChartIndividual = new CellFluorescenceIndividualGenerationsChart2D(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS);
	        charts.add(yfpChartIndividual);
	        cellInfoTable.getModel().addTableModelListener(yfpChartIndividual);
	        
	        yfpDistributionChart = new CellFluorescenceDistributionChart2D(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS);
	        charts.add(yfpDistributionChart);
        	
        	/* Add custom tab */
	        String tabName = yfpChart.getTabName();
	        MultipleChartsTab multiChartTab = new MultipleChartsTab(jTabbedPane2, charts);
        	jTabbedPane2.addTab(tabName, multiChartTab);
        	
        	int index = jTabbedPane2.indexOfComponent(multiChartTab);
        	JPanel pnlTab = multiChartTab.getTabPanel();
    		jTabbedPane2.setTabComponentAt(index, pnlTab);
        }
        
        if(forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON)) {
        	LinkedList<AbstractChart2D> charts = new LinkedList<AbstractChart2D>();
        	
        	crimsonChart = new CellFluorescenceChart2D(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON);
	        charts.add(crimsonChart);
        	cellInfoTable.getModel().addTableModelListener(crimsonChart);
	        
	        crimsonChartIndividual = new CellFluorescenceIndividualGenerationsChart2D(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON);
	        charts.add(crimsonChartIndividual);
	        cellInfoTable.getModel().addTableModelListener(crimsonChartIndividual);
	        
	        crimsonDistributionChart = new CellFluorescenceDistributionChart2D(forest, Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON);
	        charts.add(crimsonDistributionChart);
	        
	        /* Add custom tab */
	        String tabName = crimsonChart.getTabName();
	        MultipleChartsTab multiChartTab = new MultipleChartsTab(jTabbedPane2, charts);
        	jTabbedPane2.addTab(tabName, multiChartTab);
        	
        	int index = jTabbedPane2.indexOfComponent(multiChartTab);
        	JPanel pnlTab = multiChartTab.getTabPanel();
    		jTabbedPane2.setTabComponentAt(index, pnlTab);
        }

        if(forest.containsLengthData()) {
        	LinkedList<AbstractChart2D> charts = new LinkedList<AbstractChart2D>();
        	
        	lengthChart = new CellLengthChart2D(forest);
        	charts.add(lengthChart);
	        cellInfoTable.getModel().addTableModelListener(lengthChart);
	        
	        lengthDistributionChart = new CellLengthDistributionChart2D(forest);
	        charts.add(lengthDistributionChart);
	        
	        cellLengthAfterDivisionChart2D = new PopulationCellLengthBeforeAfterDivisionDistribution(forest);
	        charts.add(cellLengthAfterDivisionChart2D);
	        
	        /* Add custom tab */
	        String tabName = lengthChart.getTabName();
	        MultipleChartsTab multiChartTab = new MultipleChartsTab(jTabbedPane2, charts);
	        jTabbedPane2.addTab(tabName, multiChartTab);
        	
        	int index = jTabbedPane2.indexOfComponent(multiChartTab);
        	JPanel pnlTab = multiChartTab.getTabPanel();
    		jTabbedPane2.setTabComponentAt(index, pnlTab);
        }

        if(forest.containsAreaData()) {
        	LinkedList<AbstractChart2D> charts = new LinkedList<AbstractChart2D>();
        	
        	areaChart = new CellAreaChart2D(forest);
        	charts.add(areaChart);
	        cellInfoTable.getModel().addTableModelListener(areaChart);
        
	        areaIndividualChart = new CellAreaIndividualGenerationsChart2D(forest);
	        charts.add(areaIndividualChart);
	        cellInfoTable.getModel().addTableModelListener(areaIndividualChart);
	        
	        areaDistributionChart = new CellAreaDistributionChart2D(forest);
	        charts.add(areaDistributionChart);
	        
	        /* Add custom tab */
        	String tabName = areaChart.getTabName();
	        MultipleChartsTab multiChartTab = new MultipleChartsTab(jTabbedPane2, charts);
	        jTabbedPane2.addTab(tabName, multiChartTab);
        	
        	int index = jTabbedPane2.indexOfComponent(multiChartTab);
        	JPanel pnlTab = multiChartTab.getTabPanel();
    		jTabbedPane2.setTabComponentAt(index, pnlTab);
        }

        LinkedList<AbstractChart2D> charts = new LinkedList<AbstractChart2D>();
        
        cellDivisionTimesChart = new CellDivisionTimeChart2D(forest);
        charts.add(cellDivisionTimesChart);
        cellInfoTable.getModel().addTableModelListener(cellDivisionTimesChart);
        
        divisionTimeOfPopulationChart2D = new PopulationDivisionTimesDistributionChart2D(forest);
        charts.add(divisionTimeOfPopulationChart2D);
        
        populationDivisionTimeChart2D = new PopulationDivisionTimesChart2D(forest);
        charts.add(populationDivisionTimeChart2D);
        
        /* Add custom tab */
    	String tabName = cellDivisionTimesChart.getTabName();
        MultipleChartsTab multiChartTab = new MultipleChartsTab(jTabbedPane2, charts);
        jTabbedPane2.addTab(tabName, multiChartTab);
    	
    	int index = jTabbedPane2.indexOfComponent(multiChartTab);
    	JPanel pnlTab = multiChartTab.getTabPanel();
		jTabbedPane2.setTabComponentAt(index, pnlTab);
        
        growthCurveOfPopulationChart2D = new PopulationGrowthCurveChart2D(forest);
        jTabbedPane2.addTab("Growth Curve", growthCurveOfPopulationChart2D);
        
//        cellElongationrateChart2D = new PopulationElongationrateDistributionChart2D(forest);
//        jTabbedPane2.addTab("Cell elongation rate", cellElongationrateChart2D);
//        
//        cellElongationSistersChart2D = new CellElongationRateSistesCorrelation(forest);
//        jTabbedPane2.addTab("Cell elongation rate sisters", cellElongationSistersChart2D);
//        
//        cellDivisionTimeSistersChart2D = new CellDivisionTimeSistersCorrelation(forest);
//        jTabbedPane2.addTab("Cell division time sisters", cellDivisionTimeSistersChart2D);
        
        // TODO Get the UI style and change it accordingly
//        jTabbedPane2.getUI();
        
        /* Sets a custom implementation of a mouse listener for the tabs. */
//        jTabbedPane2.setUI (new MetalTabbedPaneUI() {
//            protected MouseListener createMouseListener() {
//                return new TabPopUpMenu.CustomAdapter(jTabbedPane2);
//            }
//        });
	}

    public CellsInformationTable getCellInfoTable() {
        return cellInfoTable;
    }
    
    /**
     * Added all structural data to StructuralDataTable.
     * 
     * @param phylo 
     */
    private void addStructuralDataToStructuralDataTable(Phylogeny phylo) {
        List<Clade> cladeList = phylo.getAllCladeInPhylogeny();
        for(Clade clade : cladeList) {
            ((DefaultTableModel) structuralDataTable.getModel()).
            addRow(new Object[]{
                phylo.getRootClade().getName(), 
                clade.getName(), 
                clade.getBranchLength()});
        } 
    }
    
    /**
     * Added all metadata to metadata table.
     * 
     * @param phylo 
     */
    private void AddMetadataToMetadataTable(Phylogeny phylo) {
        List<Clade> cladeList = phylo.getAllCladeInPhylogeny();
        for(Clade clade : cladeList) {
            ((DefaultTableModel) metadataTable.getModel()).
            addRow(new Object[]{
                clade.getCellObject().getMIFrameObject().getFrameId(), 
                clade.getCellObject().getMIFrameObject().getElapsedTime(),
                clade.getCellObject().getMIFrameObject().getFilePath(), 
                clade.getCellObject().getId(), clade.getCellObject().getFluorescences().
                    get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS),
                clade.getCellObject().getFluorescences().
                    get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON),
                clade.getCellObject().getLength(), clade.getCellObject().getArea()
            });
         
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        horizoSplitPane = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        verticalSplitPane = new javax.swing.JSplitPane();
        fScrollPane3 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        cellsInformationPanel = new javax.swing.JPanel();
        plotChartsBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        structuralDataPanel = new javax.swing.JPanel();
        metaDataPanel = new javax.swing.JPanel();
        cellsComparisonPanel = new javax.swing.JPanel();

        setClosable(true);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(1050, 1024));
        setNormalBounds(new java.awt.Rectangle(0, 0, 1050, 650));
        setPreferredSize(new java.awt.Dimension(1050, 650));
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        //horizoSplitPane.setEnabled(true);
        horizoSplitPane.setOneTouchExpandable(true);
        horizoSplitPane.setDividerLocation(1.0);
        horizoSplitPane.setDividerSize(10);
        horizoSplitPane.setResizeWeight(1.0);
        horizoSplitPane.setMaximumSize(new java.awt.Dimension(1280, 1024));

        jPanel4.setMaximumSize(new java.awt.Dimension(1280, 1024));
        jPanel4.setPreferredSize(new java.awt.Dimension(1200, 585));

        verticalSplitPane.setOneTouchExpandable(true);
        //horizoSplitPane.setDividerLocation(0.30);
        verticalSplitPane.setBackground(new java.awt.Color(255, 255, 255));
        verticalSplitPane.setDividerLocation(1.0);
        verticalSplitPane.setDividerSize(10);
        verticalSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setResizeWeight(1.0);
        verticalSplitPane.setMaximumSize(new java.awt.Dimension(1280, 1024));
        //jSplitPane2.setResizeWeight(1.0);

        fScrollPane3.setVisible(true);
        fScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        fScrollPane3.setAutoscrolls(true);
        fScrollPane3.setEnabled(false);
        fScrollPane3.setPreferredSize(new java.awt.Dimension(600, 800));
        verticalSplitPane.setTopComponent(fScrollPane3);
        verticalSplitPane.setTopComponent(fScrollPane3);

        ////Integration of CellFluorescence-diagram
        //AbstractChart2D cellFluorescenceChart = new CellFluorescenceChart2D(dataModel, cladesParList);
        ////cellFluorescenceChart.setBackground(Color.white);
        //jTabbedPane2.addTab("Fluorescence time elapsed", cellFluorescenceChart);

        //Integration of CellLength-diagram
        //ibg1tool.view.impl.CellLengthChart2D cellLengthChart = new vizardous.delegate.impl.CellLengthChart2D(dataModel);
        //cellLengthChart.setBackground(Color.white);
        //jTabbedPane2.addTab("Length time elapsed", cellLengthChart);

        //Integration of CellArea-diagram
        //ibg1tool.view.impl.CellAreaChart2D cellAreaChart = new vizardous.delegate.impl.CellAreaChart2D(dataModel);
        //cellAreaChart.setBackground(Color.white);
        //jTabbedPane2.addTab("Area time elapsed", cellAreaChart);

        //cellFluorescenceChart.setPreferredSize(jTabbedPane2.getSize());
        //cellLengthChart.setPreferredSize(jTabbedPane2.getSize());
        //cellAreaChart.setPreferredSize(jTabbedPane2.getSize());

        verticalSplitPane.setBottomComponent(jTabbedPane2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(verticalSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(verticalSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
        );

        horizoSplitPane.setLeftComponent(jPanel4);

        jPanel5.setAutoscrolls(true);
        jPanel5.setMaximumSize(new java.awt.Dimension(1280, 1024));
        jPanel5.setPreferredSize(new java.awt.Dimension(2, 2));
        jPanel5.setRequestFocusEnabled(false);
        jPanel5.setVerifyInputWhenFocusTarget(false);

        jPanel6.setMaximumSize(new java.awt.Dimension(1280, 1024));

//        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cell image", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cambria", 1, 13))); // NOI18N
//
//        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cellImages/cells_redbluegree.png"))); // NOI18N
//
//        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
//        jPanel7.setLayout(jPanel7Layout);
//        jPanel7Layout.setHorizontalGroup(
//            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 237, Short.MAX_VALUE)
//            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGroup(jPanel7Layout.createSequentialGroup()
//                    .addGap(51, 51, 51)
//                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
//                    .addGap(26, 26, 26)))
//        );
//        jPanel7Layout.setVerticalGroup(
//            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 210, Short.MAX_VALUE)
//            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGroup(jPanel7Layout.createSequentialGroup()
//                    .addGap(27, 27, 27)
//                    .addComponent(jLabel8)
//                    .addContainerGap(27, Short.MAX_VALUE)))
//        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(224, 335));
        
        cellsInformationPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cellsInformationPanel.setMaximumSize(new java.awt.Dimension(1280, 1024));

        plotChartsBtn.setText("Plot Charts");
        plotChartsBtn.setMaximumSize(new java.awt.Dimension(93, 30));
        plotChartsBtn.setMinimumSize(new java.awt.Dimension(93, 30));
        plotChartsBtn.setPreferredSize(new java.awt.Dimension(93, 30));
        plotChartsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plotChartsBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.setMaximumSize(new java.awt.Dimension(93, 30));
        deleteBtn.setMinimumSize(new java.awt.Dimension(93, 30));
        deleteBtn.setPreferredSize(new java.awt.Dimension(93, 30));
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cellsInformationPanelLayout = new javax.swing.GroupLayout(cellsInformationPanel);
        cellsInformationPanel.setLayout(cellsInformationPanelLayout);
        cellsInformationPanelLayout.setHorizontalGroup(
            cellsInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cellsInformationPanelLayout.createSequentialGroup()
                .addComponent(plotChartsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        cellsInformationPanelLayout.setVerticalGroup(
            cellsInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cellsInformationPanelLayout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addGroup(cellsInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plotChartsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Cell Information", cellsInformationPanel);

        structuralDataPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout structuralDataPanelLayout = new javax.swing.GroupLayout(structuralDataPanel);
        structuralDataPanel.setLayout(structuralDataPanelLayout);
        structuralDataPanelLayout.setHorizontalGroup(
            structuralDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        structuralDataPanelLayout.setVerticalGroup(
            structuralDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Structural data", structuralDataPanel);

        metaDataPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout metaDataPanelLayout = new javax.swing.GroupLayout(metaDataPanel);
        metaDataPanel.setLayout(metaDataPanelLayout);
        metaDataPanelLayout.setHorizontalGroup(
            metaDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        metaDataPanelLayout.setVerticalGroup(
            metaDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Metadata", metaDataPanel);

        cellsComparisonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout cellsComparisonPanelLayout = new javax.swing.GroupLayout(cellsComparisonPanel);
        cellsComparisonPanel.setLayout(cellsComparisonPanelLayout);
        cellsComparisonPanelLayout.setHorizontalGroup(
            cellsComparisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        cellsComparisonPanelLayout.setVerticalGroup(
            cellsComparisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Cell Comparison table", cellsComparisonPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                    .addGap(9, 9, 9)))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(352, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(266, 266, 266)))
        );

        horizoSplitPane.setRightComponent(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(horizoSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(horizoSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JTabbedPane getChartsTabbedPane2() {
        return jTabbedPane2;
    }

    /**
     * Used to delete the selected cells and the associated diagrams.
     * 
     * return void 
     */
    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if(cellInfoTable.getModel().getRowCount()== 0) {
            JOptionPane.showMessageDialog(this,
                "The table is empty.",
                "Message",
                JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));

        } else if( cellInfoTable.getModel().getRowCount()> 0 ) {
            if(cellInfoTable.getSelectedRows().length == 0) {
                JOptionPane.showMessageDialog(this,
                    "You have to select at first one Cell or multiple!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/alert_3_32x32.png")));

            } else {
                int n = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to delete the selected cell(s)?",
                    "Question",
                    JOptionPane.YES_NO_OPTION, 0, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_help32x32.png")));
                if(n == JOptionPane.YES_OPTION) {
                    deleteSelectedCells(cellInfoTable);
                }
            }
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void plotChartsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotChartsBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_plotChartsBtnActionPerformed

    /**
     * Used to delete the selected cells.
     * 
     * return void 
     */
    public void deleteSelectedCells(JTable table) {
        int selectedRows[] = table.getSelectedRows();
            String selectedCellIDs [] = new String[selectedRows.length];
            for(int i = 0; i < selectedRows.length; i++){
                selectedCellIDs[i]= table.getValueAt(selectedRows[i], 0).toString();
            }
            for(int i = 0; i < table.getRowCount(); i++){
                for(int j = 0; j < selectedCellIDs.length; j++){
                    if(selectedCellIDs[j].equals(table.getValueAt(i, 0).toString()))
                    ((CellInformationTableModel)table.getModel()).removeRow(i);
                }
            }
    }
    
    private InternalFrameListener getInternalFrameListener() {
        if (internalFrameListener == null) {
                internalFrameListener = new InternalFrameAdapter() {
                    @Override
                    public void internalFrameActivated(InternalFrameEvent e) {
                    }

                  @Override
                        public void internalFrameClosing(InternalFrameEvent e) {
                        }

                        @Override
                        public void internalFrameClosed(InternalFrameEvent e) {
                        }
                };
        }
		return internalFrameListener;
    }

    private void init() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        double col = (Math.log(numberOfPhylogeniesInPhyloXML+1)/Math.log(2));
        if ( (col >= 0.0) && (col < 1.0) )
            col = 1;
        else if ( col > 1 )
            col = col;
        mainPanel.setLayout(new GridLayout(0, (int) col));
        /** JScrollPane for visualization of the cell Information table. */
        cellInformationTabScrollPane = new JScrollPane();
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(cellsInformationPanel);
        cellsInformationPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cellInformationTabScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cellInformationTabScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addGap(8, 8, 8)
                .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        cellInformationTabScrollPane.setViewportView(cellInfoTable);
        
        /** JScrollPane for visualization of the structural data (contain of the phyloXML file) table. */
        structuralDataScrollPane = new JScrollPane();
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(structuralDataPanel);
        structuralDataPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(structuralDataScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                )
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(structuralDataScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                )
        );
        structuralDataScrollPane.setViewportView(structuralDataTable);
        
        /** JScrollPane for visualization of the meta data (contain of the metaXML file) table. */
        metadataScrollPane = new JScrollPane();
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(metaDataPanel);
        metaDataPanel.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(metadataScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                )
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(metadataScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                )
        );
        metadataScrollPane.setViewportView(metadataTable);
    }
      
    public CellsInformationTable getCellInformationsTable() {
        return cellInfoTable;
    }
    /**
     * Gets a list of drawed phylogeny tree.
     * 
     * @return 
     * 
     */
    public List<DrawPhyloTreeGraph> getDrawedPhyloTreeList() {
        return drawedPhyloTreeList;
    }
    
    /**
     * 
     * 
     * @return 
     */
    public MyPopUpMenu getPopuMenu() {
        return popuMenu;
    }
    

    @Override
    public void tableChanged(TableModelEvent e) {			
            // Row removed
            if (e.getType() == TableModelEvent.DELETE) {		
                    // Note: CellInformationTableDeleteEvent provides the cellId
                    if (e instanceof CellInformationTableDeleteEvent) {
                            String cellId = ((CellInformationTableDeleteEvent) e).getCellId();
                            Cell cell = this.forest.getCellById(cellId);

                            // Remove color
                    for(DrawPhyloTreeGraph obj: drawedPhyloTreeList) {
                       if(obj.getPhyloTreeGraph().containsVertex(cell)) {
                            obj.colorizePathCell(cell, Color.BLACK);
                       }
                    }

                    CellInformationTableModel model = (CellInformationTableModel) e.getSource();
                    for (int i=0; i < model.getRowCount(); i++) {
                            String otherCellId = (String) model.getValueAt(i, 0);
                            Cell otherCell = this.forest.getCellById(otherCellId);

                            for(DrawPhyloTreeGraph obj: drawedPhyloTreeList) {
                               if(obj.getPhyloTreeGraph().containsVertex(otherCell)) {
                                       List<Color> colorPalette = this.yfpChart.getColorPalette();
                                       int seriesIndex = ((CellFluorescenceChart2D) this.yfpChart).getSeriesIndex("Cell ID "+otherCellId);

                                       obj.colorizePathCell(otherCell, colorPalette.get(seriesIndex));
                               }
                            }
                    }
                    }		
            }

            // Row added
            if (e.getType() == TableModelEvent.INSERT) {
                    TableModel tm = null;
                    if (e.getSource() instanceof TableModel) {
                            tm = (TableModel) e.getSource();
                    }

                    String cellId = (String) tm.getValueAt(e.getFirstRow(), 0);
                    Cell cell = this.forest.getCellById(cellId);

                    // Colorize the trace of the plotted cells
            for(DrawPhyloTreeGraph obj: drawedPhyloTreeList) {
               if(obj.getPhyloTreeGraph().containsVertex(cell)) {
                       List<Color> colorPalette = this.yfpChart.getColorPalette();
                       int rowIndex = ((CellFluorescenceChart2D) this.yfpChart).getSeriesIndex("Cell ID "+cellId);

                       obj.colorizePathCell(cell, colorPalette.get(rowIndex));
               }
            }	
            }

            // TODO Row changed (minor importance, will it ever happen?)

    mainPanel.updateUI();
    }

    public TraceChart2D getAreaChart() {
    return areaChart;
}

    public TraceChart2D getCellDivisionTimesChart() {
        return cellDivisionTimesChart;
    }

    public CellFluorescenceDistributionChart2D getYfpDistributionChart() {
        return yfpDistributionChart;
    }
    
    public CellFluorescenceDistributionChart2D getCrimsonDistributionChart() {
        return crimsonDistributionChart;
    }
    
    @Deprecated
    public CellFluorescenceDistributionChart2D getFluorescenceDistributionChart() {
        return yfpDistributionChart;
    }

    public CellLengthDistributionChart2D getLengthDistributionChart() {
        return lengthDistributionChart;
    }

    public CellAreaDistributionChart2D getAreaDistributionChart() {
        return areaDistributionChart;
    }

    public PopulationDivisionTimesDistributionChart2D getDivisionTimeOfPopulationChart2D() {
        return divisionTimeOfPopulationChart2D;
    }

    public PopulationGrowthCurveChart2D getGrowthCurveOfPopulationChart2D() {
        return growthCurveOfPopulationChart2D;
    }

    public PopulationCellLengthBeforeAfterDivisionDistribution getCellLengthAfterDivisionChart2D() {
        return cellLengthAfterDivisionChart2D;
    }

    public CellDivisionTimeSistersCorrelation getCellDivisionTimeSistersChart2D() {
        return cellDivisionTimeSistersChart2D;
    }

    public TraceChart2D getYFPChart() {
        return yfpChart;
    }

    public TraceChart2D getCrimsonChart() {
        return crimsonChart;
    }

    public TraceChart2D getLengthChart() {
        return lengthChart;
    }
    
    /**
	 * @return the forest
	 */
	public Forest getForest() {
		return forest;
	}
	/**
	 * @param forest the forest to set
	 */
	public void setForest(Forest forest) {
		this.forest = forest;
	}  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cellsComparisonPanel;
    private javax.swing.JPanel cellsInformationPanel;
    private javax.swing.JButton deleteBtn;
    public javax.swing.JScrollPane fScrollPane3;
    private javax.swing.JSplitPane horizoSplitPane;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JPanel metaDataPanel;
    private javax.swing.JButton plotChartsBtn;
    private javax.swing.JPanel structuralDataPanel;
    public static javax.swing.JSplitPane verticalSplitPane;
    // End of variables declaration//GEN-END:variables
    
}
