/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import vizardous.delegate.impl.graphics.export.Clipping;
import vizardous.delegate.impl.graphics.export.GraphicsExporter;
import vizardous.delegate.impl.graphics.util.PrintUtilities;
import vizardous.delegate.impl.analysis.PhyloTreesDepthAnalysis;

import com.mxgraph.swing.mxGraphComponent;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ScrollPaneConstants;

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
        JMenuItem exportLineageTreeViewportMenuItem = new JMenuItem("Export Lineage Tree (viewport)", new javax.swing.ImageIcon(getClass().getResource("/icons16x16/treeChart16x16.png")));
        exportLineageTreeViewportMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser.setDefaultLocale(loc);
                GraphicsExporter.exportLineageTree(graphComponent, Clipping.VIEWPORT);
           }
        });
        
        //exportMenuItem
        JMenuItem exportLineageTreeCompleteMenuItem = new JMenuItem("Export Lineage Tree (complete)", new javax.swing.ImageIcon(getClass().getResource("/icons16x16/treeChart16x16.png")));
        exportLineageTreeCompleteMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser.setDefaultLocale(loc);
                GraphicsExporter.exportLineageTree(graphComponent, Clipping.NONE);
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
       this.add(exportLineageTreeViewportMenuItem);
       this.add(exportLineageTreeCompleteMenuItem);
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
    
}
