/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.analysis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vizardous.delegate.graphics.CellAreaDistributionChart2D;
import vizardous.delegate.graphics.CellFluorescenceDistributionChart2D;
import vizardous.delegate.graphics.CellLengthDistributionChart2D;
import vizardous.model.Cell;
import vizardous.model.Phylogeny;

/**
 * TODO Documentation
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PhyloTreesDepthAnalysis extends javax.swing.JFrame {

    private Phylogeny phylo = null; 
    private ButtonGroup groupButtonCellsCharac  = new ButtonGroup();
    private ButtonGroup groupButtonFluorescencesTyp  = new ButtonGroup();
    private List<Cell> cells = null;
    int depthIndex;
    
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(PhyloTreesDepthAnalysis.class);
    
    /**
     * Creates new form PhyloTreesDepthAnalysis
     */
    public PhyloTreesDepthAnalysis(Phylogeny phylo) {
        this.phylo = phylo;
        this.setTitle("Depth Analysis for the population with the root cell: "  
                + phylo.getRootClade().getCellObject().getId());
        initComponents();
        init();
        initJComBoxLevels();
        levelsComBox.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                fluorescencesRBIALClass.setEnabled(true);
                lengthRBIALClass.setEnabled(true);
                areaRBIALClass.setEnabled(true);
                levelsComBoxActionListner(ae);
            }
        });
        fluorescencesRBIALClass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                 fluorescence1RBIALClass.setEnabled(true);
                 fluorescence2RBIALClass.setEnabled(true);
            }
        });
        
        fluorescence1RBIALClass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                CellFluorescenceDistributionChart2D cellFluorescenceDistributionChart = new CellFluorescenceDistributionChart2D(cells, "yfp");
                showChartWindow(cellFluorescenceDistributionChart);
            }
        });
        
        fluorescence2RBIALClass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                CellFluorescenceDistributionChart2D cellFluorescenceDistributionChart = new CellFluorescenceDistributionChart2D(cells, "crimson");
                showChartWindow(cellFluorescenceDistributionChart);
            }
        });
        
        lengthRBIALClass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                fluorescence1RBIALClass.setEnabled(false);
                fluorescence2RBIALClass.setEnabled(false);
               CellLengthDistributionChart2D cellLengthDistributionChart = new CellLengthDistributionChart2D(cells);
               showChartWindow(cellLengthDistributionChart);
            }
        });
        
        areaRBIALClass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                fluorescence1RBIALClass.setEnabled(false);
                fluorescence2RBIALClass.setEnabled(false);
                CellAreaDistributionChart2D cellAreaDistributionChart = new CellAreaDistributionChart2D(cells);
                showChartWindow(cellAreaDistributionChart);
            }
        });
    }

    private void initJComBoxLevels() {
        int numberOfLevel = phylo.numberOfLevel();
        for(int i=numberOfLevel; i>0; i--) 
            levelsComBox.addItem(i);
    }
    
    private void levelsComBoxActionListner(ActionEvent ae) {
        JComboBox cb = (JComboBox)ae.getSource();
        depthIndex = Integer.parseInt(cb.getSelectedItem().toString());
        logger.info("depthIndex " + depthIndex);
        cells = phylo.getForest().cellsInSameLevel(depthIndex, phylo);
    }
    
    private void showChartWindow(JPanel ChartPanel) {
        JFrame showChartFrame = new JFrame("Histogram of Depth " + depthIndex 
                + " in population with root cell: " 
                + phylo.getRootClade().getCellObject().getId());
        showChartFrame.getContentPane().add(ChartPanel);
        showChartFrame.setVisible(true);
        showChartFrame.setSize(500, 300);
    }
    private void init() {
        fluorescencesRBIALClass.setEnabled(false);
        lengthRBIALClass.setEnabled(false);
        areaRBIALClass.setEnabled(false);
        fluorescence1RBIALClass.setEnabled(false);
        fluorescence2RBIALClass.setEnabled(false);
        
        groupButtonCellsCharac.add(areaRBIALClass);
        groupButtonCellsCharac.add(lengthRBIALClass);
        groupButtonCellsCharac.add(fluorescencesRBIALClass);
        
        groupButtonFluorescencesTyp.add(fluorescence1RBIALClass);
        groupButtonFluorescencesTyp.add(fluorescence2RBIALClass);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        selectedDepthLb = new javax.swing.JLabel();
        levelsComBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        fluorescencesRBIALClass = new javax.swing.JRadioButton();
        fluorescence1RBIALClass = new javax.swing.JRadioButton();
        fluorescence2RBIALClass = new javax.swing.JRadioButton();
        lengthRBIALClass = new javax.swing.JRadioButton();
        areaRBIALClass = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Plot Histogram for a particular cell character over a selected depth: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        selectedDepthLb.setText("Depth");

        levelsComBox.setToolTipText("The lowest level represent the level that contains leaves and highest level represent the level that contains root cell.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectedDepthLb, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(levelsComBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectedDepthLb)
                    .addComponent(levelsComBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fluorescencesRBIALClass.setText("Fluorescences");

        fluorescence1RBIALClass.setText("YFP fluorescence");
        fluorescence1RBIALClass.setEnabled(false);
        fluorescence1RBIALClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fluorescence1RBIALClassActionPerformed(evt);
            }
        });

        fluorescence2RBIALClass.setText("CRIMSON fluorescence");
        fluorescence2RBIALClass.setEnabled(false);

        lengthRBIALClass.setText("Length");

        areaRBIALClass.setText("Area");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(areaRBIALClass)
                    .addComponent(lengthRBIALClass)
                    .addComponent(fluorescencesRBIALClass))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fluorescence2RBIALClass)
                    .addComponent(fluorescence1RBIALClass))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fluorescence1RBIALClass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fluorescence2RBIALClass)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fluorescencesRBIALClass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lengthRBIALClass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(areaRBIALClass)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fluorescence1RBIALClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fluorescence1RBIALClassActionPerformed
    }//GEN-LAST:event_fluorescence1RBIALClassActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton areaRBIALClass;
    private javax.swing.JRadioButton fluorescence1RBIALClass;
    private javax.swing.JRadioButton fluorescence2RBIALClass;
    private javax.swing.JRadioButton fluorescencesRBIALClass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton lengthRBIALClass;
    private javax.swing.JComboBox levelsComBox;
    private javax.swing.JLabel selectedDepthLb;
    // End of variables declaration//GEN-END:variables
}
