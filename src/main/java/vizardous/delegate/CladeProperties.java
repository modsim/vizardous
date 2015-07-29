/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate;

import java.awt.Color;
import java.awt.Point;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import vizardous.delegate.table.CellInformationTableModel;
import vizardous.delegate.table.CellsComparisonTable;
import vizardous.delegate.table.CellsInformationTable;
import vizardous.model.Phylogeny;

/**
 * This class shows information about a clade and the associated cell. 
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */
public class CladeProperties extends javax.swing.JFrame {

    /**
     * Creates new form CladeProperties
     */
    private CellsInformationTable cellInfoTable = null;
    private CellsComparisonTable cellComparisonTable = null;
    private Phylogeny phylo = null;
    /**
     * Class constructor
     * 
     * @param cellName name of cell
     * @param location location who to display CladeProperties window
     * @param cellInfoTable 
     */
    public CladeProperties(String cellName, Point location, Phylogeny phylo,CellsInformationTable cellInfoTable, CellsComparisonTable cellComparisonTable) {
        initComponents();
        this.phylo = phylo;
        this.cellInfoTable = cellInfoTable;
        this.cellComparisonTable = cellComparisonTable;
        setTitle(cellName);
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        setLocation((getWidth() / 2), (getHeight() / 2));
        setBackground(Color.white);
        setVisible(true);
    }

    public CladeProperties(CellsInformationTable cellInfoTable) {
        this.cellInfoTable = cellInfoTable;
    }

    public String getCladeName() {
        return jl_cladeName.getText();
    }

    public String getCladeBrancheLength() {
        return jl_cladeBraneLength.getText();
    }

    public int getCladeLevel() {
        return Integer.parseInt(jl_cladeLevel.getText());
    }

    public int getCladeDepth() {
        return Integer.parseInt(jl_cladeDepth.getText());
    }

    public String getCellId() {
        return jl_CellID.getText();
    }

    public void setCladeName(String name) {
        jl_cladeName.setText(name);
    }

    public void setCladeBrancheLength(double branch) {
        jl_cladeBraneLength.setText("" + branch);
    }

    public void setCladeLevel(int level) {
        jl_cladeLevel.setText("" + level);
    }

    public void setCladeDepth(int depth) {
        jl_cladeDepth.setText("" + depth);
    }

    public void setSubCladesList(String[] names) {
        if (names == null) {
            subClades.addItem("No SubClades...");
            subClades.setForeground(Color.red);
        } else {
            if (names.length == 0) {
                subClades.addItem("No SubClades");
                subClades.setForeground(Color.red);
            } else {
                for (int i = 0; i < names.length; i++) {
                    subClades.add(names[i]);
                }
            }
        }
    }

    public void setParentCladesList(String[] names) {
        if (names == null) {
            parentClades.addItem("No parentClades...");
            parentClades.setForeground(Color.red);
        } else {
            if (names.length == 0) {
                parentClades.addItem("No parentClades");
                parentClades.setForeground(Color.red);
            } else {
                for (int i = 0; i < names.length; i++) {
                    parentClades.add(names[i]);
                }
            }
        }
    }

    public java.awt.List getParentList() {
        return parentClades;
    }

    public java.awt.List getChildrenList() {
        return subClades;
    }

    public void setCellID(String id) {
        jl_CellID.setText("" + id);
    }

    public void setCellArea(double area) {
        jl_CellArea.setText("" + area);
    }

    public void setCellLength(double length) {
        jl_CellLength.setText("" + length);
    }

    public void setFluorescence(double fluorescence) {
        jl_CellFluorescence.setText("" + fluorescence);
    }

    public void setFrameElapsedTime(double frameElapsedTime) {
        jl_FrameElapsedTime.setText("" + frameElapsedTime);
    }

    public void setCellAreaUnit(String areaUnit) {
        jl_CellAreaUnit.setText("" + areaUnit);
    }

    public void setCellLengthUnit(String lengthUnit) {
        jl_CellLengthUnit.setText("" + lengthUnit);
    }

    public void setFluorescenceUnit(String fluorescenceUnit) {
        jl_CellFluorescenceUnit.setText("" + fluorescenceUnit);
    }

    public void setFrameElapsedTimeUnit(String frameElapsedTimeUnit) {
        jl_FrameElapsedTimeUnit.setText("" + frameElapsedTimeUnit);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jl_cladeName = new javax.swing.JLabel();
        jl_cladeBraneLength = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        subClades = new java.awt.List();
        jLbParentClades = new javax.swing.JLabel();
        parentClades = new java.awt.List();
        jLabel4 = new javax.swing.JLabel();
        jl_cladeDepth = new javax.swing.JLabel();
        jl_cladeLevel = new javax.swing.JLabel();
        cladeLevelLb = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jl_CellArea = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jl_CellFluorescence = new javax.swing.JLabel();
        jl_CellID = new javax.swing.JLabel();
        jl_CellLength = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jl_FrameElapsedTime = new javax.swing.JLabel();
        jl_FrameElapsedTimeUnit = new javax.swing.JLabel();
        jl_CellAreaUnit = new javax.swing.JLabel();
        jl_CellLengthUnit = new javax.swing.JLabel();
        jl_CellFluorescenceUnit = new javax.swing.JLabel();
        addCellToInfoTableBtn = new javax.swing.JButton();
        addCellToCompTableBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clade Informations:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12), new java.awt.Color(51, 51, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Clade Name:");

        jl_cladeName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_cladeName.setText("....");
        jl_cladeName.setToolTipText("");

        jl_cladeBraneLength.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_cladeBraneLength.setText("....");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Branche Length:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Descendants list:");

        jLbParentClades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLbParentClades.setText("Ancestors list:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Depth:");

        jl_cladeDepth.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_cladeDepth.setText("....");

        jl_cladeLevel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_cladeLevel.setText("....");
        jl_cladeLevel.setToolTipText("");

        cladeLevelLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cladeLevelLb.setText("Level:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(subClades, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(parentClades, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cladeLevelLb, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jl_cladeLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jl_cladeDepth, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jl_cladeName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jl_cladeBraneLength, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLbParentClades, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)))))
                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jl_cladeName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jl_cladeBraneLength))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cladeLevelLb)
                .addComponent(jl_cladeLevel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(jl_cladeDepth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(jLbParentClades))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(subClades, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(parentClades, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING))));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cell Informations:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12), new java.awt.Color(51, 51, 255))); // NOI18N

        jl_CellArea.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellArea.setText("....");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Cell ID:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Fluorescence:");

        jl_CellFluorescence.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellFluorescence.setText("....");

        jl_CellID.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellID.setText("....");

        jl_CellLength.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellLength.setText("....");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Length:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Area:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Elapsed Time:");

        jl_FrameElapsedTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_FrameElapsedTime.setText("....");

        jl_FrameElapsedTimeUnit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_FrameElapsedTimeUnit.setText("....");

        jl_CellAreaUnit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellAreaUnit.setText("....");

        jl_CellLengthUnit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellLengthUnit.setText("....");

        jl_CellFluorescenceUnit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jl_CellFluorescenceUnit.setText("....");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jl_CellFluorescence, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addComponent(jl_CellID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jl_CellLength, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jl_CellArea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jl_FrameElapsedTime, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jl_CellFluorescenceUnit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jl_CellLengthUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jl_CellAreaUnit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jl_FrameElapsedTimeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jl_CellFluorescenceUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jl_CellLengthUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jl_CellAreaUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jl_FrameElapsedTimeUnit))
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(jl_CellID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(jl_CellFluorescence))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel10)
                .addComponent(jl_CellLength))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel11)
                .addComponent(jl_CellArea))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel12)
                .addComponent(jl_FrameElapsedTime))))));

        addCellToInfoTableBtn.setText("Add to InfoTable");
        addCellToInfoTableBtn.setToolTipText("Add cell to information table");
        addCellToInfoTableBtn.setMaximumSize(new java.awt.Dimension(65, 23));
        addCellToInfoTableBtn.setMinimumSize(new java.awt.Dimension(65, 23));
        addCellToInfoTableBtn.setPreferredSize(new java.awt.Dimension(60, 23));
        addCellToInfoTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCellToInfoTableActionPerformed(evt);
            }
        });

        addCellToCompTableBtn.setText("Add to CompTable");
        addCellToCompTableBtn.setToolTipText("Add cell to comparison table");
        addCellToCompTableBtn.setMaximumSize(new java.awt.Dimension(65, 23));
        addCellToCompTableBtn.setMinimumSize(new java.awt.Dimension(65, 23));
        addCellToCompTableBtn.setPreferredSize(new java.awt.Dimension(60, 23));
        addCellToCompTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCellToComparisonTableActionPerformed(evt);
            }
        });
        
        cancelBtn.setText("Cancel");
        cancelBtn.setToolTipText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(addCellToInfoTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(13, 13, 13)
                .addComponent(addCellToCompTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(13, 13, 13))
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(addCellToInfoTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addCellToCompTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelBtn))
                .addContainerGap()));

        pack();
    }

    private void addCellToInfoTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCellToTableActionPerformed
            addCellToCellsInformationTable(jl_CellID.getText(),
                new Double(jl_CellFluorescence.getText()),
                new Double(jl_CellLength.getText()),
                new Double(jl_CellArea.getText()));

    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

     private void addCellToComparisonTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCellToTableActionPerformed
        cellComparisonTable.addCellToCellsComparisonTable(phylo.getRootClade().getCellObject().getId(),
                jl_cladeName.getText(), jl_CellID.getText());
        this.dispose();

    }
    
    /**
     * Add selected cell to table
     * 
     * @param cellId id/name of cell
     * @param cellFluorescence fluorescence value of cell
     * @param cellLength length value of cell
     * @param cellArea area value of cell
     */
    public void addCellToCellsInformationTable(String cellId, double cellFluorescence, double cellLength, double cellArea) {
    	CellInformationTableModel model = (CellInformationTableModel) cellInfoTable.getModel();
    	
    	/* Check if the cell is already in the table */
    	if (!model.contains(cellId)) {
    		model.addRow(new Object[]{cellId, cellFluorescence, cellLength, cellArea});
    	}
        
    	this.dispose();
    }
    
    // Variables declaration - do not modify
    private javax.swing.JButton addCellToInfoTableBtn;
    private javax.swing.JButton addCellToCompTableBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel cladeLevelLb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLbParentClades;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jl_CellArea;
    private javax.swing.JLabel jl_CellAreaUnit;
    private javax.swing.JLabel jl_CellFluorescence;
    private javax.swing.JLabel jl_CellFluorescenceUnit;
    private javax.swing.JLabel jl_CellID;
    private javax.swing.JLabel jl_CellLength;
    private javax.swing.JLabel jl_CellLengthUnit;
    private javax.swing.JLabel jl_FrameElapsedTime;
    private javax.swing.JLabel jl_FrameElapsedTimeUnit;
    private javax.swing.JLabel jl_cladeBraneLength;
    private javax.swing.JLabel jl_cladeDepth;
    private javax.swing.JLabel jl_cladeLevel;
    private javax.swing.JLabel jl_cladeName;
    private java.awt.List parentClades;
    private java.awt.List subClades;
    // End of variables declaration
}
