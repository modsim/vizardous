package vizardous.delegate.impl;

import java.io.File;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;

/**
 * This class acts as an entry point for starting a Vizardous instance. Hence,
 * it either initializes a user interface or parses the commandline arguments
 * for processing in headless mode.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class Vizardous {
	
	public MainView view;
	
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
        	// create Options object
            Options options = new Options();

            Option phyloxml = Option.builder("phyloxml")
            		.required(true)
            		.argName("file")
                    .hasArg()
                    .desc("use given PhyloXML")
                    .build();
            options.addOption(phyloxml);
            
            Option metaxml = Option.builder("metaxml")
            		.required(true)
            		.argName("file")
                    .hasArg()
                    .desc("use given MetaXML")
                    .build();
            options.addOption(metaxml);
            
            Option output = Option.builder("output")
            		.required(false)
            		.argName("file")
                    .hasArg()
                    .desc("Output file")
                    .build();
            options.addOption(output);
            
            Option help = new Option("help", "print this message");
            options.addOption(help);
            
            // Create the commandline parser
            CommandLineParser parser = new DefaultParser();
            try {
                // parse the command line arguments
                CommandLine line = parser.parse(options, args, false);
                
                // Returns the left-over commandline arguments
                line.getArgs();
                
                File f1 = null;
                File f2 = null;
                File f3 = null;
                
                // has the phyloxml argument been passed?
                if (line.hasOption("phyloxml")) {
                    // initialise the member variable
                    f1 = new File(line.getOptionValue("phyloxml"));
                }
                
                // has the metaxml argument been passed?
                if (line.hasOption("metaxml")) {
                    // initialise the member variable
                    f2 = new File(line.getOptionValue("metaxml"));
                }
                
                // has the output argument been passed?
                if (line.hasOption("output")) {
                    // initialise the member variable
                    f3 = new File(line.getOptionValue("output"));
                }
                
                /* Show help */
                if (line.hasOption("help")) {
                	HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("Vizardous", options, true);
                }
                
                /* Check if an output file is provided */
                if (line.hasOption("phyloxml") && line.hasOption("metaxml") && !line.hasOption("output")) {
                	// If not: open the GUI
                	vizardousInstance.view.openFiles(f1, f2);
                } else {
                	// If provided: execute computation and write to file
                	/* Do the computation */
                    // TODO
                    
                    /* Write results to output file */
                    // TODO
                }
            } catch(ParseException exp) {
                LoggerFactory.getLogger(Vizardous.class).error("Parsing failed.", exp);
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
