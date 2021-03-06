/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.table;

import javax.swing.table.TableModel;

import vizardous.delegate.analysis.PhyloTreeAnalyser;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
public class MetadataTable extends javax.swing.JTable {
    
    private PhyloTreeAnalyser newJInterFrame = null;
    
    public MetadataTable(PhyloTreeAnalyser newJInterFrame) {
        this.newJInterFrame = newJInterFrame;
        TableModel myTabModel1 = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Frame Id", "ElapsedTime", "Path", "Cell Id", "Fluorescence YFP", "Fluorescence CRIMSON", "Length", "Area"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, 
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, 
                java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        this.setModel(myTabModel1);
    }  
}
