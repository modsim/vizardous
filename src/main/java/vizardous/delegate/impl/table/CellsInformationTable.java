/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.table;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import vizardous.delegate.impl.analysis.PhyloTreeAnalyser;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
public class CellsInformationTable extends javax.swing.JTable {
    
    private PhyloTreeAnalyser newJInterFrame = null;

    public CellsInformationTable(PhyloTreeAnalyser newJInterFrame) {
        this.newJInterFrame = newJInterFrame;
        this.setModel(new CellInformationTableModel());
        addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            tableForCellInfoKeyReleased(evt);
        }
    });
        
    }
  
    private void tableForCellInfoKeyReleased(java.awt.event.KeyEvent evt) {                                             
        if(evt.getKeyCode()== KeyEvent.VK_DELETE){
            if(this.getModel().getRowCount()== 0) {
            JOptionPane.showMessageDialog(this,
                "The table is empty.",
                "Message",
                JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_information32x32.png")));

        } else if( this.getModel().getRowCount()> 0 ) {
                if(this.getSelectedRows().length == 0) {
                    JOptionPane.showMessageDialog(this,
                    "You have to select at first one Cell or multiple!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/icons/alert_3_32x32.png")));

                } else {
                    int n = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to delete the selected Cell?" + "\n They appendant charts will be also deleted.",
                    "Question",
                        JOptionPane.YES_NO_OPTION, 0, new javax.swing.ImageIcon(getClass().getResource("/icons/symbol_help32x32.png")));
                    if(n == JOptionPane.YES_OPTION) {
                        newJInterFrame.deleteSelectedCells(this);
                    } 
                }
            } 
        }
    } 
}
