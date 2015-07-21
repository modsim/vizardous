/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.graphics.export;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.DOMImplementation;
import vizardous.delegate.impl.fileFilter.JPEGFileFilter;
import vizardous.delegate.impl.fileFilter.PDFFileFilter;
import vizardous.delegate.impl.fileFilter.PNGFileFilter;
import vizardous.delegate.impl.fileFilter.SVGFileFilter;
import vizardous.delegate.impl.graphics.AbstractChart2D;
import vizardous.delegate.impl.graphics.TraceChart2D;

/**
 * Export functionality for charts.
 * 
 * Methods are called via menu entries for exporting charts.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class GraphicsExporter {
     
	/**
	 * TODO Documentation
	 * 
	 * @param chart
	 * @param chartArt
	 */
     public static void exportChart2D(AbstractChart2D chart, String chartArt) {
        JFileChooser myChooser = new JFileChooser();
        myChooser.setCurrentDirectory(myChooser.getFileSystemView().getHomeDirectory());
        myChooser.setAcceptAllFileFilterUsed(false);
        myChooser.addChoosableFileFilter(new PNGFileFilter());
        myChooser.addChoosableFileFilter(new SVGFileFilter());
        myChooser.addChoosableFileFilter(new JPEGFileFilter());
        myChooser.addChoosableFileFilter(new PDFFileFilter());
        
        if(chart != null) {
        int option = myChooser.showSaveDialog(chart);
        if(option == JFileChooser.APPROVE_OPTION) {
            if(myChooser.getSelectedFile() != null) {
            String filePath = myChooser.getSelectedFile().getPath();
            String filterDescription = myChooser.getFileFilter().getDescription();//getChoosableFileFilters();

            /* Remove wrong file extension */
     	   if (!FilenameUtils.isExtension(filePath, "pdf")) {
     		   filePath = FilenameUtils.removeExtension(filePath);
     	   }
            
            // export graphic in png format
            if(filterDescription.equals("Portable Network Graphics (*.png)")) {
            	/* Add correct extension (.png) */
          	   if (FilenameUtils.getExtension(filePath).equals("")) {
          		  filePath = filePath + ".png";
          	   }
          	   
                BufferedImage bi = new BufferedImage((int) chart.getBounds().getWidth(), 
                        (int) chart.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB); 
                Graphics g = bi.createGraphics();
                chart.addNotify();
                chart.setVisible(true);
                chart.validate();
                chart.paint(g);  //this == JComponent
                g.dispose();
                try {
                    ImageIO.write(bi,"png",new File(filePath));
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
            // export graphic in svg format
            else if (filterDescription.equals("Scalable Vector Graphics (*.svg)")) {
            	/* Add correct extension (.svg) */
          	   if (FilenameUtils.getExtension(filePath).equals("")) {
          		  filePath = filePath + ".svg";
          	   }
          	   
                if(chartArt.equals("line"))  {
                    TraceChart2D oo = (TraceChart2D) chart;
                    oo.saveSVG(myChooser.getSelectedFile());
                   
                }
                else if( chartArt.equals("histogram") ) 
                {
                    //TODO export of distribution charts
                    
//                    /* Get a DOMImplementation and create an XML document */
//                    DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
//                    Document document = domImpl.createDocument(null, "svg", null);
//
//                    /* Draw the chart in the SVG generator */
//                    SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
//                    chart.paint(svgGenerator);
//
//                    /* Write SVG file */  
//                            try {
//                                    OutputStream outputStream = new FileOutputStream(new File(filePath));
//                                    Writer out = new OutputStreamWriter(outputStream, "UTF-8");
//                                    svgGenerator.stream(out, true /* use css */);						
//                                    outputStream.flush();
//                                    outputStream.close();
//                            } catch (FileNotFoundException e) {
//                                    // TODO Logging
//                                    e.printStackTrace();
//                            } catch (UnsupportedEncodingException e) {
//                                    // TODO Logging
//                                    e.printStackTrace();
//                            } catch (IOException e) {
//                                    // TODO Logging
//                                    e.printStackTrace();
//                            } 
                }
            }

            // export graphic in jpeg format
            else if (filterDescription.equals("Joint Photographic Experts Group Format (*.jpeg)")) {
            	/* Add correct extension (.jpeg) */
         	   if (FilenameUtils.getExtension(filePath).equals("")) {
         		  filePath = filePath + ".jpeg";
         	   }
            	
                BufferedImage expImage = new BufferedImage(chart.getWidth(),
                chart.getHeight(),BufferedImage.TYPE_INT_RGB);
                /*
                 * Print to Image, scaling if necessary.
                 */
                Graphics2D g2 = expImage.createGraphics();
                chart.addNotify();
                chart.setVisible(true);
                chart.validate();   
                chart.paint(g2);
                /*
                 * Write to File
                 */
                try {
                    OutputStream out = new FileOutputStream(filePath);
                    ImageIO.write(expImage, "jpeg", out);
                    out.close();
                } catch (Exception ex) {
                         System.err.println(ex);
                }
            }
           else if(filterDescription.equals("Portable Document Format (*.pdf)")) {        	   
        	   /* Add correct extension (.pdf) */
        	   if (FilenameUtils.getExtension(filePath).equals("")) {
        		   filePath = filePath + ".pdf";
        	   }
        	          	   
        	   com.lowagie.text.Document document = new com.lowagie.text.Document(new Rectangle(0f, 0f, (float) chart.getWidth(), (float) chart.getHeight()),0,0,0,0);
               try {
                      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
                      document.open();
                      PdfContentByte contentByte = writer.getDirectContent();
                      Graphics2D g2 = contentByte.createGraphics(chart.getWidth(), chart.getHeight());
                      
                      chart.getChart().draw(g2, new java.awt.Rectangle(chart.getWidth(), chart.getHeight()));
                      
                      g2.dispose();
                  } catch (Exception ex) {
                      ex.printStackTrace();
                      System.err.println(ex);
                  }
                  finally {
                   document.close();
                 }
            }//if-PDF
          }//if-SelectFile
         }//iF-ApproveOpt.
    }
     }
     
	/**
	 * 
	 * @param treePanel
	 */
    public static void exportLineageTree(JScrollPane treePanel) {
        JFileChooser chooserTreeExp = new JFileChooser();
        chooserTreeExp.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooserTreeExp.setMultiSelectionEnabled(false);
        chooserTreeExp.setAcceptAllFileFilterUsed(false);
        chooserTreeExp.addChoosableFileFilter(new PNGFileFilter());
        chooserTreeExp.addChoosableFileFilter(new SVGFileFilter());
        chooserTreeExp.addChoosableFileFilter(new JPEGFileFilter());
        chooserTreeExp.addChoosableFileFilter(new PDFFileFilter());
        int option = chooserTreeExp.showSaveDialog(treePanel);
        if(option == JFileChooser.APPROVE_OPTION) {
            if(chooserTreeExp.getSelectedFile() != null) {
            String filePath = chooserTreeExp.getSelectedFile().getPath();
            String filterDescription = chooserTreeExp.getFileFilter().getDescription();//getChoosableFileFilters();
            BufferedImage bi = new BufferedImage((int) treePanel.getBounds().getWidth(), 
                        (int) treePanel.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB); 
                Graphics g = bi.createGraphics();

            // export graphic in png format
            if(filterDescription.equals("Portable Network Graphics (*.png)")) {
                filePath = filePath + ".png";
                treePanel.paint(g);  
                g.dispose();
                try {
                    ImageIO.write(bi,"png",new File(filePath));
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
            // export graphic in svg format
            else if (filterDescription.equals("Scalable Vector Graphics (*.svg)")) {
                filePath = filePath + ".svg";
                //----------------------
                DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
                org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);
                SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
                // TODO Think what's the best to take: the size of viewing panel or the size og drawing tree
                svgGenerator.setSVGCanvasSize(new Dimension(treePanel.getWidth(), treePanel.getHeight()));
                boolean useCSS=true;
                try {
                        FileOutputStream os = new FileOutputStream(new File(filePath));
                        Writer out = new OutputStreamWriter(os, "UTF-8");
                        treePanel.paint(svgGenerator);
                        svgGenerator.stream(out, useCSS);
                        os.flush();
                        os.close();
                } catch (Exception ex) {
                        System.err.println(ex);
                }
            }
            // export graphic in jpeg format
            else if (filterDescription.equals("Portable Document Format (*.jpeg)")) {
                filePath = filePath + ".jpeg";
                BufferedImage expImage = new BufferedImage(treePanel.getWidth(),
                treePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
                /*
                 * Print to Image, scaling if necessary.
                 */
                Graphics2D g2 = expImage.createGraphics();
                treePanel.paint(g2);
                /*
                 * Write to File
                 */
                try {
                    OutputStream out = new FileOutputStream(filePath);
                    ImageIO.write(expImage, "jpeg", out);
                    out.close();
                } catch (Exception ex) {
                         System.err.println(ex);
                }
            }
            // export graphic in pdf format
            else if(filterDescription.equals("Portable Document Format (*.pdf)")) {
                filePath = filePath + ".pdf";
                com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A3.rotate(),0, 0, 0, 0);
               try {
                      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
                      document.open();
                      PdfContentByte contentByte = writer.getDirectContent();
                      PdfTemplate template = contentByte.createTemplate(
                              treePanel.getWidth(), 
                              treePanel.getHeight());
                      Graphics2D g2 = template.createGraphics( 
                              treePanel.getWidth(), 
                              treePanel.getHeight());
                      treePanel.print(g2);
                      g2.dispose();
                      contentByte.addTemplate(template, 0, 0);
                  } catch (Exception ex) {
                      System.err.println(ex);
                  }
                  finally {
                    if(document.isOpen()) {
                        document.close();
                    }
                 }
            }//if-PDF
          }//if-SelectFile
         }//iF-ApproveOpt.
      }
}
