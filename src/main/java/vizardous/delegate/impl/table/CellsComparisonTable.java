/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.table;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import vizardous.delegate.impl.analysis.PhyloTreeAnalyser;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
public class CellsComparisonTable extends javax.swing.JTable {
    
    private PhyloTreeAnalyser newJInterFrame = null;
    private TableModel comparisonTabModel = null;

    public CellsComparisonTable() {
        comparisonTabModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Root Id", "Clade name", "Cell Id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        this.setModel(comparisonTabModel);
    }
  
     public void addCellToCellsComparisonTable(String phylogenyRootId, String cladeName, String cellId) {
        //TODO Avoid the add in two times the same cell in table
        // Allowed is <t first to compare two cells
         if (((DefaultTableModel) this.getModel()).getRowCount() > 1) {
                for(int i = 0; i < ((DefaultTableModel) this.getModel()).getRowCount(); i++) {
                    ((DefaultTableModel)this.getModel()).removeRow(i);
                }
                ((DefaultTableModel) this.getModel()).
                            addRow(new Object[]{phylogenyRootId, cladeName, cellId});
                
        } else {
            ((DefaultTableModel) this.getModel()).
                            addRow(new Object[]{phylogenyRootId, cladeName, cellId});
                            
        }
    }
//    
     public TableModel getCellsCompTableModel() {
        return comparisonTabModel;
    } 
}
