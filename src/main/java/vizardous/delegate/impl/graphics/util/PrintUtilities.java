/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob; 
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vizardous.delegate.impl.DrawPhyloTreeGraph;
import javax.swing.RepaintManager;
        
/**
 * The printing of tree is implemented in this class. 
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 */

public class PrintUtilities implements Printable {        
    private Component compToBePrinted;

    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(DrawPhyloTreeGraph.class);
    
    public static void printComponent(Component c) {     
        new PrintUtilities(c).print();   
    }     
    
    public PrintUtilities(Component componentToBePrinted) {      
    this.compToBePrinted = componentToBePrinted;
     Locale loc = Locale.ENGLISH;
    }    
    
    /**
     * 
     */
    public void print() {              
        PrinterJob printJob = PrinterJob.getPrinterJob();            
        PageFormat pageFormat = printJob.defaultPage();  
        //new PageFormat();      
//        pageFormat.setOrientation( PageFormat.LANDSCAPE );   
        //Längs- oder Querformat (Standard: längs)            
        Paper a4PortraitPaper = new Paper();      
        final double cm2inch = 0.3937;  // 1in = 2.54cm      
        double paperHeight = 29.7 * cm2inch;      
        double paperWidth = 21.0 * cm2inch;      
        double margin = 1.5 * cm2inch;             
        a4PortraitPaper.setSize( paperWidth * 72.0, paperHeight * 72.0 );      
        a4PortraitPaper.setImageableArea( margin * 72.0, margin * 72.0, 
                ( paperWidth - 2 * margin ) * 72.0,                          
                ( paperHeight - 2 * margin ) * 72.0 );       
        pageFormat.setPaper( a4PortraitPaper );            
        printJob.setPrintable( this, pageFormat );            
        if (printJob.printDialog()) {       
            try {          
                printJob.print();        

            } catch(PrinterException ex) {          

                logger.debug("Error printing: " + ex );          
                logger.debug("Error printing (Message): " + ex.getMessage() );          
                logger.debug("Error printing (Localized Message): " + ex.getLocalizedMessage() );          
                logger.debug("Error printing (Cause): " + ex.getCause() );
            }            
        }         
    }
    
    /**
     * 
     * @param g
     * @param pageFormat
     * @param pageIndex
     * @return 
     */
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {                
        double gBreite, gHoehe;        
        int b, h;        
        double skalierung = 0.0;          
        if ( pageIndex > 0 ) {        
            return( NO_SUCH_PAGE );      
        } else {                  
            //Skalierung*******************************          
            gBreite = pageFormat.getImageableWidth();          
            gHoehe = pageFormat.getImageableHeight();         
            b = compToBePrinted.getWidth();          
            h = compToBePrinted.getHeight();                    
            skalierung = gBreite / b;                    
            System.out.println(gBreite +" " + b + " " + gHoehe + " " + h + " " + skalierung);        
            //Ende Skalierung***************************                  
    
            Graphics2D g2d = (Graphics2D)g;        
            g2d.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );                
            g2d.scale( skalierung, skalierung );                
            disableDoubleBuffering( compToBePrinted );        
            compToBePrinted.paint( g2d );       
            enableDoubleBuffering( compToBePrinted );        
            return( PAGE_EXISTS );      
        }
    }         
    
    /**
     * 
     * @param c 
     */
    public static void disableDoubleBuffering(Component c) {     
        RepaintManager currentManager = RepaintManager.currentManager( c );      
        currentManager.setDoubleBufferingEnabled( false );    }         
    
    /**
     * 
     * @param c 
     */
    public static void enableDoubleBuffering(Component c) {      
        RepaintManager currentManager = RepaintManager.currentManager( c );      
        currentManager.setDoubleBufferingEnabled( true );    
    }  
    
}