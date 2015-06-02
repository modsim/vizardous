/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import vizardous.delegate.impl.graphics.util.PrintUtilities;
import vizardous.delegate.impl.analysis.PhyloTreesDepthAnalysis;

import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.mxgraph.swing.mxGraphComponent;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ScrollPaneConstants;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

import vizardous.delegate.impl.fileFilter.JPEGFileFilter;
import vizardous.delegate.impl.fileFilter.PDFFileFilter;
import vizardous.delegate.impl.fileFilter.PNGFileFilter;
import vizardous.delegate.impl.fileFilter.SVGFileFilter;
import vizardous.delegate.impl.table.CellsInformationTable;
import vizardous.delegate.impl.treeSort.sorter.SortParam;
import vizardous.delegate.impl.treeSort.sorter.SortType;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Clade;
import vizardous.model.impl.Constants;
import vizardous.model.impl.Forest;
import vizardous.model.impl.Phylogeny;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */

public class MyPopUpMenu extends JPopupMenu {

    private MouseEvent              e               = null;
//    private VisualizationViewer     treePanel       = null;
    private mxGraphComponent		graphComponent	= null;
    private Forest                  forest          = null; 
    private Phylogeny               phylo           = null; 
    private CladeProperties         cp              = null;
    private CellsInformationTable   cellInfoTable   = null;
    private Locale                  loc             = Locale.ENGLISH;
    private double                  defaultValueFluorescence = 0;

    /**
	 * Button group for different sort options, so that the user can see which
	 * sort is currently applied
	 */
    private ButtonGroup				sortGroup 		= new ButtonGroup();
    private ButtonGroup				layoutGroup		= new ButtonGroup();
    
    /**
     * 
     * @param panel 
     */
    public MyPopUpMenu(Forest forest, Phylogeny phylo, mxGraphComponent graphComponent, CellsInformationTable cellInfoTable) {
    	this.graphComponent		= graphComponent;
        this.forest             = forest;
        this.cellInfoTable      = cellInfoTable;
        cp                      = new CladeProperties(cellInfoTable);
    }    
    
    /**
     * 
     * @param mouseEvent
     * @param panel 
     */
    public MyPopUpMenu(/*MouseEvent mouseEvent,*/ Forest forest, final Phylogeny phylo, final mxGraphComponent graphComponent, final DrawPhyloTreeGraph drawer, CellsInformationTable cellInfoTable) {
//        this.e              = mouseEvent;
        this.graphComponent = graphComponent;
        this.forest         = forest;
        this.phylo          = phylo;
        this.cellInfoTable  = cellInfoTable;
        cp                  = new CladeProperties(cellInfoTable);
        
        //exportMenuItem
/*        JMenu mouseModeMenu = new JMenu("Select mouse mode ");
        mouseModeMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons16x16/switchIcon16x16.png")));
        final JRadioButtonMenuItem pickingMode = new JRadioButtonMenuItem("PICKING");
        final JRadioButtonMenuItem tranformingMode = new JRadioButtonMenuItem("TRANSFORMING");
        
        // Add JRadioButtonMenuItem to Menu 
        mouseModeMenu.add(tranformingMode); 
        mouseModeMenu.add(pickingMode);
        
        // Create actionListener for the both JRadioButtonMenuItem
        pickingMode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                DefaultModalGraphMouse gm = new DefaultModalGraphMouse<Cell, Clade>();
                gm.setMode(ModalGraphMouse.Mode.PICKING);
                panel.setGraphMouse(gm);
                panel.repaint();
            }
        });
        tranformingMode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                DefaultModalGraphMouse gm = new DefaultModalGraphMouse<Cell, Clade>();
                gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
                panel.setGraphMouse(gm);
                panel.repaint();
            }
        });*/
        
        
        JMenuItem enableScrollbarsMenuItem = new JMenuItem(((graphComponent.getVerticalScrollBarPolicy() == ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER) ? "Show" : "Hide") + " scrollbars");
        enableScrollbarsMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphComponent.getVerticalScrollBarPolicy() == ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER) {
					graphComponent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					graphComponent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				}
				else {
					graphComponent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					graphComponent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
				}
			}
		});

        //exportMenuItem
        JMenuItem exportMenuItem = new JMenuItem("Export phyloTree as...", 
                new javax.swing.ImageIcon(getClass().getResource("/icons16x16/treeChart16x16.png")));
        exportMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser.setDefaultLocale(loc);
                exportPhylogeneticTree();
           }
        });
        
        // Select all leaves in the tree
        JMenuItem addAllLeavesToTableMenuItem = new JMenuItem("Select leaves", 
                new javax.swing.ImageIcon(getClass().getResource("/icons16x16/shapes_many_select.png")));
        addAllLeavesToTableMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser.setDefaultLocale(loc);
                addAllLeavesToTable(phylo);
           }
        });
        
        //ShowTreeInSeperateWindowMenuItem
        JMenuItem showTreeSeperateMenuItem = new JMenuItem("Show tree in seperate Window");//, new javax.swing.ImageIcon(getClass().getResource("/icons16x16/cellLengthDistChart.png")));
        showTreeSeperateMenuItem.setEnabled(true);
        showTreeSeperateMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser.setDefaultLocale(loc);
                showTreeInSeperateWindow();
           }
        });
        
        //levelInfosMenuItem
        JMenuItem levelInfosMenuItem = new JMenuItem("Get info about level", 
                new javax.swing.ImageIcon(getClass().getResource("/icons16x16/cellLengthDistChart.png")));
        levelInfosMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                showInfosAboutLevels(phylo);
           }

            
        });
        
        //printMenuItem
       JMenuItem printMenuItem = new JMenuItem("Print...", 
               new javax.swing.ImageIcon(getClass().getResource("/icons16x16/print_printer16x16.png")));
       printMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent event) {
               PrintUtilities printPhyloTree = new PrintUtilities(graphComponent);
               printPhyloTree.print();
           }
       });
       
       //treeSortMenuItem
       JMenu treeSortMenu = new JMenu("Tree sort");
       
       JMenu treeSortLeafMenu = new JMenu("Leaf");
       treeSortLeafMenu.setToolTipText(SortType.LEAF.getTooltip());
       
       JMenu treeSortBranchMenu = new JMenu("Branch");
       treeSortBranchMenu.setToolTipText(SortType.BRANCH.getTooltip());
       
       JMenu treeSortArithmeticalMenu = new JMenu("Arithmetical average");
       treeSortArithmeticalMenu.setToolTipText(SortType.ARITHMETICAL_AVERAGE.getTooltip());

       // All options for leaf sorting
       for(final SortParam sortOption : SortParam.values())
       {
    	   // all those successor based sorting methods make no sense if just the leaves are considered (just returns 1 and id sorting is applied)
    	   if(sortOption == SortParam.NUM_CHILDREN || sortOption == SortParam.OFFSPRING  || sortOption == SortParam.DEPTH)
    		   continue;
    	   
    	   if((sortOption == SortParam.AREA && !forest.containsAreaData()) || (sortOption == SortParam.LENGTH && !forest.containsLengthData()) || (sortOption == SortParam.FLUORESCENCE_CRIMSON && !forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON) || (sortOption == SortParam.FLUORESCENCE_YFP && !forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS))))
    		   continue;
    	   
    	   JMenuItem item = new JRadioButtonMenuItem(sortOption.toString());
    	   item.setToolTipText(sortOption.getTooltip());
    	   item.addActionListener(new ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					drawer.updateGraphSort(SortType.getSorter(SortType.LEAF, sortOption));
				}
			});
    	   sortGroup.add(item);
    	   treeSortLeafMenu.add(item);
       }
       
       // All options for branch sorting
       for(final SortParam sortOption : SortParam.values())
       {
    	   if((sortOption == SortParam.AREA && !forest.containsAreaData()) || (sortOption == SortParam.LENGTH && !forest.containsLengthData()) || (sortOption == SortParam.FLUORESCENCE_CRIMSON && !forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON) || (sortOption == SortParam.FLUORESCENCE_YFP && !forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS))))
    		   continue;

    	   JMenuItem item = new JRadioButtonMenuItem(sortOption.toString());
    	   item.setToolTipText(sortOption.getTooltip());
    	   item.addActionListener(new ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					drawer.updateGraphSort(SortType.getSorter(SortType.BRANCH, sortOption));
				}
			});
    	   sortGroup.add(item);
    	   treeSortBranchMenu.add(item);
       }
       
       // All options for arithmetical sorting
       for(final SortParam sortOption : SortParam.values())
       {
    	   if(sortOption == SortParam.ID)
    		   continue;

    	   if((sortOption == SortParam.AREA && !forest.containsAreaData()) || (sortOption == SortParam.LENGTH && !forest.containsLengthData()) || (sortOption == SortParam.FLUORESCENCE_CRIMSON && !forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON) || (sortOption == SortParam.FLUORESCENCE_YFP && !forest.containsFluorescenceData(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS))))
    		   continue;

    	   JMenuItem item = new JRadioButtonMenuItem(sortOption.toString());
    	   item.setToolTipText(sortOption.getTooltip());
    	   item.addActionListener(new ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					drawer.updateGraphSort(SortType.getSorter(SortType.ARITHMETICAL_AVERAGE, sortOption));
				}
			});
    	   sortGroup.add(item);
    	   treeSortArithmeticalMenu.add(item);
       }
       
       // add sub menus
       treeSortMenu.add(treeSortBranchMenu);
       treeSortMenu.add(treeSortLeafMenu);
       treeSortMenu.add(treeSortArithmeticalMenu);
       
       // layout options menu
       JMenu treeLayoutMenu = new JMenu("Tree layout");
       
       ActionListener treeLayoutClick = new ActionListener() {
      		
	   		@Override
	   		public void actionPerformed(ActionEvent e) {
	   			drawer.setGraphLayout(!drawer.isHorizontalLayout());
	   		}
       };
       JMenuItem treeLayoutHorizontalMenuItem = new JRadioButtonMenuItem("Horizontal", drawer.isHorizontalLayout());
       treeLayoutHorizontalMenuItem.addActionListener(treeLayoutClick);
       treeLayoutHorizontalMenuItem.setToolTipText("View the lineage tree in horizontal layout.");
       JMenuItem treeLayoutVerticalMenuItem = new JRadioButtonMenuItem("Vertical", !drawer.isHorizontalLayout());
       treeLayoutVerticalMenuItem.addActionListener(treeLayoutClick);
       treeLayoutVerticalMenuItem.setToolTipText("View the lineage tree in vertical layout.");
       
       layoutGroup.add(treeLayoutHorizontalMenuItem);
       layoutGroup.add(treeLayoutVerticalMenuItem);
       treeLayoutMenu.add(treeLayoutHorizontalMenuItem);
       treeLayoutMenu.add(treeLayoutVerticalMenuItem);
       
       this.add(enableScrollbarsMenuItem);
       this.addSeparator();
//       this.add(mouseModeMenu);
//       this.addSeparator();
//       this.add(addRootToTableMenuItem);
       this.add(addAllLeavesToTableMenuItem);
       this.addSeparator();
       this.add(showTreeSeperateMenuItem);
       this.addSeparator();
       this.add(levelInfosMenuItem);
       this.addSeparator();
       this.add(treeSortMenu);
       this.addSeparator();
       this.add(treeLayoutMenu);
       this.addSeparator();
       this.add(exportMenuItem);
       this.addSeparator();
       this.add(printMenuItem);
//       this.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        
    }
     
    private void setVisualizationMode(final VisualizationViewer panel, JRadioButtonMenuItem pickingMode,JRadioButtonMenuItem tranformingMode) {
//        pickingMode.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                DefaultModalGraphMouse gm = new DefaultModalGraphMouse<Cell, Clade>();
//                gm.setMode(ModalGraphMouse.Mode.PICKING);
//                panel.setGraphMouse(gm);
//                panel.repaint();
//                System.out.println("PICKING mouse mode");
//            }
//        });
//
//        tranformingMode.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                DefaultModalGraphMouse gm = new DefaultModalGraphMouse<Cell, Clade>();
//                gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
//                panel.setGraphMouse(gm);
//                System.out.println("TRANSFORMING mouse mode");
//                panel.repaint();
//            }
//        });
    }
    
	/**
	 * Adds leaves from the given phylogeny to the information table.
	 * 
	 * @param phylogeny
	 *            The phylogeny from which the leaves are added to the
	 *            information table
	 */
	public void addAllLeavesToTable(Phylogeny phylogeny) {
		// Add leaves from to the information table
    	List<Clade> leaves = phylogeny.getLeaves();
    	for (Clade leafClade : leaves) {
    		Cell c = leafClade.getCellObject();
    		
    		if ( c.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) != null ) {
                cp.addCellToCellsInformationTable(c.getId(), c.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS), c.getLength(), c.getArea());
        	} else {
        		cp.addCellToCellsInformationTable(c.getId(), defaultValueFluorescence, c.getLength(), c.getArea());
        	}
    	}
    }
    
    /**
     * Shows the phylogeny tree in separate Window.
     * 
     */
     public void showTreeInSeperateWindow() {
        JFrame frame = new JFrame(forest.getMetaxml().getProjectName());
        frame.getContentPane().add(graphComponent);
        frame.setSize(500, 500);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
     }

     public void showInfosAboutLevels(Phylogeny phylogeny) {
          PhyloTreesDepthAnalysis infoLevel = new PhyloTreesDepthAnalysis(phylogeny);
          infoLevel.setVisible(true);
//          infoLevel.dispose();
     }
     
/*     public void updateLayout(SortType type, SortParam param)
     {
    	 // create new tree which is sorted using new sort type and parameter
    	 TreeLayout<Cell, Clade> treeLayout = new TreeLayout<Cell, Clade>(new CreatePhyloTreeGraph(phylo, type, param).getPhyloTreeGraph(), 2, 2);
    	 
    	 // set the new layout
    	 treePanel.setGraphLayout(treeLayout);
    	 
    	 // actualise the GUI
    	 treePanel.revalidate();
    	 treePanel.repaint();
     }*/
    
    /***
     * Uses to export the phylogeny tree in several formats;
     * 
     */
    public void exportPhylogeneticTree() {
        JFileChooser chooserTreeExp = new JFileChooser();
        chooserTreeExp.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooserTreeExp.setMultiSelectionEnabled(false);
        chooserTreeExp.setAcceptAllFileFilterUsed(false);
        chooserTreeExp.addChoosableFileFilter(new PNGFileFilter());
        chooserTreeExp.addChoosableFileFilter(new SVGFileFilter());
        chooserTreeExp.addChoosableFileFilter(new JPEGFileFilter());
        chooserTreeExp.addChoosableFileFilter(new PDFFileFilter());
        int option = chooserTreeExp.showSaveDialog(graphComponent);
        if(option == JFileChooser.APPROVE_OPTION) {
            if(chooserTreeExp.getSelectedFile() != null) {
            String filePath = chooserTreeExp.getSelectedFile().getPath();
            String filterDescription = chooserTreeExp.getFileFilter().getDescription();//getChoosableFileFilters();
            BufferedImage bi = new BufferedImage((int) graphComponent.getBounds().getWidth(), 
                        (int) graphComponent.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB); 
                Graphics g = bi.createGraphics();

            // export graphic in png format
            if(filterDescription.equals("Portable Network Graphics (*.png)")) {
                filePath = filePath + ".png";
                graphComponent.paint(g);  
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
                svgGenerator.setSVGCanvasSize(new Dimension(graphComponent.getWidth(), graphComponent.getHeight()));
                boolean useCSS=true;
                try {
                        FileOutputStream os = new FileOutputStream(new File(filePath));
                        Writer out = new OutputStreamWriter(os, "UTF-8");
                        graphComponent.paint(svgGenerator);
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
                BufferedImage expImage = new BufferedImage(graphComponent.getWidth(),
                		graphComponent.getHeight(),BufferedImage.TYPE_INT_RGB);
                /*
                 * Print to Image, scaling if necessary.
                 */
                Graphics2D g2 = expImage.createGraphics();
                graphComponent.paint(g2);
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
                              graphComponent.getWidth(), 
                              graphComponent.getHeight());
                      Graphics2D g2 = template.createGraphics( 
                    		  graphComponent.getWidth(), 
                    		  graphComponent.getHeight());
                      graphComponent.print(g2);
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
