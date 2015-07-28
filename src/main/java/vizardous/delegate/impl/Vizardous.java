package vizardous.delegate.impl;

import java.io.File;
import java.util.Locale;

import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * This class acts as an entry point for starting a Vizardous instance. Hence,
 * it either initializes a user interface or parses the commandline arguments
 * for processing in headless mode.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class Vizardous {
	
	public MainView view;
	
	@Parameter(names = {"-p", "--phyloxml"}, description = "use given PhyloXML", converter = FileConverter.class)
	private static File phyloXml;
	
	@Parameter(names = {"-m", "--metaxml"}, description = "use given MetaXML", converter = FileConverter.class)
	private static File metaXml;
	
	@Parameter(names = {"-o", "--output"}, description = "Output file", converter = FileConverter.class, hidden = true)
	private static File output;
	
	@Parameter(names = "--help", help = true)
	private static boolean help;
	
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         
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
            LoggerFactory.getLogger(MainView.class).error(null, ex);
        } catch (InstantiationException ex) {
        	LoggerFactory.getLogger(MainView.class).error(null, ex);
        } catch (IllegalAccessException ex) {
        	LoggerFactory.getLogger(MainView.class).error(null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        	LoggerFactory.getLogger(MainView.class).error(null, ex);
        }
        //</editor-fold>

        final Vizardous vizardousInstance = new Vizardous();
        
        /* Show help if no commandline parameters are passed */
        if (args.length != 0) {
        	JCommander jc = new JCommander(vizardousInstance, args);
            jc.setProgramName("vizardous");
            
            if (help) {
            	jc.usage();
            }
                
            /* Check if an output file is provided */
            if (Vizardous.phyloXml != null && Vizardous.metaXml != null && Vizardous.output == null) {
            	Locale.setDefault(Locale.US);
                vizardousInstance.view = new MainView();
                
                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        vizardousInstance.view.centerFrame();
                        vizardousInstance.view.setVisible(true);
                    }
                });            	
            	
            	vizardousInstance.view.openFiles(Vizardous.phyloXml, Vizardous.metaXml);
            } else {
            	// If provided: execute computation and write to file
            	/* Do the computation */
                // TODO
                
                /* Write results to output file */
                // TODO
            }
        } else {
        	Locale.setDefault(Locale.US);
            vizardousInstance.view = new MainView();
            
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                
                @Override
                public void run() {
                    vizardousInstance.view.centerFrame();
                    vizardousInstance.view.setVisible(true);
                }
            });
        }
    }
}
