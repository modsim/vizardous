/*//GEN-FIRST:event_connectionButtonActionPerformed
 * To change this template, choose Tools | Templates//GEN-LAST:event_connectionButtonActionPerformed
 * and open the template in the editor.
 */
package vizardous.omeroserver.gui;

/**
 * This class implements a graphical user interface for the administration of 
 * credentials. 
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0.0
 */

public class Login extends javax.swing.JDialog {

    /**
     * Creates new form Login
     */
    public Login(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        credentialsSettingsWindow = new javax.swing.JPanel();
        hostName = new javax.swing.JLabel();
        hostAdressTxtFeld = new javax.swing.JTextField();
        userName = new javax.swing.JLabel();
        jtxtUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        portTxtFeld = new javax.swing.JTextField();
        jtxtPassword = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        connectionButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.getRootPane().setDefaultButton(connectionButton);
        
        hostName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hostName.setText("Host Name:");

        hostAdressTxtFeld.setText("ibt-v705");

        userName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userName.setText("User Name:");
        
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Password:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Port:");

        portTxtFeld.setText("4064");

        connectionButton.setText("Connect");
        connectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Enter the information to log on the server");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons16x16/Network connectionUnknowSize.png"))); // NOI18N

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout credentialsSettingsWindowLayout = new javax.swing.GroupLayout(credentialsSettingsWindow);
        credentialsSettingsWindow.setLayout(credentialsSettingsWindowLayout);
        credentialsSettingsWindowLayout.setHorizontalGroup(
            credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                        .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(connectionButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelButton))
                            .addComponent(jSeparator2)
                            .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(hostName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                                        .addComponent(jtxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(hostAdressTxtFeld, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtxtPassword)
                                    .addComponent(portTxtFeld, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(21, 21, 21))))
        );
        credentialsSettingsWindowLayout.setVerticalGroup(
            credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                        .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(portTxtFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jtxtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(credentialsSettingsWindowLayout.createSequentialGroup()
                        .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hostName)
                            .addComponent(hostAdressTxtFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(userName)
                            .addComponent(jtxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(credentialsSettingsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectionButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(credentialsSettingsWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(credentialsSettingsWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        
        pack();
        
        // Set initial focus on the user name
        jtxtUsername.requestFocusInWindow();
    }// </editor-fold>                        

    private void connectionButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        dispose();
    }                                                

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        dispose();
    }                                            

    /**
     * Gets the host name.
     * 
     * @return Returns the name of the host.
     */
    public String getHostname() {
        return hostAdressTxtFeld.getText();
    }

    /**
     * Sets the host name.
     * 
     * @param host Host name to set.
     */
    public void setHostAdressTxtFeld(String host) {
        hostAdressTxtFeld.setText(host);
    }

   /**
     * Gets the port number.
     * 
     * @return Returns the number of the port or -1.
     */
    public int getPort() {
        int rtn;

        try {
                rtn = Integer.parseInt(portTxtFeld.getText());
        } catch (Exception e) {
                return -1;
        }

	return rtn;
    }

    /**
     * Sets the port number.
     * 
     * @param port Port number to set.
     */
    public void setPort(int port) {
        portTxtFeld.setText(Integer.toString(port));
    }
    
    /**
     * Gets the password.
     * 
     * @return Returns the password.
     */
    public String getPassword() {
        return jtxtPassword.getText();
    }

    /**
     * Sets the password.
     * 
     * @param password The password to set.
     */
    public void setPassword(String password) {
            jtxtPassword.setText(password);
    }

    /**
     * Gets the user name.
     * 
     * @return Returns the name of the user.
     */
    public String getUsername() {
            return jtxtUsername.getText();
    }

    /**
     * Sets the port number.
     * 
     * @param username The user name to set.
     */
    public void setBenutzername(String username) {
            jtxtUsername.setText(username);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login dialog = new Login(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
   
    // Variables declaration - do not modify                     
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton connectionButton;
    private javax.swing.JPanel credentialsSettingsWindow;
    private javax.swing.JTextField hostAdressTxtFeld;
    private javax.swing.JLabel hostName;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPasswordField jtxtPassword;
    private javax.swing.JTextField jtxtUsername;
    private javax.swing.JTextField portTxtFeld;
    private javax.swing.JLabel userName;
    // End of variables declaration                   
}
