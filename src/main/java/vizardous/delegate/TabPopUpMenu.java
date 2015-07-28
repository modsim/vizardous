package vizardous.delegate;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import vizardous.delegate.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class TabPopUpMenu extends JPopupMenu {

//	private static TabPopUpMenu instance = new TabPopUpMenu();
	
	/** TODO Documentation */
	private static final long serialVersionUID = 5624457744209530075L;

	private MultipleChartsTab selectedChartTab;
	
	/**
	 * TODO Documentation
	 */
//	public TabPopUpMenu() {
//		init();
//	}
	
	public TabPopUpMenu(Component component) {
		if (component instanceof MultipleChartsTab) {
			this.selectedChartTab = (MultipleChartsTab) component;
			
			init();
		}
	}

	/**
	 * TODO Documentation
	 */
	private void init() {
		List<AbstractChart2D> charts = this.selectedChartTab.getCharts();		
		
		for (AbstractChart2D chart : charts) {
			final int index = charts.indexOf(chart);
			
			JMenuItem menuItem = new JMenuItem(chart.getTabName());
			this.add(menuItem);
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					selectedChartTab.setActiveChart(index);
				}
			});
		}		
	}
	
//	public static TabPopUpMenu getInstance() {
//		return instance;
//	}

	/**
	 * TODO Documentation
	 * 
	 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
	 */
	public static class CustomAdapter extends MouseAdapter
	{
	    /** TODO Documentation */
		private JTabbedPane tabbedPane;

		/**
		 * TODO Documentation
		 * 
		 * @param tabbedPane
		 */
	    public CustomAdapter ( JTabbedPane tabbedPane )
	    {
	        super ();
	        this.tabbedPane = tabbedPane;
	    }

	    public void mousePressed ( MouseEvent e )
	    {
	        final int index = tabbedPane.indexAtLocation(e.getX(), e.getY());
	        
	        if ( index != -1 )
	        {
	            if ( SwingUtilities.isLeftMouseButton ( e ) )
	            {
	                if ( tabbedPane.getSelectedIndex () != index )
	                {
	                    tabbedPane.setSelectedIndex ( index );
	                }
	                else if ( tabbedPane.isRequestFocusEnabled () )
	                {
	                    tabbedPane.requestFocusInWindow ();
	                }
	            }
	            else if ( SwingUtilities.isRightMouseButton ( e ) )
	            {
	                final TabPopUpMenu popupMenu = new TabPopUpMenu(tabbedPane.getComponentAt(index));
	                
	                /* Show the menu at the left border of the tab */
	                final Rectangle tabBounds = tabbedPane.getBoundsAt(index);
	                popupMenu.show(tabbedPane, tabBounds.x, tabBounds.y + tabBounds.height);
	            }
	        }
	    }
	}
	
}
