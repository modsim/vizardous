/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vizardous.delegate.impl.fileFilter.XMLFileFilter;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MyFileChooser extends javax.swing.JFrame {

	/** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(MyFileChooser.class);
	
    private MainView mainWind = null;
    private File phyXMLFile = null;
    private File metaXMLFile = null; 
    
    /**
     * Creates new form MyFileChooser
     */
    public MyFileChooser(MainView mainWind) { 
        Locale.setDefault(Locale.US);
        this.mainWind = mainWind;
        initComponents();
        jl_seletedFile5.setVisible(false);
        jl_seletedFile6.setVisible(false); 
        setVisible(true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtFileName1 = new javax.swing.JTextField();
        txtFileName2 = new javax.swing.JTextField();
        jl_seletedFile5 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOpenFiles = new javax.swing.JButton();
        btnBrowse1 = new javax.swing.JButton();
        btnBrowse2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jl_seletedFile6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Open");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("FilesImportFrame"); // NOI18N
        setResizable(false);

        jLabel6.setText("PhyloXML File");

        jLabel7.setText("MetaML File");

        txtFileName1.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jl_seletedFile5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons16x16/Apply.png"))); // NOI18N
        jl_seletedFile5.setText("OK");

        btnCancel.setLabel("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(67, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(67, 23));
        btnCancel.setOpaque(false);
        btnCancel.setPreferredSize(new java.awt.Dimension(67, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOpenFiles.setText("Open Files");
        btnOpenFiles.setMaximumSize(new java.awt.Dimension(67, 23));
        btnOpenFiles.setMinimumSize(new java.awt.Dimension(67, 23));
        btnOpenFiles.setPreferredSize(new java.awt.Dimension(67, 23));
        btnOpenFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenFilesActionPerformed(evt);
            }
        });

        btnBrowse1.setText("Browse");
        btnBrowse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowse1ActionPerformed(evt);
            }
        });

        btnBrowse2.setText("Browse");
        btnBrowse2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowse2ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/file_open_48x48.png"))); // NOI18N

        jl_seletedFile6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons16x16/Apply.png"))); // NOI18N
        jl_seletedFile6.setText("OK");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtFileName2, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                                    .addComponent(txtFileName1)))
                            .addComponent(jLabel1))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnOpenFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnBrowse2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBrowse1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_seletedFile6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jl_seletedFile5))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse1)
                    .addComponent(jl_seletedFile5))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse2)
                    .addComponent(jl_seletedFile6))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnOpenFiles, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowse1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowse1ActionPerformed
        JFileChooser openFileChooser1 = new JFileChooser();
        openFileChooser1.setCurrentDirectory(openFileChooser1.getFileSystemView()
                        .getParentDirectory(new File(System.getProperty("user.home")))); 
        openFileChooser1.setMultiSelectionEnabled(false);
        openFileChooser1.addChoosableFileFilter(new XMLFileFilter());
        openFileChooser1.addChoosableFileFilter(openFileChooser1.getAcceptAllFileFilter());

        int ret = openFileChooser1.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = openFileChooser1.getSelectedFile();
            File f1 = new File(file.getPath());
            if( ( file != null ) && ( file.length() > 0 ) && ( ret == JFileChooser.APPROVE_OPTION )) {
                txtFileName1.setText(f1.getName());
                jl_seletedFile5.setVisible(true);
                phyXMLFile = file;
            } else {
                 throw new IllegalArgumentException("PhyloXML is Null.");
             }
            
          } else if (ret == JFileChooser.CANCEL_OPTION) {
                logger.debug("JFileChooser.CANCEL_OPTION");
          }
    }//GEN-LAST:event_btnBrowse1ActionPerformed
   
    private void btnBrowse2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowse2ActionPerformed
        JFileChooser openFileChooser2 = new JFileChooser();
        openFileChooser2.setCurrentDirectory(openFileChooser2.getFileSystemView()
                        .getParentDirectory(new File(System.getProperty("user.home"))));
        openFileChooser2.setMultiSelectionEnabled(false);
        openFileChooser2.addChoosableFileFilter(new XMLFileFilter());
        openFileChooser2.addChoosableFileFilter(openFileChooser2.getAcceptAllFileFilter());
        
        int ret = openFileChooser2.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File mFile = openFileChooser2.getSelectedFile();
            File f2 = new File(mFile.getPath());
             if( ( mFile != null ) && ( mFile.length() > 0 ) && ( ret == JFileChooser.APPROVE_OPTION )) {
                txtFileName2.setText(f2.getName());
                jl_seletedFile6.setVisible(true);
                metaXMLFile = mFile;
            } else 
                 throw new IllegalArgumentException("MetaXML is Null.");
             
          } else if (ret == JFileChooser.CANCEL_OPTION) 
              dispose();
    }//GEN-LAST:event_btnBrowse2ActionPerformed

    private void btnOpenFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenFilesActionPerformed
        mainWind.openFiles(phyXMLFile, metaXMLFile);
        this.dispose();
    }//GEN-LAST:event_btnOpenFilesActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse1;
    private javax.swing.JButton btnBrowse2;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOpenFiles;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel jl_seletedFile5;
    private javax.swing.JLabel jl_seletedFile6;
    private javax.swing.JTextField txtFileName1;
    private javax.swing.JTextField txtFileName2;
    // End of variables declaration//GEN-END:variables
}

